package com.ringme.dto.selfcare;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleCallDto {
    private Long id;
    private String supportType;
    private Date startDate;
    private Date endDate;
    private String isdn;
    private String language;
    private Integer status;
    private String note;
    private Date createdAt;
    private Date createdAtStart;
    private Date createdAtEnd;
    private String startDateStr;
    private String endDateStr;
}
