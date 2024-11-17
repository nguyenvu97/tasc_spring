package org.tasc.tasc_spring.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tasc.tasc_spring.user_service.model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("select s from Store as s where s.user_id = user_id ")
    Store findByUser_id(@Param(value = "user_id") String id);
}
