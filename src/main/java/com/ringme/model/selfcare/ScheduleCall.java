package com.ringme.model.selfcare;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "schedule_call")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "support_type", nullable = false, length = 50)
    private String supportType;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(length = 20)
    private String isdn;

    @Column(length = 5)
    private String language;

    private Integer status;

    private String note;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "process_date")
    private Date processDate;

    @Column(name = "success_date")
    private Date successDate;

    @Column(name = "cancel_date")
    private Date cancelDate;

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
}
