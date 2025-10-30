package com.ringme.model.selfcare;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "natcash_payment_mobile_service_vas_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NatcashPaymentMobileServiceVasHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String isdn;

    @Column(name = "package_code", length = 25)
    private String packageCode;

    @Column
    private Double money;

    @Column(name = "error_code", length = 5, nullable = false)
    private String errorCode = "0";

    @Column(name = "order_number", length = 100, nullable = false)
    private String orderNumber;

    @Column(name = "user_msg", length = 255)
    private String userMsg;

    @Column(name = "created_at")
    private Date createdAt;
}
