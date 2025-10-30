package com.ringme.service.selfcare;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.enums.sys.CommonStatus;
import com.ringme.enums.sys.ListAjaxType;
import com.ringme.model.selfcare.SelfcareSoapWS;
import com.ringme.repository.selfcare.SelfcareSoapWSRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class SelfcareSoapService {
    @Autowired
    SelfcareSoapWSRepository repository;

    public List<AjaxSearchDto> ajaxSearch(String input) {
        return Helper.listAjax(repository.ajaxSearch(Helper.processStringSearch(input)), ListAjaxType.TEXT.getType());
    }

    public Page<SelfcareSoapWS> search(Integer pageNo, Integer pageSize, String name, CommonStatus status) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.search(pageable, Helper.processStringSearch(name), status != null ? status.getType() : null);
    }

    public SelfcareSoapWS findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void save(SelfcareSoapWS object) {
        try {
            repository.save(object);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }
    }
}
