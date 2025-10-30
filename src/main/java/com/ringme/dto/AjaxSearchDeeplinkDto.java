package com.ringme.dto;

import lombok.Data;

@Data
public class AjaxSearchDeeplinkDto {
    private String id;
    private String text;
    private String serviceId;
    private String deeplink;
    private String deeplinkParams;
}
