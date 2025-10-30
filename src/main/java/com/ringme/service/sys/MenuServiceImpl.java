package com.ringme.service.sys;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.sys.Menu;
import com.ringme.repository.sys.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepository menuRepository;

    @Override
    public void saveMenu(Menu menu) {
        menuRepository.save(menu);
    }

    @Override
    public Optional<Menu> findMenuById(Long id) {

        return menuRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public Page<Menu> findMenuPage(Long parentName, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("parent_name_id").ascending()
                .and(Sort.by("order_num").ascending()));
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return menuRepository.getAll(parentName, pageable);
    }

    @Override
    public List<Menu> getListMenuNoParent() {
        List<Menu> list = menuRepository.findByParentNameIsNullOrderByOrderNumAsc();
        list.sort(Comparator.comparing(Menu::getOrderNum));
        return list;
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public List<AjaxSearchDto> ajaxSearch(String input) {
        return Helper.listAjax(menuRepository.ajaxSearch(Helper.processStringSearch(input)), 1);
    }

//    @Override
//    public Map<String, List<Menu>> getMapMenuParent() {
//        List<Menu> menus = menuRepository.findAll();
//        Map<String, List<Menu>> map = menus.stream().filter(e -> !e.getParentName().equals("")).collect(Collectors.groupingBy(Menu::getParentName));
//        Map<String, List<Menu>> sortedMap = map.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByValue((o1, o2) -> {
//                    int size1 = o1.size();
//                    int size2 = o2.size();
//                    for (int i = 0; i < size1 && i < size2; i++) {
//                        int compare = Integer.compare(o1.get(i).getOrder_num(), o2.get(i).getOrder_num());
//                        if (compare != 0) {
//                            return compare;
//                        }
//                    }
//                    return Integer.compare(size1, size2);
//                }))
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (oldValue, newValue) -> oldValue,
//                        LinkedHashMap::new));
//        return sortedMap;
//    }

//    @Override
//    public Optional<Menu> findMenuByName(String name) {
//        return menuRepository.findMenuByName(name);
//    }
}
