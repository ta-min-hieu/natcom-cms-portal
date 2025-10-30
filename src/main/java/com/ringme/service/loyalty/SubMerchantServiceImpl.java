package com.ringme.service.loyalty;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.loyalty.SubMerchantDto;
import com.ringme.enums.sys.ListAjaxType;
import com.ringme.model.voucher.SubMerchant;
import com.ringme.repository.voucher.SubMerchantRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public final class SubMerchantServiceImpl implements SubMerchantService {
    @Autowired
    SubMerchantRepository repository;

    @Override
    public List<AjaxSearchDto> ajaxSearch(String input, String idMerchant) {
        return Helper.listAjax(repository.ajaxSearch(Helper.processStringSearch(input), Helper.processStringSearch(idMerchant)), ListAjaxType.TEXT.getType());
    }

    @Override
    public Page<SubMerchant> search(Integer pageNo, Integer pageSize, String title, Integer status) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        SubMerchantDto dto = searchInputHandler(title, status);

        return repository.search(pageable, dto.getName(), dto.getStatus());
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
    public SubMerchant findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void save(SubMerchant object) {
        try {
            repository.save(object);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }
    }

    private SubMerchantDto searchInputHandler(String name, Integer status) {
        SubMerchantDto dto = new SubMerchantDto();

        dto.setName(name);
        dto.setStatus(status);

        return dto;
    }
}
