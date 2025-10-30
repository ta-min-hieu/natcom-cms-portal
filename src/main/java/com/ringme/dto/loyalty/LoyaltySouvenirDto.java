package com.ringme.dto.loyalty;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltySouvenirDto {
    private Long id;
    private String title;
    private Integer status; // 1: active, 0: huy
    private String iconUrl;
    private Integer point;
    private String unit;
    private String description;
    private String startDate;
    private String dateExpired;
    private Date dateExpiredStart;
    private Date dateExpiredEnd;
    private int quantityTotal;
    private int quantityReal;
    private int quantityExchanged;
    private Date createdAt;
    private Date updatedAt;
}
