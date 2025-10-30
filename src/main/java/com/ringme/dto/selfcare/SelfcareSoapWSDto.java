package com.ringme.dto.selfcare;

import lombok.Data;

@Data
public class SelfcareSoapWSDto {
    private Long id;
    private String name;
    private String description;
    private String url;
    private String xmlBody;
    private Integer status;
}
