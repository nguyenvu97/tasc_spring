package org.tasc.tasc_spring.user_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tasc.tasc_spring.api_common.ex.EntityNotFound;
import org.tasc.tasc_spring.api_common.model.CustomerDto;
import org.tasc.tasc_spring.api_common.model.ResponseData;
import org.tasc.tasc_spring.user_service.model.Store;
import org.tasc.tasc_spring.user_service.model.status.StoreStatus;
import org.tasc.tasc_spring.user_service.repository.StoreRepository;
import org.tasc.tasc_spring.user_service.service.AuthenticationService;
import org.tasc.tasc_spring.user_service.service.StoreService;

import static org.tasc.tasc_spring.api_common.javaUtils.DecodeToken.decodeToken;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;
    private final StoreRepository storeRepository;
    @Override
    public ResponseData addStore(Store store, String token) {
      ResponseData responseData =  authenticationService.decode_token(token);
      CustomerDto customerDto = decodeToken(responseData,objectMapper);
        if (customerDto == null) {
            throw  new EntityNotFound("customer not found", 404);
        }

        return ResponseData
                .builder()
                .message("ok")
                .status_code(200)
                .data(storeRepository.save(Store
                        .builder()
                        .store_address(store.getStore_address())
                        .store_email(store.getStore_email())
                        .store_phone(store.getStore_phone())
                        .user_id(customerDto.getId())
                        .store_status(StoreStatus.OPEN)
                        .build()))
                .build();
    }
    @Override
    @Transactional
    public ResponseData deleteStore(String token) {
        if (token == null){
            throw new EntityNotFound("token not found", 404);
        }
        ResponseData responseData =  authenticationService.decode_token(token);
        CustomerDto customerDto = decodeToken(responseData,objectMapper);
        if (customerDto == null) {
            throw  new EntityNotFound("customer not found", 404);
        }
        Store store = storeRepository.findByUser_id(customerDto.getId());
        if (store == null || store.getStore_status() == StoreStatus.CLOSED) {
            throw  new EntityNotFound("store not found", 404);
        }
        store.setStore_status(StoreStatus.CLOSED);
        return ResponseData
                .builder()
                .status_code(200)
                .data("ok")
                .message("delete Ok")
                .build();
    }

    @Override
    @Transactional
    public ResponseData updateStore(Store store, String token) {
        ResponseData responseData =  authenticationService.decode_token(token);
        CustomerDto customerDto = decodeToken(responseData,objectMapper);
        if (customerDto == null) {
            throw  new EntityNotFound("customer not found", 404);
        }
        

        return ResponseData
                .builder()
                .message("ok")
                .status_code(200)
                .data("Ok")
                .build();
    }

}
