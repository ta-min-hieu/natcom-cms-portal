package com.ringme.model.sys;

import com.ringme.model.mynatcom.Shop;
import com.ringme.model.selfcare.FtthBranch;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tbl_user")
public class User extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "fullname")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "type")
    private String type;    // 0 : admin , 1 : user

    @Column(name = "active")
    private boolean active;

    @Column(name = "showroom_id")
    private String showroomId;

    @Column(name = "branch_id")
    private Long branchId;

    @ManyToOne
    @JoinColumn(name = "partner_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Partner partner;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private FtthBranch ftthBranch;
}
