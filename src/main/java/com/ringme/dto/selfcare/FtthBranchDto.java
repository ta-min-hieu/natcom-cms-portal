package com.ringme.dto.selfcare;

import lombok.*;

import java.util.Date;

@Data
public class FtthBranchDto {
    private Long id;
    private String name;
    private Date createdAt;
    private Date updatedAt;
}
