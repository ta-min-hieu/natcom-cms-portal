package com.ringme.dto.selfcare;

import lombok.Data;

import java.util.Date;

@Data
public class NatcashPaymentMobileServiceVasHistoryDto {
    private Long id;
    private String isdn;
    private String packageCode;
    private Double money;
    private String errorCode = "0";
    private String orderNumber;
    private String userMsg;
    private Date createdAt;

    private Date createdAtStart;
    private Date createdAtEnd;
}
