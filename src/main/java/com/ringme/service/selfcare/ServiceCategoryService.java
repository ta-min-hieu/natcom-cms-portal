package com.ringme.service.selfcare;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.enums.sys.CommonStatus;
import com.ringme.model.selfcare.ServiceCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public sealed interface ServiceCategoryService permits ServiceCategoryServiceImpl {
    List<AjaxSearchDto> ajaxSearch(String input);

    List<AjaxSearchDto> ajaxSearchNoChild(String input);

    Page<ServiceCategory> search (Integer pageNo, Integer pageSize, String name, Integer categoryLevel, CommonStatus status);

    void delete (Long id);

    ServiceCategory findById (Long id);

    void save (ServiceCategory obj);
}
