package com.ringme.model.loyalty;

import com.ringme.enums.loyalty.SouvenirOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "loyalty_souvenir_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltySouvenirOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "souvenir_id")
    private Integer souvenirId;

    @Column(name = "showroom_id", length = 32)
    private String showroomId;

    @Column(name = "showroom_name", length = 255)
    private String showroomName;

    @Column(name = "order_code", length = 100, nullable = false)
    private String orderCode;

    @Column(length = 20, nullable = false)
    private String isdn;

    @Column(nullable = false, columnDefinition = "TINYINT(5) DEFAULT 1")
    private Integer status; // 1: pending, 2: processing, 3: valid, 4: received, 5: cancel

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "date_expired")
    private Date dateExpired;

    @Column(name = "icon_url", length = 512)
    private String iconUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer point;

    @Column(length = 50, nullable = false, columnDefinition = "varchar(50) default 'points'")
    private String unit;

    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "province_name", length = 100)
    private String provinceName;

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "district_name", length = 100)
    private String districtName;

    @Column(length = 255)
    private String address;

    @Column(name = "receive_date")
    private Date receiveDate;

    @Column(name = "process_date")
    private Date processDate;

    @Column(name = "confirm_date")
    private Date confirmDate;

    @Column(name = "cancel_date")
    private Date cancelDate;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public SouvenirOrderStatus getStatusEnum() {
        if (status == null) return null;

        return switch (status) {
            case 1 -> SouvenirOrderStatus.PENDING;
            case 2 -> SouvenirOrderStatus.PROCESSING;
            case 3 -> SouvenirOrderStatus.VALID;
            case 4 -> SouvenirOrderStatus.RECEIVED;
            case 5 -> SouvenirOrderStatus.CANCEL;
            default -> null;
        };
    }
}
