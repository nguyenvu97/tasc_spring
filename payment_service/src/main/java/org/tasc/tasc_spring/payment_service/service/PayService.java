package org.tasc.tasc_spring.payment_service.service;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;

public interface PayService {
    ResponseData hashUrl( String orderNo, String token);
    String Transaction ( long amount, String BankCode, String OrderInfo, String responseCode, String vnp_TxnRef, String vnp_TransactionNo);
}
