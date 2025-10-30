package com.ringme.model.voucher;

import com.ringme.model.sys.EntityBaseV2;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "voucher")
@EntityListeners(AuditingEntityListener.class)
public class Voucher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String code;

    @Column(name = "id_voucher_group")
    private Long idVoucherGroup;

    @Column(name = "voucher_group_name", length = 100)
    private String voucherGroupName;

    /**
     * 0 - Exchanged, 1 - Used
     */
    @Column
    private Integer status;

    @Column(length = 50)
    private String isdn;

    @Column(name = "used_date")
    private Date usedDate;

    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "topic_name", length = 100)
    private String topicName;

    @Column(name = "id_merchant")
    private Long idMerchant;

    @Column(name = "merchant_name", length = 100)
    private String merchantName;

    @Column(name = "max_point")
    private Integer maxPoint;

    @Column(name = "discount_unit", length = 5)
    private String discountUnit;

    @Column(name = "point_unit", length = 8)
    private String pointUnit;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(length = 255)
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
