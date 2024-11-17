package org.tasc.tasc_spring.user_service.service;

import org.tasc.tasc_spring.api_common.model.ResponseData;
import org.tasc.tasc_spring.user_service.model.Store;

public interface StoreService {
    ResponseData addStore(Store store,String token);
    ResponseData deleteStore(String token);
    ResponseData updateStore(Store store,String token);


}
