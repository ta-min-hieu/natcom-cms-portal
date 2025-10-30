package com.ringme.service.selfcare;

import com.ringme.dto.record.Page;
import com.ringme.dto.selfcare.NatcashPaymentMobileServiceVasHistoryDto;

public interface NatcashHistoryService {
    Page<NatcashPaymentMobileServiceVasHistoryDto> search(Integer pageNo, Integer pageSize, String isdn, String packageCode, String errorCode, String orderNumber, String dateRanger);
}
