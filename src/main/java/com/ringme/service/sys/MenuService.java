package com.ringme.service.sys;


import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.sys.Menu;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    void saveMenu(Menu menu);

    Optional<Menu> findMenuById(Long id);

    void deleteById(Long id);

    Page<Menu> findMenuPage(Long parentName, int pageNo, int pageSize);

    List<Menu> getListMenuNoParent();

    List<Menu> findAll();

    List<AjaxSearchDto> ajaxSearch(String input);

//    Map<String, List<Menu>> getMapMenuParent();

//    Optional<Menu> findMenuByName(String name);
}
