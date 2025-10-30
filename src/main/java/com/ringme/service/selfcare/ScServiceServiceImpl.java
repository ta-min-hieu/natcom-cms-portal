package com.ringme.service.selfcare;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.selfcare.SelfcareServiceDto;
import com.ringme.enums.sys.CommonStatus;
import com.ringme.enums.sys.ListAjaxType;
import com.ringme.model.selfcare.SelfcareService;
import com.ringme.repository.selfcare.SelfcareServiceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public final class ScServiceServiceImpl implements ScServiceService {
    @Autowired
    SelfcareServiceRepository repository;

    @Override
    public List<AjaxSearchDto> ajaxSearch(String input) {
        return Helper.listAjax(repository.ajaxSearch(Helper.processStringSearch(input)), ListAjaxType.TEXT.getType());
    }

    @Override
    public Page<SelfcareService> search(Integer pageNo, Integer pageSize, String name, Long serviceCategory, CommonStatus status) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        SelfcareServiceDto dto = searchInputHandler(name, serviceCategory, status);

        return repository.search(pageable, dto.getName(), dto.getServiceCategoryId(), dto.getStatus());
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
    public SelfcareService findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void save(SelfcareService object) {
        try {
            repository.save(object);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }
    }

    private SelfcareServiceDto searchInputHandler(String name, Long serviceCategory, CommonStatus status) {
        SelfcareServiceDto dto = new SelfcareServiceDto();

        dto.setName(Helper.processStringSearch(name));
        dto.setServiceCategoryId(serviceCategory);
        dto.setStatus(status == null ? null : status.getType());

        return dto;
    }
}
