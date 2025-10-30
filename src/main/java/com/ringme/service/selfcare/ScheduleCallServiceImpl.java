package com.ringme.service.selfcare;

import com.ringme.common.Helper;
import com.ringme.dto.selfcare.ScheduleCallDto;
import com.ringme.enums.selfcare.ScheduleCallStatus;
import com.ringme.enums.selfcare.ScheduleCallSupportType;
import com.ringme.model.selfcare.ScheduleCall;
import com.ringme.repository.selfcare.ScheduleCallRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public final class ScheduleCallServiceImpl implements ScheduleCallService {
    @Autowired
    ScheduleCallRepository repository;

    @Override
    public Page<ScheduleCall> search(Integer pageNo, Integer pageSize, ScheduleCallSupportType supportType, String isdn, ScheduleCallStatus status, String dateRanger) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        ScheduleCallDto dto = searchInputHandler(supportType, isdn, status, dateRanger);

        return repository.search(pageable, dto.getSupportType(), dto.getIsdn(), dto.getStatus(), dto.getCreatedAtStart(), dto.getCreatedAtEnd());
    }

    @Override
    public void updateProcessStatus(long id) {
        repository.updateProcessStatus(id);
    }

    @Override
    public void updateSuccessStatus(long id, String note) {
        repository.updateSuccessStatus(id, note);
    }

    @Override
    public void updateCancelStatus(long id, String note) {
        repository.updateCancelStatus(id, note);
    }

    private ScheduleCallDto searchInputHandler(ScheduleCallSupportType supportType, String isdn, ScheduleCallStatus status, String dateRanger) {
        ScheduleCallDto dto = new ScheduleCallDto();

        dto.setSupportType(supportType != null ? supportType.getType() : null);
        dto.setIsdn(Helper.processStringSearch(isdn));
        dto.setStatus(status != null ? status.getType() : null);

        String[] dateRanges = Helper.reportDate(dateRanger);
        dto.setCreatedAtStart(Helper.convertDateV2(dateRanges[0]));
        dto.setCreatedAtEnd(Helper.convertDateV2(dateRanges[1]));

        return dto;
    }
}
