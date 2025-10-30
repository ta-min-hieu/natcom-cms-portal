package com.ringme.service.selfcare;

import com.ringme.common.Helper;
import com.ringme.dao.NatcashHistoryDao;
import com.ringme.dto.record.Page;
import com.ringme.dto.selfcare.NatcashPaymentMobileServiceVasHistoryDto;
import com.ringme.repository.selfcare.NatcashPaymentMobileServiceVasHistoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class NatcashHistoryServiceImpl implements NatcashHistoryService {
    @Autowired
    NatcashPaymentMobileServiceVasHistoryRepository natcashPaymentMobileServiceVasHistoryRepository;
    @Autowired
    NatcashHistoryDao dao;

    @Override
    public Page<NatcashPaymentMobileServiceVasHistoryDto> search(Integer pageNo, Integer pageSize, String isdn, String packageCode, String errorCode, String orderNumber, String dateRanger) {
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        NatcashPaymentMobileServiceVasHistoryDto dto = searchInputHandler(isdn, packageCode, errorCode, orderNumber, dateRanger);

//        return natcashPaymentMobileServiceVasHistoryRepository.search(pageable, dto.getIsdn(), dto.getPackageCode(), dto.getErrorCode(), dto.getOrderNumber(), dto.getCreatedAtStart(), dto.getCreatedAtEnd());

        List<NatcashPaymentMobileServiceVasHistoryDto> list = dao.getNatcashPaymentHistory(pageNo, pageSize, dto.getIsdn(), dto.getPackageCode(), dto.getOrderNumber(), dto.getErrorCode(), dto.getCreatedAtStart(), dto.getCreatedAtEnd());
        int totalRecord = dao.countNatcashPaymentHistory(dto.getIsdn(), dto.getPackageCode(), dto.getOrderNumber(), dto.getErrorCode(), dto.getCreatedAtStart(), dto.getCreatedAtEnd());

        return new Page<>(list, totalRecord, (totalRecord + pageSize - 1) / pageSize);
    }

    private NatcashPaymentMobileServiceVasHistoryDto searchInputHandler(String isdn, String packageCode, String errorCode, String orderNumber, String dateRanger) {
        NatcashPaymentMobileServiceVasHistoryDto dto = new NatcashPaymentMobileServiceVasHistoryDto();

        dto.setIsdn(Helper.processStringSearch(isdn));
        dto.setPackageCode(Helper.processStringSearch(packageCode));
        dto.setErrorCode(Helper.processStringSearch(errorCode));
        dto.setOrderNumber(Helper.processStringSearch(orderNumber));

        String[] dateRanges = Helper.reportDate(dateRanger);
        dto.setCreatedAtStart(Helper.convertDateV2(dateRanges[0]));
        dto.setCreatedAtEnd(Helper.convertDateV2(dateRanges[1]));

        return dto;
    }
}
