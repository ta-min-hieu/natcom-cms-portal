package com.ringme.model.selfcare;

import com.ringme.model.sys.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Date;

@Entity
@Table(name = "sc_sub_service")
@Data
public class ScSubService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private Integer status;
    @Column(name = "description")
    private String description;
    @Column(name = "code")
    private String code;
    @Column(name = "price")
    private Integer price = 0;
//    @Column(name = "unit")
//    private String unit = "HTG";
    @Column(name = "register_by")
    private Integer registedBy = 1;
    @Column(name = "allow_prepaid")
    private Integer allowPrepaid;
    @Column(name = "allow_postpaid")
    private Integer allowPostpaid;
    @Column(name = "allow_3gsim")
    private Integer allow3gsim;
    @Column(name = "allow_4gsim")
    private Integer allow4gsim;
    @Column(name = "allow_5gsim")
    private Integer allow5gsim;
    @Column(name = "is_gift")
    private Integer isGift;
    @Column(name = "is_auto_renew")
    private Integer isAutoRenew;
    @Column(name = "dk_soap_tkg")
    private Integer dkSoapTkg;
    @Column(name = "dk_soap_vi")
    private Integer dkSoapVi;
    @Column(name = "cancel_soap")
    private Integer cancelSoap;
    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private SelfcareService selfcareService;

    @ManyToOne
    @JoinColumn(name = "dk_soap_tkg", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private SelfcareSoapWS dkSoapTkgWS;

    @ManyToOne
    @JoinColumn(name = "dk_soap_vi", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private SelfcareSoapWS dkSoapViWS;

    @ManyToOne
    @JoinColumn(name = "cancel_soap", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private SelfcareSoapWS cancelSoapWS;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private User userCreatedBy;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private User userUpdatedBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @Transient
    private String atView;
}
