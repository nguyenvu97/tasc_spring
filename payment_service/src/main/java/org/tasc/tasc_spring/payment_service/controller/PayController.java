package org.tasc.tasc_spring.payment_service.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.payment_service.model.ResultDto;
import org.tasc.tasc_spring.payment_service.service.PayService;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

import static org.tasc.tasc_spring.payment_service.config.Config.*;


@RestController
@RequestMapping("api/v1/vnpay")
@Slf4j
@RequiredArgsConstructor
public class PayController {
private final PayService payService;

    /*
    *Cart Test
    * Ngân hàng	NCB
Số thẻ	9704198526191432198
Tên chủ thẻ	NGUYEN VAN A
Ngày phát hành	07/15
Mật khẩu OTP	123456

* customer pay
*
* */

    @GetMapping("/get")
    public ResponseEntity<?> hashUrl(@RequestParam(value = "orderNo") String orderNo, @RequestHeader(value = "Authorization") String token) {
        try{
            return ResponseEntity.ok().body(payService.hashUrl(orderNo, token));
        }catch (EntityNotFound e){
            throw e;
        }
    }
    @GetMapping("/domain.vn")
    public ResponseEntity<?> Transaction(
            @RequestParam(value = "vnp_Amount") long amount,
            @RequestParam(value = "vnp_BankCode") String BankCode,
            @RequestParam(value = "vnp_OrderInfo") String OrderInfo,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(value = "vnp_TransactionNo") String vnp_TransactionNo
    ) {
        try{
          return  ResponseEntity.ok().body(payService.Transaction(amount, BankCode, OrderInfo, responseCode, vnp_TxnRef, vnp_TransactionNo));
        }catch (EntityNotFound e){
            throw e;
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?>searchPay(@RequestParam(value = "vnp_TxnRef")String vnp_TxnRef , @RequestParam(value = "vnp_OrderInfo") String vnp_OrderInfo,@RequestParam(value ="vnp_CreateDate" )String vnp_CreateDate ){
        String vnp_IpAddr = "192.168.1.15";

        Map<String, String> vnp_Params = new HashMap<>();
        String vnp_RequestId = getRandomNumber(8);
        vnp_Params.put("vnp_RequestId", vnp_RequestId);
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef",vnp_TxnRef );
        vnp_Params.put("vnp_OrderInfo",vnp_OrderInfo);
        vnp_Params.put("vnp_TransactionNo","");
        vnp_Params.put("vnp_TransactionDate",vnp_CreateDate);
        vnp_Params.put("vnp_CreateDate",vnp_CreateDate);
        vnp_Params.put("vnp_IpAddr",vnp_IpAddr);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        String data = vnp_RequestId + "|" + vnp_Version + "|" + vnp_Command + "|" + vnp_TmnCode + "|" +
                vnp_TxnRef + "|" + vnp_CreateDate + "|" + vnp_CreateDate + "|" + vnp_IpAddr + "|" +
                vnp_OrderInfo;
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, data);
        String url ;
        try {
            url = vnp_apiUrl + "?" +
                    "vnp_RequestId=" + vnp_RequestId +
                    "&vnp_Version=" + vnp_Version +
                    "&vnp_Command=" + vnp_Command +
                    "&vnp_TmnCode=" + vnp_TmnCode +
                    "&vnp_TxnRef=" + vnp_TxnRef +
                    "&vnp_TransactionDate=" + vnp_CreateDate +
                    "&vnp_CreateDate=" + vnp_CreateDate +
                    "&vnp_IpAddr=" + vnp_IpAddr +
                    "&vnp_OrderInfo=" + URLEncoder.encode(vnp_OrderInfo, "UTF-8") +
                    "&vnp_SecureHash=" + vnp_SecureHash;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        ResultDto storeDto = new ResultDto();
        storeDto.setStatus("200");
        storeDto.setMessenger("Successfully");
        storeDto.setDate(LocalDateTime.now());
        storeDto.setUrl(url);
        return ResponseEntity.status(HttpStatus.OK).body(storeDto);
    }





}
