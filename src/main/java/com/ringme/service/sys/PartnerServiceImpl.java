package com.ringme.service.sys;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.sys.PartnerDto;
import com.ringme.model.sys.Partner;
import com.ringme.repository.sys.PartnerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@Transactional
public class PartnerServiceImpl implements PartnerService {
    @Autowired
    PartnerRepository repository;
    @Lazy
    @Autowired
    UserService userService;

    @Override
    public Page<Partner> get(PartnerDto dto, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.get(dto.getName(), pageable);
    }
    @Override
    public Partner findById(Integer id) {
        return repository.findById(id).orElse(null);
    }
    @Override
    public PartnerDto processSearch(String name) {
        PartnerDto dto = new PartnerDto();
        dto.setName(Helper.processStringSearch(name));
        return dto;
    }

    @Override
    public void delete(int id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    @Override
    public void save(Partner object) {
        try {
            repository.save(object);
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }


    @Override
    public List<AjaxSearchDto> ajaxSearch(String input) {
        return Helper.listAjax(repository.ajaxSearch(PageRequest.of(0, 20), Helper.processStringSearch(input)), 1);
    }

    @Override
    public boolean isPartner(int partnerId) {
        Partner partner = findById(userService.getPartnerId());
        return partner != null && partner.getStatus() == 1;
    }
}
