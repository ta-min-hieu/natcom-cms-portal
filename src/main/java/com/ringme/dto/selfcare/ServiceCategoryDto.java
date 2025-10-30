package com.ringme.dto.selfcare;

import lombok.Data;

@Data
public class ServiceCategoryDto {
    private Long id;
    private String name;
    private Integer status;
    private String description;
    private Long parentId;
    private Integer categoryLevel;
    private int hasChild;
}
