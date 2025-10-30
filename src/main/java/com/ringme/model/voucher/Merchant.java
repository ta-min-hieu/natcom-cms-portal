package com.ringme.model.voucher;

import com.ringme.model.sys.EntityBaseV2;
import com.ringme.model.sys.User;
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
@Table(name = "merchant")
@EntityListeners(AuditingEntityListener.class)
public class Merchant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "owner_name")
    private String ownerName;
    @Column(name = "owner_phonenumber")
    private String ownerPhoneNumber;
    @Column(name = "status")
    private Integer status;
    @Column(name = "note")
    private String note;
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "merchant")
    private List<VoucherGroup> voucherGroups = new ArrayList<>();
    @OneToMany(mappedBy = "merchant")
    private List<SubMerchant> subMerchants = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
