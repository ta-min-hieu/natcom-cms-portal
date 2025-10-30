package com.ringme.dto.selfcare;

import lombok.Data;

@Data
public class SelfcareServiceDto {
    private Long id;
    private String name;
    private String nameHT;
    private Integer status;
    private String description;
    private String descriptionHT;
    private String thumb11;
    private String thumb169;
    private Long serviceCategoryId;
    private String shortDes;
    private String shortDesHT;
}
