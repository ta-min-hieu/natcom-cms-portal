package com.ringme.service.loyalty;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.loyalty.MerchantDto;
import com.ringme.enums.sys.ListAjaxType;
import com.ringme.model.voucher.Merchant;
import com.ringme.repository.voucher.MerchantRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public final class MerchantServiceImpl implements MerchantService {
    @Autowired
    MerchantRepository repository;

    @Override
    public List<AjaxSearchDto> ajaxSearch(String input) {
        return Helper.listAjax(repository.ajaxSearch(Helper.processStringSearch(input)), ListAjaxType.TEXT.getType());
    }

    @Override
    public Page<Merchant> search(Integer pageNo, Integer pageSize, String title, Integer status) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        MerchantDto dto = searchInputHandler(title, status);

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
    public Merchant findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void save(Merchant object) {
        try {
            repository.save(object);
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage(), e);
        }
    }

    private MerchantDto searchInputHandler(String name, Integer status) {
        MerchantDto dto = new MerchantDto();

        dto.setName(name);
        dto.setStatus(status);

        return dto;
    }
}
