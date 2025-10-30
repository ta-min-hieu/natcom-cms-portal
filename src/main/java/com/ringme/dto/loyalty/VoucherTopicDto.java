package com.ringme.dto.loyalty;

import lombok.Data;

@Data
public class VoucherTopicDto {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
    private Integer status;
}
