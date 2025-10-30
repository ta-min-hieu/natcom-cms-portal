package com.ringme.model.sys;

import com.ringme.dto.sys.MenuDto;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@Log4j2
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tbl_menu")
public class Menu extends EntityBase implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_vn")
    @NotNull(message = "Không được để trống tên menu")
    private String nameVn;
    @Column(name = "name_en")
    @NotNull(message = "Không được để trống tên menu")
    private String nameEn;
    @OneToOne
    @JoinColumn(name = "router_id")
    private Router router;
    @NotNull(message = "Không được để trống vị trí")
    @Column(name = "order_num")
    private Integer orderNum;
    @ManyToOne
    @JoinColumn(name = "parent_name_id")
    private Menu parentName;

    @ManyToOne
    @JoinColumn(name = "icon_id")
    private Icon icon;

    public MenuDto convertToDto(String locate){
        MenuDto menuDto = new MenuDto();
        if (locate.equals("vn")){
            menuDto.setId(this.id);
            menuDto.setOrder_num(this.orderNum);
            menuDto.setRouter(this.router);
            menuDto.setName(this.nameVn);
            menuDto.setIcon(this.icon);
        }
        else {
            menuDto.setId(this.id);
            menuDto.setOrder_num(this.orderNum);
            menuDto.setRouter(this.router);
            menuDto.setName(this.nameEn);
            menuDto.setIcon(this.icon);
        }
        return menuDto;
    }

    public Long getParentId() {
        if(parentName == null)
            return null;
        return parentName.getId();
    }
}
