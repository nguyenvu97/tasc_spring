package org.tasc.tasc_spring.user_service.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.model.status.TokenType;
import org.tasc.tasc_spring.api_common.redis_api.RedisApi;
import org.tasc.tasc_spring.api_common.model.request.AuthenticationRequest;
import org.tasc.tasc_spring.api_common.model.response.AuthenticationResponse;
import org.tasc.tasc_spring.user_service.auth.RegisterRequest;
import org.tasc.tasc_spring.user_service.config.JwtService;
import org.tasc.tasc_spring.user_service.config.LogoutService;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.user_service.model.Token;
import org.tasc.tasc_spring.user_service.model.User;
import org.tasc.tasc_spring.user_service.repository.TokenRepository;
import org.tasc.tasc_spring.user_service.repository.UserRepository;
import org.tasc.tasc_spring.user_service.service.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

import static org.tasc.tasc_spring.api_common.config.RedisConfig.Token_Key;
import static org.tasc.tasc_spring.api_common.ex.ExceptionMessagesEnum.CLIENT_NOT_FOUND;
import static org.tasc.tasc_spring.api_common.ex.ExceptionMessagesEnum.LOGIN_FAILS;


@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final LogoutService logoutService;
    private final RedisApi redisApi;
    @Override
    public ResponseData register(RegisterRequest request) {
        User user = User
                .builder()
                .address(request.getAddress())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .fullName(request.getFullName())
                .role(request.getRole())
                .build();
        userRepository.save(user);
       return ResponseData
               .builder()
               .message("createOk")
               .status_code(200)
               .data("Ok")
               .build();
    }


    @Override
//    @Transactional(rollbackOn = Exception.class)
    public ResponseData login(AuthenticationRequest request, HttpServletResponse response) throws EntityNotFound {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFound(LOGIN_FAILS.getDescription(), 400));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
           throw new EntityNotFound("user not found",404);
        }
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        save_token(jwtToken,request.getEmail());
        revokeAllUserTokens(user);
        saveUserToken(user, refreshToken);
        log.info("Fetching user details for userId: {}", jwtToken);
        return ResponseData
                .builder()
                .message("loginOk")
                .status_code(200)
                .data(AuthenticationResponse
                        .builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .build())
                .build();

    }

    private void save_token(String token,String email){
        Claims claims = jwtService.extractToken(token);
        redisApi.saveToken(Token_Key,claims.getSubject(),token,2592000);
        Map<String, String> claimsMap = new HashMap<>();
        claimsMap.put(token,email);
    }
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUser_id());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    private void saveUserToken(User user, String jwtToken) {

        Token token = tokenRepository.findByToken(user.getUser_id()).orElse(null);

        if (token != null) {
            token.setUser(user);
            token.setToken(jwtToken);
            token.setTokenType(TokenType.BEARER);
            token.setExpired(false);
            token.setRevoked(false);
            tokenRepository.save(token);
        } else {
         Token token1 = Token.builder()
                    .user(user)
                    .token(jwtToken)
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .build();
            tokenRepository.save(token1);  // Lưu token mới vào cơ sở dữ liệu
        }
    }




    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request,response,authentication);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {

    }
    @Override
    public ResponseData decode_token(String token)  {
        Claims claims = null;
        try {
            claims = jwtService.extractToken(token);

            System.out.println(claims);
            if (claims == null) {

                throw new EntityNotFound(CLIENT_NOT_FOUND.getDescription(), 400);
            }
        } catch (ExpiredJwtException e) {
            return ResponseData
                    .builder()
                    .status_code(401)
                    .message("Token đã hết hạn.")
                    .data(null)
                    .build();
        } catch (JwtException e) {
            return ResponseData
                    .builder()
                    .status_code(400)
                    .message("Lỗi khi giải mã JWT.")
                    .data(null)
                    .build();
        }
//   User user =  userRepository.findByEmail(claims.getSubject()).orElseThrow(()-> new EntityNotFound(CLIENT_NOT_FOUND.getDescription(), 400));
        return ResponseData
                .builder()
                .status_code(200)
                .message("OK")
                .data(CustomerDto
                        .builder()
                        .fullName((String) claims.get("fullName")) // Ép kiểu trực tiếp
                        .address((String) claims.get("address")) // Ép kiểu trực tiếp
                        .phone((String) claims.get("phone")) // Ép kiểu trực tiếp
                        .role((String) claims.get("role"))
                        .email(claims.getSubject())
                        .id(claims.getId())
                        .exp(claims.getExpiration().getTime())
                        .iat(claims.getIssuedAt().getTime())
                        .build())
                .build();
    }


}
