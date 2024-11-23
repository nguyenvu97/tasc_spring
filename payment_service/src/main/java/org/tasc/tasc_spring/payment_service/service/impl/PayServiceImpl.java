package org.tasc.tasc_spring.payment_service.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.request.AuthenticationRequest;
import org.tasc.tasc_spring.api_common.model.request.ProductRequest;
import org.tasc.tasc_spring.api_common.model.response.AuthenticationResponse;
import org.tasc.tasc_spring.api_common.model.response.CustomerDto;
import org.tasc.tasc_spring.api_common.model.response.OrderDto;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;
import org.tasc.tasc_spring.api_common.order_api.Order_Api;
import org.tasc.tasc_spring.api_common.product_api.ProductApi;
import org.tasc.tasc_spring.api_common.user_api.UserApi;
import org.tasc.tasc_spring.payment_service.model.Transaction;
import org.tasc.tasc_spring.payment_service.service.PayService;
import org.tasc.tasc_spring.payment_service.service.TransactionService;
import org.tasc.tasc_spring.payment_service.model.ResultDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.tasc.tasc_spring.api_common.javaUtils.DecodeToken.get_customer;
import static org.tasc.tasc_spring.payment_service.config.Config.*;
import static org.tasc.tasc_spring.payment_service.config.Config.vnp_PayUrl;

@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {
    private  final ObjectMapper objectMapper;
    private final UserApi userApi;
    private  final Order_Api order_api;
    private final ProductApi productApi;
    private final TransactionService transactionService;
    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public ResponseData hashUrl(String orderNo, String token,HttpServletRequest request)  {
        CustomerDto customerDto = get_customer(token,userApi,objectMapper);
        if (customerDto.getId() == null || customerDto.getId().isEmpty()) {
            throw  new EntityNotFound("token not found",404);
        }
        ResponseData responseData = order_api.getOrderNo(orderNo);
        OrderDto orderDto = null;
        if (responseData.status_code == 200 && responseData.data != null) {
            orderDto = objectMapper.convertValue(responseData.data, OrderDto.class);
        }
        System.out.println(orderDto.getTotalPrice());
        Long amount = (long) orderDto.getTotalPrice() * 100;
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
        if (transactionService.countTransaction(orderNo) >=1) {
            throw new EntityNotFound("transaction has create",404);
        }
            ResponseData responseData1 = transactionService.createTransaction(Transaction
                    .builder()
                    .method("vnpay")
                    .orderStatus(OrderStatus.PENDING)
                    .orderNo(orderNo)
                    .userId(customerDto.getId())
                    .amount(amount)
                    .requestId(request.getRequestId())
                    .createdAt(LocalDateTime.now())
                    .build());

        if (responseData1.status_code != 200) {
            throw  new EntityNotFound("save not transaction ",404);
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnp_PayUrl + "?" + queryUrl;
        return ResponseData
                .builder()
                .message("Successfully")
                .status_code(200)
                .data(paymentUrl)
                .build();
    }

    @Override
    public String Transaction(long amount, String BankCode, String OrderInfo, String responseCode, String vnp_TxnRef, String vnp_TransactionNo) {
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

                AuthenticationRequest authenticationRequest = new AuthenticationRequest(adminUsername,adminPassword);
                ResponseData res1= userApi.authenticate(authenticationRequest);
                if (res1.status_code == 200 && res1.data != null) {
                    AuthenticationResponse res = objectMapper.convertValue(res1.data, new TypeReference<AuthenticationResponse>() {});
                    ResponseData responseData1 = productApi.update(productRequests,res.getAccessToken());
                    if (responseData1.status_code == 200  ) {
                        ResponseData rs =  transactionService.updateTransaction(OrderInfo,1);
                        if (rs.status_code != 200) {
                            throw new EntityNotFound("save not transaction  ",404);
                        }
                        System.out.println("send email");
                    }else {
                        ResponseData responseData2 = order_api.create(4,OrderInfo);
                        if (responseData2.status_code == 200){
                            throw new EntityNotFound("Quantity not available",404);
                        }
                        ResponseData rs =  transactionService.updateTransaction(OrderInfo,4);
                        if (rs.status_code != 200) {
                            throw new EntityNotFound("save not transaction  ",404);
                        }

                    }
                }


            }
            return generateHtmlResponse("Thanh Toán Thành Công","Cảm Ơn Bạn Thanh Toán !");
        }
        ResponseData responseData2 = order_api.create(2,OrderInfo);
        if (responseData2.status_code == 200){
            throw new EntityNotFound("Quantity not available",404);
        }
        ResponseData rs =  transactionService.updateTransaction(OrderInfo,2);
        if (rs.status_code != 200) {
            throw new EntityNotFound("save not transaction ",404);
        }

        return  generateHtmlResponse("Thanh Toán Thất bại","Thanh Toán Thất bại");

    }

    private String generateHtmlResponse(String title, String message) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>" + title + "</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #008000;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>" + title + "</h1>\n" +
                "    <p>" + message + "</p>\n" +
                "</body>\n" +
                "</html>\n";
    }

    /// gioi thieu ve ban than
    // cac cong nghe su dung trong du an
    // co nhung sservice
    // minh dc phan cong trogn du an do
    // du an do bui theo microservice
}
