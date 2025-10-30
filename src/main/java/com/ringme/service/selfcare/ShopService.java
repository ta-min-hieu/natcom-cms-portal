package com.ringme.service.selfcare;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.mynatcom.Shop;
import org.springframework.data.domain.Page;

import java.util.List;

public sealed interface ShopService permits ShopServiceImpl {
    Page<Shop> search (Integer pageNo, Integer pageSize);

    Shop findById (String id);

    List<AjaxSearchDto> ajaxSearch(String input);
}
