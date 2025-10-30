package com.ringme.model.sys;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tbl_partner")
public class Partner implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code; // bo tren giao dien
    @Column(name = "token")
    private String token;
    @Column(name = "address")
    private String address; //new
    @Column(name = "email")
    private String email; //new
    @Column(name = "contact")
    private String contact; //new
    @Column(name = "director")
    private String director; //new
    @Column(name = "status")
    private int status; // -1: delete, 0: not active, 1: active
    @Column(name = "webhook_url")
    private String callbackUrl; //new
    @Column(name = "description")
    private String description;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;
    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;

//    @OneToMany(mappedBy = "partner")
//    private List<User> users;


}
