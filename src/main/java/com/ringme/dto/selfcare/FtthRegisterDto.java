package com.ringme.dto.selfcare;

import lombok.*;

import java.util.Date;

@Data
public class FtthRegisterDto {
    private Long id;
    private String orderCode;
    private String packageCode;
    private String packageName;
    private String name;
    private String isdner;
    private String isdnee;
    private String emailee;
    private String proviceId;
    private Long branchId;
    private String proviceName;
    private String districtId;
    private String districtName;
    private String commune;
    private String note;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;

    private String startDate;
    private String endDate;

    private Date createdAtStart;
    private Date createdAtEnd;

//    public FtthRegisterStatus getStatusEnum() {
//        if (status == null) return null;
//
//        return switch (status) {
//            case 1 -> FtthRegisterStatus.IN_PROCESS;
//            case 2 -> FtthRegisterStatus.SUCCESS;
//            case 3 -> FtthRegisterStatus.CANCEL;
//            default -> null;
//        };
//    }
}
