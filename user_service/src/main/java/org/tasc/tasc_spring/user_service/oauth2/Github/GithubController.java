//package org.tasc.tasc_spring.user_service.oauth2.Github;
//
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import org.tasc.tasc_spring.user_service.oauth2.LoginStatus;
//import org.tasc.tasc_spring.user_service.oauth2.Oauth2;
//import org.tasc.tasc_spring.user_service.oauth2.Oauth2Repository;
//import org.tasc.tasc_spring.user_service.oauth2.Oauth2Token.Oauth2Token;
//import org.tasc.tasc_spring.user_service.oauth2.Oauth2Token.Oauth2TokenRepository;
//import org.tasc.tasc_spring.user_service.auth.AuthenticationResponse;
//import org.tasc.tasc_spring.user_service.model.status.TokenType;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Map;
//import java.util.Random;
//
//@RestController
//@RequiredArgsConstructor
//public class GithubController {
//
//    public final RestTemplate restTemplate;
//
//    public final Oauth2Repository oauth2Repository;
//
//    public final Oauth2TokenRepository oauth2TokenRepository;
//    @Value("${github.client.id}")
//    private String clientId;
//
//    @Value("${github.client.secret}")
//    private String clientSecret;
//
//    @Value("${github.client.uri}")
//    private String redirectUri;
//
//
//    //     random state
//    public String random() {
//        Random random = new Random();
//        return String.valueOf(random.nextInt(10000));
//    }
//
//    @GetMapping("git/login")
//    public void githubLogin(HttpServletResponse response) throws IOException {
//        String authorizeUrl = "https://github.com/login/oauth/authorize" +
//                "?client_id=" + clientId +
//                "&redirect_uri=" + redirectUri +
//                "&login=suggested_username" +
//                "&scope=repo,user" +
//                "&state=" + random() +
//                "&allow_signup=true";
//        response.sendRedirect(authorizeUrl);
//    }
//
//    @GetMapping("/login/oauth2/code/github")
//    private AuthenticationResponse takeCodeGit(@RequestParam("code") String authorizationCode){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
//        body.put("client_id", Collections.singletonList(clientId));
//        body.put("client_secret", Collections.singletonList(clientSecret));
//        body.put("code", Collections.singletonList(authorizationCode));
//        body.put("redirect_uri",Collections.singletonList(redirectUri));
//        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(body,headers);
//        ResponseEntity<Map> response = restTemplate.postForEntity(
//                "https://github.com/login/oauth/access_token",
//                entity,
//                Map.class);
//        System.out.println( response.getBody());
//        System.out.println(authorizationCode);
//
//        Map<String,String>getToken = response.getBody();
//        String accessToken = getToken.get("access_token");
//        System.out.println(response.getBody());
//        useTokenGetInfo(accessToken);
//        System.out.println("accessToken : " + accessToken);
//        return AuthenticationResponse
//                .builder()
//                .accessToken(accessToken)
//                .build();
//    }
//    private void useTokenGetInfo(String access_Token){
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + access_Token);
//        String url = "https://api.github.com/user";
//        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
//        ResponseEntity<Map> response = restTemplate.postForEntity(
//                url,
//                httpEntity,
//                Map.class);
//        Map<String, Object> userInfo = response.getBody();
//        System.out.println(userInfo);
//        String login = (String) userInfo.get("login");
//        String avatar_url = (String) userInfo.get("avatar_url");
//        int id = (int) userInfo.get("id");
//        Oauth2 oauth2 = oauth2Repository.findByUserId(String.valueOf(id));
//        if (oauth2 == null){
//            oauth2Repository.save(Oauth2
//                    .builder()
//                    .userId(String.valueOf(id))
//                    .email(login)
//                    .fonos(0)
//                    .picture(avatar_url)
//                    .loginStatus(LoginStatus.Google)
//                    .build());
//            oauth2TokenRepository.save(Oauth2Token
//                    .builder()
//                    .userId(String.valueOf(id))
//                    .expired(false)
//                    .tokenType(TokenType.BEARER)
//                    .token(access_Token)
////                    .refreshToken(refestoken)
//                    .build());
//        }else {
//            oauth2TokenRepository.save(Oauth2Token
//                    .builder()
//                    .userId(String.valueOf(id))
//                    .expired(false)
//                    .tokenType(TokenType.BEARER)
//                    .token(access_Token)
//                    .build());
//        }
//
//    }
//
//
//}
