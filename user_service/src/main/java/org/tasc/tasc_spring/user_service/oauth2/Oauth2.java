package org.tasc.tasc_spring.user_service.oauth2;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Oauth2 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String userId;
    private String email;
    @Enumerated(value = EnumType.STRING)
    private LoginStatus loginStatus;
    private String premiunStatus;
    private String picture;
    private int fonos;

}
