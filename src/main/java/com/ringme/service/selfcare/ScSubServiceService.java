package com.ringme.service.selfcare;

import com.ringme.enums.sys.CommonStatus;
import com.ringme.model.selfcare.ScSubService;
import org.springframework.data.domain.Page;

import java.util.List;

public sealed interface ScSubServiceService permits ScSubServiceServiceImpl {
    Page<ScSubService> search (Integer pageNo, Integer pageSize, String name, String code, CommonStatus status, Long serviceId);

    List<ScSubService> getList(long serviceId);

    void delete (Long id);

    ScSubService findById (Long id);

    void save (ScSubService obj);
}
