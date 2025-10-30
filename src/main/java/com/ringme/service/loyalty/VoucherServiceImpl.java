package com.ringme.service.loyalty;

import com.ringme.common.Helper;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.loyalty.VoucherDto;
import com.ringme.enums.sys.ListAjaxType;
import com.ringme.model.voucher.Voucher;
import com.ringme.repository.voucher.VoucherRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public final class VoucherServiceImpl implements VoucherService {
    @Autowired
    VoucherRepository repository;

    @Override
    public Page<Voucher> search(Integer pageNo, Integer pageSize, String isdn, String voucherGroupName, Long topicId, Long merchantId) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        VoucherDto dto = searchInputHandler(isdn, voucherGroupName, topicId, merchantId);

        return repository.search(pageable, dto.getIsdn(), dto.getVoucherGroupName(), dto.getTopicId(), dto.getIdMerchant());
    }

    @Override
    public void updateUsedStatus(long id) {
        repository.updateUsedStatus(id);
    }

    private VoucherDto searchInputHandler(String isdn, String voucherGroupName, Long topicId, Long merchantId) {
        VoucherDto dto = new VoucherDto();

        dto.setIsdn(Helper.processStringSearch(isdn));
        dto.setVoucherGroupName(Helper.processStringSearch(voucherGroupName));
        dto.setTopicId(topicId);
        dto.setIdMerchant(merchantId);

        return dto;
    }
}
