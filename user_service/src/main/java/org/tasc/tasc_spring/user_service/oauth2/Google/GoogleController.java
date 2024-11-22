//package org.tasc.tasc_spring.user_service.oauth2.Google;
//
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
//import org.tasc.tasc_spring.user_service.oauth2.LoginStatus;
//import org.tasc.tasc_spring.user_service.oauth2.Oauth2;
//import org.tasc.tasc_spring.user_service.oauth2.Oauth2Repository;
//import org.tasc.tasc_spring.user_service.oauth2.Oauth2Token.Oauth2Token;
//import org.tasc.tasc_spring.user_service.oauth2.Oauth2Token.Oauth2TokenRepository;
//import org.tasc.tasc_spring.api_common.model.response.AuthenticationResponse;
//import org.tasc.tasc_spring.user_service.model.status.TokenType;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class GoogleController {
//
//    @Value("${google.client.id}")
//    private String clientId;
//
//    @Value("${google.client.secret}")
//    private String clientSecret;
//
//    @Value("${google.client.uri}")
//    private String redirectUri;
//
//    public final Oauth2Repository oauth2Repository;
//
//    public  final Oauth2TokenRepository oauth2TokenRepository;
//
//    public final RestTemplate restTemplate;
//    /*
//    sendRedirect Google strat for api
//    * */
//    @GetMapping("google/login")
//    public void googleLogin(HttpServletResponse response) throws IOException {
//        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
//                "scope=https%3A//www.googleapis.com/auth/drive.metadata.readonly&" +
//                "access_type=offline&include_granted_scopes=true&response_type=code&state=state_parameter_passthrough_value&" +
//                "redirect_uri=http://localhost:8083/login/oauth2/code/google&client_id=438887675887-l9f6rhmcuno8rkpuq3ts520okdupdots.apps.googleusercontent.com";
//        response.sendRedirect(authUrl);
//    }
//    /*
//    toke a code Auth 2 keyword = "code"
//    * */
//    @GetMapping("/login/oauth2/code/google")
//    public AuthenticationResponse handleGoogleAuthorizationCode(@RequestParam("code") String authorizationCode, HttpServletResponse response) {
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.put("code", Collections.singletonList(authorizationCode));
//        body.put("client_id", Collections.singletonList(clientId));
//        body.put("client_secret", Collections.singletonList(clientSecret));
//        body.put("redirect_uri", Collections.singletonList(redirectUri));
//        body.put("grant_type", Collections.singletonList("authorization_code"));
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
//        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
//                "https://oauth2.googleapis.com/token",
//                entity,
//                Map.class
//        );
//        Map<String, Object> responseBody = responseEntity.getBody();
//        String accessToken = (String) responseBody.get("access_token");
//        String refestoken = (String)  responseBody.get("refresh_token");
//        userInfo(accessToken,refestoken);
//       return AuthenticationResponse
//               .builder()
//               .accessToken(accessToken)
//               .refreshToken(refestoken)
//               .build();
//    }
//
//    /*
//    save user info google accessToken
//    * */
//    public void userInfo(String token,String refestoken){
//        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
//        HttpHeaders headers = new HttpHeaders();
//        System.out.println(token);
//        headers.set("Authorization", "Bearer " + token);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        System.out.println(entity);
//
//        ResponseEntity<Map> response1 = restTemplate.getForEntity(
//                "https://www.googleapis.com/oauth2/v3/userinfo",
//                Map.class,
//                entity
//
//        );
//        if (response1.getBody() == null){
//            throw  new EntityNotFound("not found",404);
//        }
//        Map<String, Object> userInfo = response1.getBody();
//        String email = (String) userInfo.get("email");
//        String sub = (String) userInfo.get("sub");
//        String id = (String) userInfo.get("id");
//        String picture =(String) userInfo.get("picture");
//        Oauth2 oauth2 = oauth2Repository.findByUserId(id);
//        if (oauth2 == null){
//            oauth2Repository.save(Oauth2
//                    .builder()
//                            .userId(id)
//                            .email(email)
//                            .fonos(0)
//                            .picture(picture)
//                            .loginStatus(LoginStatus.Google)
//                    .build());
//            oauth2TokenRepository.save(Oauth2Token
//                    .builder()
//                            .userId(id)
//                            .expired(false)
//                            .tokenType(TokenType.BEARER)
//                            .token(token)
//                            .refreshToken(refestoken)
//                            .created_at(LocalDateTime.now())
//                    .build());
//        }else {
//            oauth2TokenRepository.save(Oauth2Token
//                    .builder()
//                    .userId(id)
//                    .expired(false)
//                    .tokenType(TokenType.BEARER)
//                    .token(token)
//                    .created_at(LocalDateTime.now())
//                    .refreshToken(refestoken)
//                    .build());
//        }
//        System.out.println(response1.getBody());
//    }
//    /*
//    refresh_token api google
//    * */
//
//    public AuthenticationResponse refresh_token ( String refreshToken ){
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.put("client_id", Collections.singletonList(clientId));
//        body.put("client_secret", Collections.singletonList(clientSecret));
//        body.put("grant_type", Collections.singletonList("refresh_token"));
//        body.put("refresh_token", Collections.singletonList(refreshToken));
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
//        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
//                "https://oauth2.googleapis.com/token",
//                entity,
//                Map.class
//        );
//        Map<String, Object> responseBody = responseEntity.getBody();
//        String accessToken = (String) responseBody.get("access_token");
//        System.out.println("access_token " + accessToken);
//        return AuthenticationResponse
//                .builder()
//                .accessToken(accessToken)
//                .build();
//    }
//
//    //Check tg token xong de con lay lai token sua la jwtdecode
//
//    public void checkTimeLIneToken(String token){
//        var oauth2Token = oauth2TokenRepository.findByToken(token);
//        if (oauth2Token == null){
//            throw new EntityNotFound("not found" ,404);
//        }
//        LocalDateTime timeNow = LocalDateTime.now();
//        LocalDateTime expTime = oauth2Token.getCreated_at().plusMinutes(45);
//        if (timeNow.isAfter(expTime)){
//            refresh_token(oauth2Token.getRefreshToken());
//        }else {
//            oauth2Token.setExpired(true);
//            oauth2TokenRepository.save(oauth2Token);
//        }
//    }
//}
