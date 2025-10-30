package com.ringme.service.selfcare;

import com.ringme.common.ExportExcel;
import com.ringme.common.Helper;
import com.ringme.config.AppConfig;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.selfcare.FtthRegisterDto;
import com.ringme.dto.selfcare.FtthRegisterExportDto;
import com.ringme.enums.selfcare.FtthRegisterStatus;
import com.ringme.enums.sys.ListAjaxType;
import com.ringme.model.selfcare.FtthRegister;
import com.ringme.repository.selfcare.FtthBranchRepository;
import com.ringme.repository.selfcare.FtthRegisterRepository;
import com.ringme.repository.selfcare.ProvinceRepository;
import com.ringme.service.sys.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class FtthServiceImpl implements FtthService {
    @Autowired
    FtthBranchRepository branchRepository;
    @Autowired
    FtthRegisterRepository registerRepository;
    @Autowired
    ProvinceRepository provinceRepository;
    @Autowired
    UserService userService;
    @Autowired
    ExportExcel export;
    @Autowired
    AppConfig appConfig;
    @Autowired
    LocaleFactory localeFactory;

    @Override
    public Page<FtthRegister> search(Integer pageNo, Integer pageSize, String orderCode, String packageCode, String isdner, String isdnee, String name, String proviceId, Long branchId, FtthRegisterStatus status, String dateRanger) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Long branchIdRole = userService.getBranchId();
        if(branchIdRole != null)
            branchId = branchIdRole;

        FtthRegisterDto dto = searchInputHandler(orderCode, packageCode, isdner, isdnee, name, proviceId, branchId, status, dateRanger);

        return registerRepository.search(pageable, dto.getBranchId(), dto.getOrderCode(), dto.getPackageCode(), dto.getIsdner(), dto.getIsdnee(), dto.getName(), dto.getProviceId(), dto.getStatus(), dto.getCreatedAtStart(), dto.getCreatedAtEnd());
    }

    @Override
    public FtthRegister findById(long id) {
        return registerRepository.findById(id).orElse(null);
    }

    @Override
    public List<AjaxSearchDto> ajaxSearchFtthBranch(String input) {
        return Helper.listAjax(branchRepository.ajaxSearch(Helper.processStringSearch(input)), ListAjaxType.TEXT.getType());
    }

    @Override
    public List<AjaxSearchDto> ajaxSearchProvince(String input) {
        return Helper.listAjax(provinceRepository.ajaxSearch(Helper.processStringSearch(input)), ListAjaxType.TEXT.getType());
    }

    @Override
    public void updateCallingStatus(long id) {
        registerRepository.updateCallingStatus(id);
    }

    @Override
    public void updateCallSuccessStatus(long id, String customerFeedback) {
        registerRepository.updateCallSuccessStatus(id, customerFeedback);
    }

    @Override
    public void updateCallFailStatus(long id, String reasonCanNotContactSuccess) {
        registerRepository.updateCallFailStatus(id, "\n - " + reasonCanNotContactSuccess);

        FtthRegister ftthRegister = registerRepository.findById(id).orElse(null);
        if(ftthRegister != null && ftthRegister.getCallNumber() >= 3)
            registerRepository.updateCancelStatus(id, "Call more than 3 times");
    }

    @Override
    public void updateDeployStatus(long id) {
        registerRepository.updateDeployStatus(id);
        FtthRegister ftthRegister = registerRepository.findById(id).orElse(null);
        if(ftthRegister == null || ftthRegister.getStatus() != 5)
            return;
        callApiSMS(ftthRegister.getIsdnee(), localeFactory.getMessage("sms.ftth.signed").replace("{{CODE}}", ftthRegister.getOrderCode()));
    }

    @Override
    public void updateSignContractStatus(long id) {
        registerRepository.updateSignContractStatus(id);
    }

    @Override
    public void updateSuccessStatus(long id, String remarkAdditionInfo) {
        registerRepository.updateSuccessStatus(id, remarkAdditionInfo);
        FtthRegister ftthRegister = registerRepository.findById(id).orElse(null);
        if(ftthRegister == null || ftthRegister.getStatus() != 6)
            return;
        callApiSMS(ftthRegister.getIsdnee(), localeFactory.getMessage("sms.ftth.done").replace("{{CODE}}", ftthRegister.getOrderCode()));
    }

    @Override
    public void updateCancelStatus(long id, String remarkAdditionInfo) {
        registerRepository.updateCancelStatus(id, remarkAdditionInfo);

        FtthRegister ftthRegister = registerRepository.findById(id).orElse(null);
        if(ftthRegister == null || ftthRegister.getStatus() != 7)
            return;
        callApiSMS(ftthRegister.getIsdnee(), localeFactory.getMessage("sms.ftth.cancel").replace("{{CODE}}", ftthRegister.getOrderCode()).replace("{{Reason information}}", remarkAdditionInfo));
    }

    @Override
    public void updateBranch(long id, long branchId) {
        registerRepository.updateBranch(id, branchId);
    }

    private FtthRegisterDto searchInputHandler(String orderCode, String packageCode, String isdner, String isdnee, String name, String proviceId, Long branchId, FtthRegisterStatus status, String dateRanger) {
        FtthRegisterDto dto = new FtthRegisterDto();

        dto.setOrderCode(Helper.processStringSearch(orderCode));
        dto.setPackageCode(Helper.processStringSearch(packageCode));
        dto.setIsdner(Helper.processStringSearch(isdner));
        dto.setIsdnee(Helper.processStringSearch(isdnee));
        dto.setName(Helper.processStringSearch(name));
        dto.setProviceId(Helper.processStringSearch(proviceId));
        dto.setBranchId(branchId);
        dto.setStatus(status != null ? status.getType() : null);

        String[] dateRanges = Helper.reportDate(dateRanger);
        dto.setCreatedAtStart(Helper.convertDateV2(dateRanges[0]));
        dto.setCreatedAtEnd(Helper.convertDateV2(dateRanges[1]));

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public void export(HttpServletResponse response, String orderCode, String packageCode, String isdner, String isdnee, String name, String proviceId, Long branchId, FtthRegisterStatus status, String dateRanger) {

        String[] headers = {"id", "Order Code", "Package Code", "Package Name", "Name", "Isdner",
                "Isdnee", "Email", "Provice Id", "Provice Name", "District Id", "District Name", "Commune", "Note",
                "Branch", "status", "Call Number", "Reason can not contact success", "Customer feedback",
                "Remark Addition Info", "Created At"};

        Long branchIdRole = userService.getBranchId();
        if(branchIdRole != null)
            branchId = branchIdRole;

        FtthRegisterDto dto = searchInputHandler(orderCode, packageCode, isdner, isdnee, name, proviceId, branchId, status, dateRanger);

        List<FtthRegister> list = registerRepository.getList(dto.getBranchId(), dto.getOrderCode(), dto.getPackageCode(), dto.getIsdner(), dto.getIsdnee(), dto.getName(), dto.getProviceId(), dto.getStatus(), dto.getStartDate(), dto.getEndDate());

        List<FtthRegisterExportDto> listExport = convert(list);

        export.export(listExport, headers, response);
    }

    private List<FtthRegisterExportDto> convert(List<FtthRegister> list) {
        List<FtthRegisterExportDto> dtos = new ArrayList<>();
        for (FtthRegister entity : list) {
            FtthRegisterExportDto dto = new FtthRegisterExportDto();
            dto.setId(entity.getId());
            dto.setOrderCode(entity.getOrderCode());
            dto.setPackageCode(entity.getPackageCode());
            dto.setPackageName(entity.getPackageName());
            dto.setName(entity.getName());
            dto.setIsdner(entity.getIsdner());
            dto.setIsdnee(entity.getIsdnee());
            dto.setEmailee(entity.getEmailee());
            dto.setProviceId(entity.getProviceId());
            dto.setProviceName(entity.getProviceName());
            dto.setDistrictId(entity.getDistrictId());
            dto.setDistrictName(entity.getDistrictName());
            dto.setCommune(entity.getCommune());
            dto.setNote(entity.getNote());
            dto.setStatus(entity.getStatus());
            dto.setBranchName(entity.getFtthBranch() != null ? entity.getFtthBranch().getName() : "");
            dto.setCreatedAt(entity.getCreatedAt());
            dto.setCallNumber(entity.getCallNumber());
            dto.setReasonCanNotContactSuccess(entity.getReasonCanNotContactSuccess());
            dto.setCustomerFeedback(entity.getCustomerFeedback());
            dto.setRemarkAdditionInfo(entity.getRemarkAdditionInfo());

            dtos.add(dto);
        }

        return dtos;
    }

    private void callApiSMS(String isdn, String content) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = appConfig.getApiBaseUrl() + "/without-bearer/test-sms";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("msisdn", isdn);
            body.add("content", content);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            log.info("Response: " + response.getBody());
        } catch (Exception e) {
            log.error("isdn: {}, content: {}, error: {}", isdn, content, e.getMessage(), e);
        }
    }
}
