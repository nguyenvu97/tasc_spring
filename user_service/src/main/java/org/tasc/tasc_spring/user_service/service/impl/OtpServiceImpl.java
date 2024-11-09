package org.tasc.tasc_spring.user_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.ResponseData;
import org.tasc.tasc_spring.user_service.dto.request.RequestOtp;
import org.tasc.tasc_spring.user_service.model.Otp;
import org.tasc.tasc_spring.user_service.model.User;
import org.tasc.tasc_spring.user_service.repository.UserRepository;
import org.tasc.tasc_spring.user_service.service.OtpService;
import redis.clients.jedis.Jedis;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import static org.tasc.tasc_spring.api_common.config.RedisConfig.Otp_Key;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final UserRepository userRepository;
    private final Jedis jedis;
    private final PasswordEncoder passwordEncoder;
    public static String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public ResponseData authenticateUser(String email){
        User customer = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFound("not found",404));
        try {
            return generateAndSaveOTP(customer);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ResponseData generateAndSaveOTP(User user) throws JsonProcessingException {

        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(zoneId);
        long currentTimeMillis = zonedDateTime.toEpochSecond() * 1000;
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        int otp = gAuth.getTotpPassword(hashSHA256(user.email),currentTimeMillis);
        Otp otp1 = new Otp();
        otp1.setCreateAt(LocalDateTime.now().toString());
        otp1.setNumber(otp);
        Map<String,String> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String otpJson = objectMapper.writeValueAsString(otp1);
        map.put(user.getEmail(), otpJson);
        log.info(otp1.toString());
        jedis.hset(Otp_Key,map);
        return ResponseData
                .builder()
                .message("ok")
                .status_code(200)
                .data(otp)
                .build();
    }
    public ResponseData verifyOTP(RequestOtp requestOtp) {
        Map<String, String> otpData = jedis.hgetAll(Otp_Key);
        if (otpData.containsKey(requestOtp.getEmail())){
            String data = otpData.get(requestOtp.getEmail());
            Otp otp1 ;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                otp1 = objectMapper.readValue(data, Otp.class);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            String createAtString = otp1.getCreateAt();  // Giả sử chuỗi có dạng "2024-11-08T15:06:25.015627"
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;  // Dùng định dạng chuẩn ISO
            LocalDateTime createTotpTime = LocalDateTime.parse(createAtString, formatter);
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(createTotpTime, currentTime);
            long minutesDifference = duration.toMinutes();
            if (CheckTime(minutesDifference)) {
                User user = userRepository.findByEmail(requestOtp.getEmail()).orElseThrow(null);
                GoogleAuthenticator gAuth = new GoogleAuthenticator();
                boolean isValid = gAuth.authorize(hashSHA256(requestOtp.getEmail()), requestOtp.getOtp());
                if (isValid) {
                    user.setPassword(passwordEncoder.encode(requestOtp.getNewPassword()));
                    userRepository.save(user);
                    jedis.hdel(Otp_Key, requestOtp.getEmail());
                    return ResponseData
                            .builder()
                            .data("change the password OK")
                            .message("ok")
                            .status_code(200)
                            .build();
                }
            }
            jedis.hdel(Otp_Key, requestOtp.getEmail());
            throw  new EntityNotFound("Expired otp code",404);

        }
        throw new EntityNotFound("not found",404);



    }


    private boolean CheckTime(long time ) {
        return time <= 2;
    }
}
