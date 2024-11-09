package org.tasc.tasc_spring.user_service.oauth2.Oauth2Token;


import jakarta.persistence.*;
import lombok.*;
import org.tasc.tasc_spring.user_service.model.status.TokenType;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Oauth2Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(unique = true)
    private String token;
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;
    public String exp;
    public String refreshToken;
    private String userId;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    public boolean expired = false;

}
