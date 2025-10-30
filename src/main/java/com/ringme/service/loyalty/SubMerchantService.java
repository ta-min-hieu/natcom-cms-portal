package com.ringme.service.loyalty;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.voucher.SubMerchant;
import org.springframework.data.domain.Page;

import java.util.List;

public sealed interface SubMerchantService permits SubMerchantServiceImpl {
    List<AjaxSearchDto> ajaxSearch(String input, String idMerchant);

    Page<SubMerchant> search (Integer pageNo, Integer pageSize, String title, Integer status);

    void delete (Long id);

    SubMerchant findById (Long id);

    void save (SubMerchant obj);
}
