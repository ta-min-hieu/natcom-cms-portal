package com.ringme.model.selfcare;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Date;

@Entity
@Table(name = "ftth_register")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FtthRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", length = 8)
    private String orderCode;

    @Column(name = "package_code", length = 50)
    private String packageCode;

    @Column(name = "package_name", length = 100)
    private String packageName;

    @Column(length = 100)
    private String name;

    @Column(length = 20)
    private String isdner;

    @Column(length = 20)
    private String isdnee;

    @Column(length = 100)
    private String emailee;

    @Column(name = "provice_id", length = 20)
    private String proviceId;

    @Column(name = "provice_name", length = 100)
    private String proviceName;

    @Column(name = "district_id", length = 20)
    private String districtId;

    @Column(name = "district_name", length = 100)
    private String districtName;

    @Column(length = 100)
    private String commune;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(columnDefinition = "int default 0")
    private Integer status;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "call_date")
    private Date callDate;

    @Column(name = "call_fail_date")
    private Date callFailDate;

    @Column(name = "survey_date")
    private Date surveyDate;

    @Column(name = "deploy_date")
    private Date deployDate;

    @Column(name = "sign_contract_date")
    private Date signContractDate;

    @Column(name = "success_date")
    private Date successDate;

    @Column(name = "cancel_date")
    private Date cancelDate;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "call_number")
    private Integer callNumber;

    @Column(name = "reason_can_not_contact_success")
    private String reasonCanNotContactSuccess;

    @Column(name = "customer_feedback")
    private String customerFeedback;

    @Column(name = "remark_addition_info")
    private String remarkAdditionInfo;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private FtthBranch ftthBranch;

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
