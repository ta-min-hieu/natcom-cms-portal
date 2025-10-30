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
@Table(name = "sub_merchant")
@EntityListeners(AuditingEntityListener.class)
public class SubMerchant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_merchant")
    private Long idMerchant;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "owner_name")
    private String ownerName;
    @Column(name = "owner_phonenumber")
    private String ownerPhoneNumber;
    @Column(name = "status")
    private Integer status;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany(mappedBy = "subMerchants")
    private List<VoucherGroup> voucherGroups = new ArrayList<>();
    @OneToMany(mappedBy = "subMerchant")
    private List<VoucherGroupSubMerchant> voucherGroupSubMerchants = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "id_merchant", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Merchant merchant;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
