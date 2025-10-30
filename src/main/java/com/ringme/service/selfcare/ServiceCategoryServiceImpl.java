package com.ringme.service.selfcare;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.selfcare.ServiceCategoryDto;
import com.ringme.enums.sys.CommonStatus;
import com.ringme.enums.sys.ListAjaxType;
import com.ringme.model.selfcare.ServiceCategory;
import com.ringme.repository.selfcare.ServiceCategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public final class ServiceCategoryServiceImpl implements ServiceCategoryService {
    @Autowired
    ServiceCategoryRepository repository;

    @Override
    public List<AjaxSearchDto> ajaxSearch(String input) {
        return Helper.listAjax(repository.ajaxSearch(Helper.processStringSearch(input), null), ListAjaxType.TEXT.getType());
    }

    @Override
    public List<AjaxSearchDto> ajaxSearchNoChild(String input) {
        return Helper.listAjax(repository.ajaxSearch(Helper.processStringSearch(input), true), ListAjaxType.TEXT.getType());
    }

    @Override
    public Page<ServiceCategory> search(Integer pageNo, Integer pageSize, String name, Integer categoryLevel, CommonStatus status) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        ServiceCategoryDto dto = searchInputHandler(name, categoryLevel, status);

        return repository.search(pageable, dto.getName(), dto.getCategoryLevel(), dto.getStatus());
    }

    @Override
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }


    @Override
    public ServiceCategory findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void save(ServiceCategory object) {
        try {
            repository.save(object);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }
    }

    private ServiceCategoryDto searchInputHandler(String name, Integer categoryLevel, CommonStatus status) {
        ServiceCategoryDto dto = new ServiceCategoryDto();

        dto.setName(Helper.processStringSearch(name));
        dto.setCategoryLevel(categoryLevel);
        dto.setStatus(status == null ? null : status.getType());

        return dto;
    }
}
