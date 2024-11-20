package org.tasc.tasc_spring.payment_service.vnpay;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.order_api.Order_Api;
import org.tasc.tasc_spring.api_common.product_api.ProductApi;
import org.tasc.tasc_spring.api_common.user_api.UserApi;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.tasc.tasc_spring.api_common.javaUtils.DecodeToken.get_customer;
import static org.tasc.tasc_spring.payment_service.vnpay.Config.*;


@RestController
@RequestMapping("api/v1/vnpay")
@Slf4j
@RequiredArgsConstructor
public class PayController {
private  final ObjectMapper objectMapper;
private final UserApi userApi;
private  final Order_Api order_api;
private final ProductApi productApi;



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
        CustomerDto customerDto = get_customer(token,userApi,objectMapper);
        if (customerDto.getId() == null || customerDto.getId().isEmpty()) {
            throw  new EntityNotFound("token not fould",404);
        }
        ResponseData responseData = order_api.getOrderNo(orderNo);
        OrderDto orderDto = null;
        if (responseData.status_code == 200 && responseData.data != null) {
             orderDto = objectMapper.convertValue(responseData.data, OrderDto.class);

        }
        System.out.println(orderDto.getTotalPrice());
        Long amount = (long) orderDto.getTotalPrice() *100;
        String vnp_TxnRef = getRandomNumber(8);
        System.out.println("vnp_TxnRef" + vnp_TxnRef);
        String vnp_IpAddr = "192.168.1.15";
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_Command", vnp_Command);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderInfo", orderNo);
        vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_OrderType", "Quần áo");
        cld.add(Calendar.MINUTE, 15);
        System.out.println(vnp_CreateDate);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnp_PayUrl + "?" + queryUrl;
        ResultDto storeDto = new ResultDto();
        storeDto.setStatus("200");
        storeDto.setMessenger("Successfully");
        storeDto.setDate(LocalDateTime.now());
        storeDto.setUrl(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(storeDto);
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

    @GetMapping("/domain.vn")
    public String Transaction(
            @RequestParam(value = "vnp_Amount") long amount,
            @RequestParam(value = "vnp_BankCode") String BankCode,
            @RequestParam(value = "vnp_OrderInfo") String OrderInfo,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(value = "vnp_TransactionNo") String vnp_TransactionNo
    ) {
        ResultDto storeDto = new ResultDto();
        if (responseCode.equals("00")) {
            storeDto.setStatus("ok");
            storeDto.setMessenger("thanh cong");

             ResponseData responseData = order_api.create(1,OrderInfo);
            if (responseData.status_code == 200 && responseData.message.equals("SUCCESS")) {
                OrderDto orderDto = objectMapper.convertValue(responseData.data, OrderDto.class);
                List<ProductRequest> productRequests = new ArrayList<>();
                orderDto.getOrderDetailsList().forEach(p ->
                        productRequests.add(ProductRequest
                                .builder()
                                .productId(p.getProductId())
                                .quantity(p.getQuantity())
                                .build())
                );
                productApi.update(productRequests);
            }
            String body = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "    <title>Thanh Toán Thành Công </title>\n" + "    <style>\n" + "        body {\n" + "            font-family: Arial, sans-serif;\n" + "            text-align: center;\n" + "        }\n" + "        h1 {\n" + "            color: #008000;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    <h1>Thanh Toán Thành Công</h1>\n" + "    <p>Cảm Ơn Bạn Thanh Toán !</p>\n"  + "</body>\n" + "</html>\n";
                return body;
        }
         order_api.create(2,OrderInfo);
        String body = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "    <title>Thanh Toán Thất bại </title>\n" + "    <style>\n" + "        body {\n" + "            font-family: Arial, sans-serif;\n" + "            text-align: center;\n" + "        }\n" + "        h1 {\n" + "            color: #008000;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    <h1>Thanh Toán Thất bại</h1>\n"   + "</body>\n" + "</html>\n";
        return body;

    }
//    public String body (long money){
//        return  "<!DOCTYPE html>\n" +
//                "<html lang=\"vi\">\n" +
//                "<head>\n" +
//                "    <meta charset=\"UTF-8\">\n" +
//                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                "    <title>Cảm ơn bạn đã đặt hàng</title>\n" +
//                "    <style>\n" +
//                "        body {\n" +
//                "            font-family: Arial, sans-serif;\n" +
//                "            background-color: #f4f4f4;\n" +
//                "            color: #333;\n" +
//                "            padding: 20px;\n" +
//                "        }\n" +
//                "        .container {\n" +
//                "            background: white;\n" +
//                "            padding: 20px;\n" +
//                "            border-radius: 5px;\n" +
//                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
//                "        }\n" +
//                "        table {\n" +
//                "            width: 100%;\n" +
//                "            border-collapse: collapse;\n" +
//                "            margin: 20px 0;\n" +
//                "        }\n" +
//                "        th, td {\n" +
//                "            padding: 10px;\n" +
//                "            border: 1px solid #ddd;\n" +
//                "            text-align: left;\n" +
//                "        }\n" +
//                "        th {\n" +
//                "            background-color: #f2f2f2;\n" +
//                "        }\n" +
//                "    </style>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "\n" +
//                "<div class=\"container\">\n" +
//                "    <h2>Cảm ơn bạn đã đặt vé!</h2>\n" +
//                "    <p>Tổng giá trị đơn hàng: <strong>" + money+"VNĐ</strong></p>\n" +
//
//
//                "     <p>Mọi thông tin trên số hotline: 19001000</p>\n\" +" +
//                "</body>\n" +
//                "</html>\n";
//    }



}
