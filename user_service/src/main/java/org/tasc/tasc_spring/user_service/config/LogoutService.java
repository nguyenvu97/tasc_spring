package org.tasc.tasc_spring.user_service.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.redis_api.RedisApi;
import org.tasc.tasc_spring.user_service.repository.TokenRepository;

import java.util.UUID;

import static org.tasc.tasc_spring.api_common.config.RedisConfig.Token_Key;


@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService  implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final RedisApi redisApi;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final  String authHeader = request.getHeader("Authorization");
        final String jwt ;
        if (authHeader == null && !authHeader.startsWith("Bearer ")){
            return;
        }
        jwt = authHeader.substring(7);

        Claims claims = jwtService.extractToken(jwt);
        var storedToken = tokenRepository.findByToken(UUID.fromString(claims.getId())).orElse(null);
        redisApi.delete(Token_Key,claims.getSubject());
        if (storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
