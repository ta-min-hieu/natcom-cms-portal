package com.ringme.dto.loyalty;

import lombok.Data;

@Data
public class MerchantDto {
    private Long id;
    private String name;
    private String ownerName;
    private String ownerPhoneNumber;
    private Integer status;
    private String note;
}
