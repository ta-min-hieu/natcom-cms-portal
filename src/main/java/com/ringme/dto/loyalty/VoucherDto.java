package com.ringme.dto.loyalty;

import lombok.Data;

import java.util.Date;

@Data
public class VoucherDto {
    private Long id;
    private String code;
    private Long idVoucherGroup;
    private String voucherGroupName;
    private Integer status;
    private String isdn;
    private Date usedDate;
    private Long topicId;
    private String topicName;
    private Long idMerchant;
    private String merchantName;
    private Integer maxPoint;
    private String discountUnit;
    private String pointUnit;
    private Double discountAmount;
    private String description;
    private String imageUrl;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private Date updatedAt;
}
