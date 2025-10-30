package com.ringme.service.loyalty;

import com.ringme.common.Helper;
import com.ringme.enums.loyalty.SouvenirOrderStatus;
import com.ringme.model.loyalty.LoyaltySouvenirOrder;
import com.ringme.repository.loyalty.LoyaltySouvenirOrderRepository;
import com.ringme.repository.loyalty.LoyaltySouvenirRepository;
import com.ringme.service.sys.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public final class LoyaltySouvenirOrderServiceImpl implements LoyaltySouvenirOrderService {
    @Autowired
    LoyaltySouvenirOrderRepository repository;
    @Autowired
    LoyaltySouvenirRepository souvenirRepository;
    @Autowired
    UserService userService;

    @Override
    public Page<LoyaltySouvenirOrder> search(Integer pageNo, Integer pageSize, String isdn, String orderCode, String name, SouvenirOrderStatus status) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.search(pageable, userService.getShowroomId(), Helper.processStringSearch(isdn), Helper.processStringSearch(orderCode), Helper.processStringSearch(name), status == null ? null : status.getType());
    }

    @Override
    public LoyaltySouvenirOrder findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void updateProcessingStatus(long id) {
        repository.updateProcessingStatus(id);
    }

    @Override
    public void updateValidStatus(long id) {
        repository.updateValidStatus(id);
    }

    @Override
    public void updateReceivedStatus(long id) {
        repository.updateReceivedStatus(id);
        LoyaltySouvenirOrder souvenirOrder = repository.findById(id).orElse(null);
        if(souvenirOrder != null)
            souvenirRepository.updateNumberOfQuantityReal(souvenirOrder.getSouvenirId());
    }

    @Override
    public void updateCancelStatus(long id) {
        repository.updateCancelStatus(id);
    }
}
