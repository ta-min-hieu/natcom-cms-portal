package com.ringme.model.voucher;

import com.ringme.model.sys.EntityBaseV2;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "voucher_group")
@EntityListeners(AuditingEntityListener.class)
public class VoucherGroup implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "topic_id")
    private Long topicId;
    @Column(name = "id_merchant")
    private Long idMerchant;
    @Column(name = "status")
    private Integer status; // 0 - Lock, 1 - Active
    @Column(name = "name")
    private String name;
    @Column(name = "quantity_total")
    private Integer quantityTotal;
    @Column(name = "quantity_exchanged") // số lượng đã đổi
    private Integer quantityExchanged = 0;
    @Column(name = "max_point")
    private Integer point;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "description")
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "discount_amount")
    private Double amount;
    @Column(name = "discount_unit")
    private String discountUnit;
    @Column(name = "point_unit")
    private String pointUnit;
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private VoucherTopic topic;

    @ManyToOne
    @JoinColumn(name = "id_merchant", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Merchant merchant;

    @ManyToMany
    @JoinTable(
            name = "voucher_group_sub_merchant",
            joinColumns = @JoinColumn(name = "id_voucher_group"),
            inverseJoinColumns = @JoinColumn(name = "id_sub_merchant")
    )
    private List<SubMerchant> subMerchants = new ArrayList<>();
    @OneToMany(mappedBy = "voucherGroup")
    private List<VoucherGroupSubMerchant> voucherGroupSubMerchants = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
