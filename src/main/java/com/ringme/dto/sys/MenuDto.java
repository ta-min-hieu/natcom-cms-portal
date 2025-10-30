package com.ringme.dto.sys;

import com.ringme.model.sys.Icon;
import com.ringme.model.sys.Menu;
import com.ringme.model.sys.Router;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor

public class MenuDto {

    private Long id;

    private String name;

    private Router router;

    private Integer order_num;

    private Menu parentName;

    private List<MenuDto> lstChildMenus;

    private Icon icon;

}
