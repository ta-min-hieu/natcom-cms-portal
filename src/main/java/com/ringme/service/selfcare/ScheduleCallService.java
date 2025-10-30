package com.ringme.service.selfcare;

import com.ringme.enums.selfcare.ScheduleCallStatus;
import com.ringme.enums.selfcare.ScheduleCallSupportType;
import com.ringme.model.selfcare.ScheduleCall;
import org.springframework.data.domain.Page;

public sealed interface ScheduleCallService permits ScheduleCallServiceImpl {
    Page<ScheduleCall> search(Integer pageNo, Integer pageSize, ScheduleCallSupportType supportType, String isdn, ScheduleCallStatus status, String dateRanger);

    void updateProcessStatus(long id);

    void updateSuccessStatus(long id, String note);

    void updateCancelStatus(long id, String note);
}
