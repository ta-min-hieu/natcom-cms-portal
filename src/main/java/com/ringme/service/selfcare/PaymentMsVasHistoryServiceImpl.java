package com.ringme.service.selfcare;

import com.ringme.common.Helper;
import com.ringme.dto.selfcare.PaymentMsVasHistoryDto;
import com.ringme.model.selfcare.PaymentMsVasHistory;
import com.ringme.repository.selfcare.PaymentMsVasHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentMsVasHistoryServiceImpl implements PaymentMsVasHistoryService {
    @Autowired
    PaymentMsVasHistoryRepository repository;

    @Override
    public Page<PaymentMsVasHistory> search(int pageNo, int pageSize, String isdn, String serviceCode, Integer status, String url, String dateRanger) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        PaymentMsVasHistoryDto dto = searchInputHandler(isdn, serviceCode, status, url, dateRanger);

        return repository.search(pageable, dto.getIsdn(), dto.getServiceCode(), dto.getStatus(), dto.getUrl(), dto.getCreatedAtStart(), dto.getCreatedAtEnd());
    }
    
    private PaymentMsVasHistoryDto searchInputHandler(String isdn, String serviceCode, Integer status, String url, String dateRanger) {
        PaymentMsVasHistoryDto dto = new PaymentMsVasHistoryDto();

        dto.setIsdn(Helper.processStringSearch(isdn));
        dto.setServiceCode(Helper.processStringSearch(serviceCode));
        dto.setUrl(Helper.processStringSearch(url));
        dto.setStatus(status);

        String[] dateRanges = Helper.reportDate(dateRanger);
        dto.setCreatedAtStart(Helper.convertDateV2(dateRanges[0]));
        dto.setCreatedAtEnd(Helper.convertDateV2(dateRanges[1]));

        return dto;
    }
}
