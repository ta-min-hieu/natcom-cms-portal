package com.ringme.service.loyalty;

import com.ringme.dto.loyalty.VoucherGroupDto;
import com.ringme.model.voucher.VoucherGroup;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public sealed interface VoucherGroupService permits VoucherGroupServiceImpl {
    Page<VoucherGroup> search (Integer pageNo, Integer pageSize, String title, Integer status, String dateExpireds);

    void delete (Long id);

    VoucherGroup findById (Long id);

    void save (VoucherGroup obj);

    int saveHandler(RedirectAttributes redirectAttributes, VoucherGroupDto dto, String thumbUpload1);
}
