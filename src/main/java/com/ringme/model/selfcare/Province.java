package com.ringme.model.selfcare;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "province")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Province {
    @Id
    private String id;

    @Column
    private String name;
}
