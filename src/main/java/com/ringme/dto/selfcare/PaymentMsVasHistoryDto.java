package com.ringme.dto.selfcare;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentMsVasHistoryDto {
    private Long id;
    private String isdn;
    private String serviceCode;
    private String url;
    private String request;
    private String response;
    private Integer status;
    private Date createdAt;

    private Date createdAtStart;
    private Date createdAtEnd;
}
