package org.tasc.tasc_spring.user_service.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2Repository extends JpaRepository<Oauth2,Integer> {
    Oauth2 findByUserId(String id);



//    Oauth2 findByUserIdAndEmail(String id, String sub);
}
