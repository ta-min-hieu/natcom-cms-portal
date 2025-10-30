package com.ringme.dto.sys;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PartnerDto {
    private Integer id;
    private String name;
    private String code;
    private String description;
    private String linkDomain;
    private String token;
    private String avatar;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private List<UserUpdateDto> users;
    private String address;
    private String email;
    private String contact;
    private String director;
    private String callbackUrl;
}
