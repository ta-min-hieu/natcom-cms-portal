package com.ringme.dto.selfcare;

import lombok.Data;

@Data
public class ScSubServiceDto {
    private Long id;
    private String name;
    private Integer status;
    private String description;
    private String code;
    private Integer price = 0;
//    private String unit = "HTG";
    private Integer registedBy = 1;
    private Integer allowPrepaid;
    private Integer allowPostpaid;
    private Integer allow3gsim;
    private Integer allow4gsim;
    private Integer allow5gsim;
    private Integer isGift;
    private Integer isAutoRenew;
    private Integer dkSoapTkg;
    private Integer dkSoapVi;
    private Integer cancelSoap;
    private Long serviceId;
    private String atView;
}
