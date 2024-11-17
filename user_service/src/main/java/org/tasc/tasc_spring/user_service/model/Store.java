package org.tasc.tasc_spring.user_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.tasc.tasc_spring.user_service.model.status.StoreStatus;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long store_id;
    @Column(nullable = false,unique = true)
    public String store_email;
    @Column(nullable = false,unique = true)
    public String store_phone;
    @Column(nullable = false)
    public String store_address;
    @Column(nullable = false)
    public StoreStatus store_status;
    @Column(nullable = false,updatable = false)
    public String user_id;

}
