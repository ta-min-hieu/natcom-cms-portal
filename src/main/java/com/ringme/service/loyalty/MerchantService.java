package com.ringme.service.loyalty;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.voucher.Merchant;
import org.springframework.data.domain.Page;

import java.util.List;

public sealed interface MerchantService permits MerchantServiceImpl {
    List<AjaxSearchDto> ajaxSearch(String input);

    Page<Merchant> search(Integer pageNo, Integer pageSize, String title, Integer status);

    void delete (Long id);

    Merchant findById (Long id);

    void save (Merchant obj);
}
