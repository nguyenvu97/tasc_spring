package org.tasc.tasc_spring.payment_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.response.ResponseData;
import org.tasc.tasc_spring.api_common.model.status.OrderStatus;
import org.tasc.tasc_spring.payment_service.model.Transaction;
import org.tasc.tasc_spring.payment_service.reposittory.TransactionRepository;
import org.tasc.tasc_spring.payment_service.service.TransactionService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public ResponseData createTransaction(Transaction transaction) {
        return ResponseData
                .builder()
                .data(transactionRepository.save(transaction))
                .status_code(200)
                .message("Transaction Created")
                .build();
    }

    @Override
    @Transactional
    public ResponseData updateTransaction(String orderNo,int choose) {
            Transaction transaction = transactionRepository.findByOrderNo(orderNo);
        if (transaction == null) {
            throw new EntityNotFound("orderNo not found", 404);
        }
        ResponseData.ResponseDataBuilder responseBuilder = ResponseData.builder().status_code(200);

        switch (choose) {
            case 1:
                transaction.setOrderStatus(OrderStatus.SUCCESS);
                transaction.setUpdatedAt(LocalDateTime.now());
                responseBuilder.message("SUCCESS").data("ok");
                transactionRepository.save(transaction);
                break;
            case 2:
                transaction.setOrderStatus(OrderStatus.FAILURE);
                transaction.setUpdatedAt(LocalDateTime.now());
                responseBuilder.message("FAILURE").data(null);
                transactionRepository.save(transaction);
                break;
            case 3:
                transaction.setOrderStatus(OrderStatus.CANCELLED);
                transaction.setUpdatedAt(LocalDateTime.now());
                responseBuilder.message("CANCELLED").data(null);
                transactionRepository.save(transaction);
                break;
            case 4:
                transaction.setOrderStatus(OrderStatus.ERROR);
                transaction.setUpdatedAt(LocalDateTime.now());
                responseBuilder.message("ERROR").data(null);
                transactionRepository.save(transaction);
                break;
            default:
                throw new IllegalArgumentException("Invalid choice for order status");
        }

        return responseBuilder.build();

    }

    @Override
    public int countTransaction(String orderNo) {
        return transactionRepository.countByOrderNo(orderNo);
    }
}
