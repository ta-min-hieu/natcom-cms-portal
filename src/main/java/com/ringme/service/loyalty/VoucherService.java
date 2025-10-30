package com.ringme.service.loyalty;

import com.ringme.model.voucher.Voucher;
import org.springframework.data.domain.Page;

public sealed interface VoucherService permits VoucherServiceImpl {
    Page<Voucher> search(Integer pageNo, Integer pageSize, String isdn, String voucherGroupName, Long topicId, Long merchantId);

    void updateUsedStatus(long id);
}
