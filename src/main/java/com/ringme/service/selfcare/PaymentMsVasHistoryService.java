package com.ringme.service.selfcare;

import com.ringme.model.selfcare.PaymentMsVasHistory;
import org.springframework.data.domain.Page;

public interface PaymentMsVasHistoryService {
    Page<PaymentMsVasHistory> search(int pageNo, int pageSize, String isdn, String serviceCode, Integer status, String url, String dateRanger);
}
