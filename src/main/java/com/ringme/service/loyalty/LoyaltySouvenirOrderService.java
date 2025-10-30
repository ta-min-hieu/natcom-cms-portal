package com.ringme.service.loyalty;

import com.ringme.enums.loyalty.SouvenirOrderStatus;
import com.ringme.model.loyalty.LoyaltySouvenirOrder;
import org.springframework.data.domain.Page;

public sealed interface LoyaltySouvenirOrderService permits LoyaltySouvenirOrderServiceImpl {
    Page<LoyaltySouvenirOrder> search (Integer pageNo, Integer pageSize, String isdn, String orderCode, String name, SouvenirOrderStatus status);

    LoyaltySouvenirOrder findById (Long id);

    void updateProcessingStatus(long id);

    void updateValidStatus(long id);

    void updateReceivedStatus(long id);

    void updateCancelStatus(long id);
}
