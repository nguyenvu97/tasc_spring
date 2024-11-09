//package org.tasc.tesc_spring.payment_service.vnpay;
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.tasc.tesc_spring.payment_service.vnpay.Config.*;
//
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/v1/vnpay")
//@Slf4j
//public class PayController {
////    public final Jwt jwtDecoder1;
////    public final MongoTemplate mongoTemplate;
////    private final  TicketApi   ticketApi;
////    private final EmailSender emailSender;
//
//
//    /*
//    *Cart Test
//    * Ngân hàng	NCB
//Số thẻ	9704198526191432198
//Tên chủ thẻ	NGUYEN VAN A
//Ngày phát hành	07/15
//Mật khẩu OTP	123456
//
//* customer pay
//*
//* */
//
//
//
//    @GetMapping
//    public ResponseEntity<?> hashUrl(@RequestParam(value = "orderNo") String orderNo) {
//
//        var listTicket = ticketApi.get(orderNo);
//        long money = 0;
//
//            money += (long) listTicket.getTotalTicket();
//
//
//        String vnp_TxnRef = getRandomNumber(8);
//        String vnp_IpAddr = "192.168.1.15";
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Amount", String.valueOf( money *100));
//        vnp_Params.put("vnp_Command", vnp_Command);
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//        vnp_Params.put("vnp_Locale", "vn");
//        vnp_Params.put("vnp_OrderInfo", orderNo);
//        vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_OrderType", "Quần áo");
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//        List fieldNames = new ArrayList(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = vnp_Params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = vnp_PayUrl + "?" + queryUrl;
//        ResultDto storeDto = new ResultDto();
//        storeDto.setStatus("200");
//        storeDto.setMessger("Successfully");
//        storeDto.setDate(LocalDateTime.now());
//        storeDto.setUrl(paymentUrl);
//
//        return ResponseEntity.status(HttpStatus.OK).body(storeDto);
//    }
//
//    @GetMapping("/domain.vn")
//    public String Transaction(
//            @RequestParam(value = "vnp_Amount") long amount,
//            @RequestParam(value = "vnp_BankCode") String BankCode,
//            @RequestParam(value = "vnp_OrderInfo") String OrderInfo,
//            @RequestParam(value = "vnp_ResponseCode") String responseCode,
//            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
//            @RequestParam(value = "vnp_TransactionNo") String vnp_TransactionNo
//    ) {
//        ResultDto storeDto = new ResultDto();
//        if (responseCode.equals("00")) {
//            storeDto.setStatus("ok");
//            storeDto.setMessger("thanh cong");
//            mongoTemplate.insert(VnpayInfo
//                    .builder()
//                            .amount(amount/100)
//                            .BankCode(BankCode)
//                            .responseCode(responseCode)
//                            .vnp_TxnRef(vnp_TxnRef)
//                            .vnp_TransactionNo(vnp_TransactionNo)
//                            .OrderInfo(OrderInfo)
//                    .build());
//            ticketApi.update(OrderInfo);
//
//            Ticket  tickets = ticketApi.get(OrderInfo);
//            delete_assets(tickets);
//
//            EmailDto emailDto = new EmailDto();
//            emailDto.setTo(tickets.getCustomers().get(0).getEmail());
//            emailDto.setBody(body(amount));
//            emailDto.setEmail(tickets.getCustomers().get(0).getEmail());
//            emailSender.sendemail(emailDto);
//
//
//
//
//            String body = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "    <title>Thanh Toán Thành Công </title>\n" + "    <style>\n" + "        body {\n" + "            font-family: Arial, sans-serif;\n" + "            text-align: center;\n" + "        }\n" + "        h1 {\n" + "            color: #008000;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    <h1>Thanh Toán Thành Công</h1>\n" + "    <p>Cảm Ơn Bạn Thanh Toán !</p>\n"  + "</body>\n" + "</html>\n";
//                return body;
//        }
//        String body = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "    <title>Thanh Toán Thất bại </title>\n" + "    <style>\n" + "        body {\n" + "            font-family: Arial, sans-serif;\n" + "            text-align: center;\n" + "        }\n" + "        h1 {\n" + "            color: #008000;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    <h1>Thanh Toán Thất bại</h1>\n"   + "</body>\n" + "</html>\n";
//        return body;
//
//    }
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
//
//    private void delete_assets(Ticket ticket){
//    List<Integer> flight_go_id = get_flight_id(ticket.getFlights_go());
//        Map<Integer,List<String>>  assets_go_child = new HashMap<>();
//    if (ticket.getFlights_go().getFlights() != null){
//         assets_go_child = assets_data(ticket.getCustomers(),2);
//    }
//
//        Map<Integer,List<String>>  assets_go = assets_data(ticket.getCustomers(),1);
//
//
//
//    if (ticket.getFlights_back() != null){
//
//        List<Integer>flight_back_id = get_flight_id(ticket.getFlights_back());
//        Map<Integer,List<String>> assets_back = assets_data(ticket.getCustomers(),3);
//        Map<Integer,List<String>>  assets_back_child = new HashMap<>() ;
//        if(ticket.getFlights_back().getFlights() != null){
//             assets_back_child = assets_data(ticket.getCustomers(),4);
//        }
//
//        for (Integer flightId : flight_back_id) {
//            if (assets_back.containsKey(flightId)) {
//                ticketApi.delete(flightId, assets_back.get(flightId));
//            }
//            if (assets_back_child.containsKey(flightId)) {
//                ticketApi.delete(flightId,  assets_back_child.get(flightId));
//
//            }
//        }
//    }
//        for (Integer flightId : flight_go_id) {
//            if (assets_go.containsKey(flightId)) {
//
//                ticketApi.delete(flightId, assets_go.get(flightId));
//            }
//            if (assets_go_child.containsKey(flightId)) {
//
//                ticketApi.delete(flightId, assets_go_child.get(flightId));
//
//            }
//        }
//    }
//    private List<Integer>get_flight_id(FlightTickitDto flightTickitDto){
//        List<Integer> flight_id = new ArrayList<>();
//        if (flightTickitDto != null) {
//            flight_id.add(flightTickitDto.getId());
//            if (flightTickitDto.getFlights() != null) {
//                flight_id.add(flightTickitDto.getFlights().get(0).getId());
//            }
//        }
//        return flight_id;
//    }
//    private Map<Integer, List<String>> assets_data(List<CustomerDto> customerDtos, int choose) {
//        Map<Integer,List<String> > result = new HashMap<>();
//        List<String>assets = new ArrayList<>();
//        switch (choose) {
//            case 1:
//                assets.clear();
//                customerDtos.forEach(customer -> {
//                    assets.addAll(customer.getSeats_go().values());
//                    result.put(customer.getSeats_go().keySet().iterator().next(), assets);
//                });
//                break;
//            case 2:
//                assets.clear();
//                customerDtos.forEach(customer -> {
//
//                    assets.addAll(customer.getSeats_go_child().values());
//                    result.put(customer.getSeats_go_child().keySet().iterator().next(), assets);
//                });
//                break;
//            case 3:
//                assets.clear();
//                customerDtos.forEach(customer -> {
//                    assets.addAll(customer.getSeats_back_child().values());
//                    result.put(customer.getSeats_back_child().keySet().iterator().next(), assets);
//                });
//                break;
//            case 4:
//                assets.clear();
//                customerDtos.forEach(customer -> {
//
//                    assets.addAll(customer.getSeats_back().values());
//                    result.put(customer.getSeats_back().keySet().iterator().next(), assets);
//                });
//                break;
//        }
//        log.info(result.toString());
//
//        return result;
//
//    }
//}
