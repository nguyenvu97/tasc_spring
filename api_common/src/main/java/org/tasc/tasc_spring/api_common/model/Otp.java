package org.tasc.tasc_spring.api_common.model;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Otp implements Serializable {
    private String createAt;
    private int number;


}
