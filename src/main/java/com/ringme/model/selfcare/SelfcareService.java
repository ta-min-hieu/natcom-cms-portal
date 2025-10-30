package com.ringme.model.selfcare;

import com.ringme.model.sys.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Date;

@Entity
@Table(name = "sc_service")
@Data
public class SelfcareService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "name_lang2")
    private String nameHT;
    @Column(name = "status")
    private Integer status;
    @Column(name = "description")
    private String description;
    @Column(name = "description_lang2")
    private String descriptionHT;
    @Column(name = "thumb_1_1_url")
    private String thumb11;
    @Column(name = "thumb_16_9_url")
    private String thumb169;
    @Column(name = "service_category_id")
    private Long serviceCategoryId;
    @Column(name = "short_des")
    private String shortDes;
    @Column(name = "short_des_ht")
    private String shortDesHT;

    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "service_category_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private ServiceCategory serviceCategory;

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
}
