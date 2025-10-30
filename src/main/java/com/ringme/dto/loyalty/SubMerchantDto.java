package com.ringme.dto.loyalty;

import lombok.Data;

@Data
public class SubMerchantDto {
    private Long id;
    private Long idMerchant;
    private String name;
    private String address;
    private String ownerName;
    private String ownerPhoneNumber;
    private Integer status;
}
