package com.ringme.model.voucher;

import com.ringme.model.sys.EntityBaseV2;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "voucher_group_sub_merchant")
@EntityListeners(AuditingEntityListener.class)
public class VoucherGroupSubMerchant extends EntityBaseV2 implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_voucher_group")
    private Long idVoucherGroup;
    @Column(name = "id_sub_merchant")
    private Long idSubMerchant;

    @ManyToOne
    @JoinColumn(name = "id_voucher_group", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private VoucherGroup voucherGroup;
    @ManyToOne
    @JoinColumn(name = "id_sub_merchant", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private SubMerchant subMerchant;
}
