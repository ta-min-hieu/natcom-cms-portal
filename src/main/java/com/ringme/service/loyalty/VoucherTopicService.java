package com.ringme.service.loyalty;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.voucher.VoucherTopic;
import org.springframework.data.domain.Page;

import java.util.List;

public sealed interface VoucherTopicService permits VoucherTopicServiceImpl {
    List<AjaxSearchDto> ajaxSearch(String input);

    Page<VoucherTopic> search(Integer pageNo, Integer pageSize, String title, Integer status);

    void delete (Long id);

    VoucherTopic findById (Long id);

    void save (VoucherTopic obj);
}
