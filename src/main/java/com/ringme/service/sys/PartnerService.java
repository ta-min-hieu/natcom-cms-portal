package com.ringme.service.sys;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.sys.PartnerDto;
import com.ringme.model.sys.Partner;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PartnerService {
    Page<Partner> get(PartnerDto dto, int pageNo, int pageSize);
    Partner findById(Integer id);
    PartnerDto processSearch(String name);
    void delete(int id);
    void save(Partner object);
    List<AjaxSearchDto> ajaxSearch(String input);

    boolean isPartner(int partnerId);
}