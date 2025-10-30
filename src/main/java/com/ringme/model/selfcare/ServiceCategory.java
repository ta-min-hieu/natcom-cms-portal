package com.ringme.model.selfcare;

import com.ringme.model.sys.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sc_service_category")
@Data
public class ServiceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private Integer status;
    @Column(name = "description")
    private String description;
    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "category_level")
    private int categoryLevel;
    @Column(name = "has_child")
    private int hasChild;

    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private List<ServiceCategory> children;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private ServiceCategory parentCategory;

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
