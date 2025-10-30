package com.ringme.dto.selfcare;

import com.ringme.enums.selfcare.FtthRegisterStatus;
import lombok.Data;

import java.util.Date;

@Data
public class FtthRegisterExportDto {
    private Long id;
    private String orderCode;
    private String packageCode;
    private String packageName;
    private String name;
    private String isdner;
    private String isdnee;
    private String emailee;
    private String proviceId;
    private String proviceName;
    private String districtId;
    private String districtName;
    private String commune;
    private String note;
    private String branchName;
    private String status;

    private Integer callNumber;
    private String reasonCanNotContactSuccess;
    private String customerFeedback;
    private String remarkAdditionInfo;
    private Date createdAt;

    public void setStatus(Integer status) {
        if(status == null)
            this.status = "";

        else {
            switch (status) {
                case 0 -> this.status = FtthRegisterStatus.NEW.name();
                case 1 -> this.status = FtthRegisterStatus.CALLING.name();
                case 2 -> this.status = FtthRegisterStatus.UNREACHABLE.name();
                case 3 -> this.status = FtthRegisterStatus.SURVEYING.name();
                case 4 -> this.status = FtthRegisterStatus.SIGN_CONTRACT.name();
                case 5 -> this.status = FtthRegisterStatus.DEPLOYING.name();
                case 6 -> this.status = FtthRegisterStatus.SUCCESS.name();
                case 7 -> this.status = FtthRegisterStatus.CANCEL.name();
            }
        }
    }
}
