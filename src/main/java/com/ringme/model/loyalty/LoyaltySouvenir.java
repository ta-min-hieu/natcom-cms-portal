package com.ringme.model.loyalty;

import com.ringme.enums.loyalty.SouvenirStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

@Entity
@Table(name = "loyalty_souvenir")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class LoyaltySouvenir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "int default 0")
    private Integer status; // 1: active, 0: huy

    @Column(name = "icon_url", length = 512)
    private String iconUrl;

    private Integer point;

    @Column(length = 8)
    private String unit;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "date_expired")
    private Date dateExpired;

    @Column(name = "quantity_total", columnDefinition = "int default 0")
    private int quantityTotal;

    @Column(name = "quantity_real", columnDefinition = "int default 0")
    private int quantityReal;

    @Column(name = "quantity_exchanged", columnDefinition = "int default 0")
    private int quantityExchanged;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public SouvenirStatus getStatusEnum() {
        if (status == null) return null;

        return switch (status) {
            case 1 -> SouvenirStatus.ACTIVE;
            case 0 -> SouvenirStatus.INACTIVE;
            default -> null;
        };
    }
}
