package com.ringme.model.sys;


import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "tbl_icon")
@EntityListeners(AuditingEntityListener.class)
public class Icon implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(name = "display_name")
    private String displayName;
}
