package org.tasc.tasc_spring.payment_service.vnpay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {

    private String status;
    private String messenger;
    private LocalDateTime date;
    private String url;

}
