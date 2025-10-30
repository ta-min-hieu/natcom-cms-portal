package com.ringme.dto.selfcare;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShopDto {
    private String id;
    private String name;
    private String addr;
    private String openTime;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Integer provinceId;
    private Integer districtId;
    private String isdn;
    private Byte type = 0;
    private Byte status = 1;
    private Date createdTime;
    private String createdBy;
    private Date lastUpdatedTime;
    private String lastUpdatedBy;
    private Integer la;
    private Integer lo;
    private Byte shopOrder;
}
