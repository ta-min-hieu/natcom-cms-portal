package com.ringme.service.selfcare;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.enums.sys.ListAjaxType;
import com.ringme.model.mynatcom.Shop;
import com.ringme.repository.mynatcom.ShopRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public final class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopRepository repository;

    @Override
    public Page<Shop> search(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.search(pageable);
    }

    @Override
    public Shop findById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<AjaxSearchDto> ajaxSearch(String input) {
        return Helper.listAjax(repository.ajaxSearch(Helper.processStringSearch(input)), ListAjaxType.TEXT.getType());
    }
}
