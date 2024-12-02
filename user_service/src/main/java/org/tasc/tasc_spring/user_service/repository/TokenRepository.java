package org.tasc.tasc_spring.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tasc.tasc_spring.user_service.model.Token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {

    @Query(value = "SELECT t FROM Token t JOIN t.user u WHERE u.user_id = :id AND (t.expired = false OR t.revoked = false)")
    List<Token> findAllValidTokenByUser(@Param(value = "id") UUID id);

    @Query("select t from Token as t where t.user.user_id = :user_id")
    Optional<Token> findByToken(@Param(value = "user_id") UUID userId);
}
