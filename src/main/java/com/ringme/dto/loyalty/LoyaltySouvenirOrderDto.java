package com.ringme.dto.loyalty;

import com.ringme.enums.loyalty.SouvenirOrderStatus;
import lombok.*;

import java.util.Date;

@Data
@Builder
public class LoyaltySouvenirOrderDto {
    private Long id;
    private String showroomId;
    private String showroomName;
    private String orderCode;
    private String isdn;
    private SouvenirOrderStatus status; // 1: pending, 2: processing, 3: valid, 4: received, 5: cancel
    private Date startDate;
    private Date dateExpired;
    private String iconUrl;
    private String description;
    private String title;
    private Integer point;
    private String unit;
    private Integer provinceId;
    private String provinceName;
    private Integer districtId;
    private String districtName;
    private String address;
    private Date receiveDate;
    private Date processDate;
    private Date confirmDate;
    private Date cancelDate;
    private Date createdAt;
    private Date updatedAt;
}
