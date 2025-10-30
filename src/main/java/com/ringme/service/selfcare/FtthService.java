package com.ringme.service.selfcare;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.enums.selfcare.FtthRegisterStatus;
import com.ringme.model.selfcare.FtthRegister;
import org.springframework.data.domain.Page;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface FtthService {
    Page<FtthRegister> search(Integer pageNo, Integer pageSize, String orderCode, String packageCode, String isdner, String isdnee, String name, String proviceId, Long branchId, FtthRegisterStatus status, String dateRanger);

    FtthRegister findById(long id);

    List<AjaxSearchDto> ajaxSearchFtthBranch(String input);

    List<AjaxSearchDto> ajaxSearchProvince(String input);

    void updateCallingStatus(long id);

    void updateCallSuccessStatus(long id, String customerFeedback);

    void updateCallFailStatus(long id, String reasonCanNotContactSuccess);

    void updateDeployStatus(long id);

    void updateSignContractStatus(long id);

    void updateSuccessStatus(long id, String remarkAdditionInfo);

    void updateCancelStatus(long id, String remarkAdditionInfo);

    void updateBranch(long id, long branchId);

    void export(HttpServletResponse response, String orderCode, String packageCode, String isdner, String isdnee, String name, String proviceId, Long branchId, FtthRegisterStatus status, String dateRanger);
}
