package com.ringme.service.selfcare;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.enums.sys.CommonStatus;
import com.ringme.model.selfcare.SelfcareService;
import org.springframework.data.domain.Page;

import java.util.List;

public sealed interface ScServiceService permits ScServiceServiceImpl {
    List<AjaxSearchDto> ajaxSearch(String input);

    Page<SelfcareService> search (Integer pageNo, Integer pageSize, String name, Long serviceCategory, CommonStatus status);

    void delete (Long id);

    SelfcareService findById (Long id);

    void save (SelfcareService obj);
}
