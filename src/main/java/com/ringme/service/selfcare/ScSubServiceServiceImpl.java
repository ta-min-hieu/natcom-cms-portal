package com.ringme.service.selfcare;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.selfcare.ScSubServiceDto;
import com.ringme.enums.sys.CommonStatus;
import com.ringme.enums.sys.ListAjaxType;
import com.ringme.model.selfcare.ScSubService;
import com.ringme.repository.selfcare.ScSubServiceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public final class ScSubServiceServiceImpl implements ScSubServiceService {
    @Autowired
    ScSubServiceRepository repository;

    @Override
    public Page<ScSubService> search(Integer pageNo, Integer pageSize, String name, String code, CommonStatus status, Long serviceId) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        ScSubServiceDto dto = searchInputHandler(name, code, status, serviceId);

        return repository.search(pageable, dto.getName(), dto.getCode(), dto.getStatus(), dto.getServiceId());
    }

    @Override
    public List<ScSubService> getList(long serviceId) {
        return repository.getList(serviceId);
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
    public ScSubService findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void save(ScSubService object) {
        try {
            repository.save(object);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }
    }

    private ScSubServiceDto searchInputHandler(String name, String code, CommonStatus status, Long serviceId) {
        ScSubServiceDto dto = new ScSubServiceDto();

        dto.setName(Helper.processStringSearch(name));
        dto.setCode(Helper.processStringSearch(code));
        dto.setStatus(status == null ? null : status.getType());
        dto.setServiceId(serviceId);

        return dto;
    }
}
