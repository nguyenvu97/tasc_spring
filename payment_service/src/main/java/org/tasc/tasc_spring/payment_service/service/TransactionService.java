package org.tasc.tasc_spring.payment_service.service;

import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;
import org.tasc.tasc_spring.payment_service.model.Transaction;

public interface TransactionService {
    ResponseData createTransaction(Transaction transaction);
    ResponseData updateTransaction(String orderNo,int choose);
    int countTransaction(String orderNo);
}
