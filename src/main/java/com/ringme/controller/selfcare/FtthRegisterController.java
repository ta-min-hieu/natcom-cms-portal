package com.ringme.controller.selfcare;

import com.ringme.common.ExportExcel;
import com.ringme.common.Helper;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.enums.selfcare.FtthRegisterStatus;
import com.ringme.model.selfcare.FtthRegister;
import com.ringme.repository.selfcare.FtthBranchRepository;
import com.ringme.repository.selfcare.ProvinceRepository;
import com.ringme.service.selfcare.FtthService;
import com.ringme.service.sys.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/selfcare/ftth-register")
public class FtthRegisterController {
    @Autowired
    FtthService service;
    @Autowired
    ProvinceRepository provinceRepository;
    @Autowired
    FtthBranchRepository ftthBranchRepository;
    @Autowired
    UserService userService;
    @Autowired
    LocaleFactory localeFactory;
    @Autowired
    ExportExcel excel;

    @GetMapping("/ajax-search-branch")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearchBranch(@RequestParam(name = "input", required = false) String input) {
        log.info("input: {}", input);
        return new ResponseEntity<>(service.ajaxSearchFtthBranch(input), HttpStatus.OK);
    }

    @GetMapping("/ajax-search-province")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearchProvince(@RequestParam(name = "input", required = false) String input) {
        log.info("input: {}", input);
        return new ResponseEntity<>(service.ajaxSearchProvince(input), HttpStatus.OK);
    }

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "orderCode", required = false, defaultValue = "") String orderCode,
                             @RequestParam(name = "packageCode", required = false, defaultValue = "") String packageCode,
                             @RequestParam(name = "isdner", required = false, defaultValue = "") String isdner,
                             @RequestParam(name = "isdnee", required = false, defaultValue = "") String isdnee,
                             @RequestParam(name = "name", required = false, defaultValue = "") String name,
                             @RequestParam(name = "proviceId", required = false, defaultValue = "") String proviceId,
                             @RequestParam(name = "branchIdSS", required = false, defaultValue = "") Long branchId,
                             @RequestParam(name = "dateRanger", required = false, defaultValue = "") String dateRanger,
                             @RequestParam(name = "status", required = false, defaultValue = "") FtthRegisterStatus status,
                             Model model) {
        if(page == null)
            page = 1;

        Page<FtthRegister> objectPage = service.search(page, pageSize, orderCode, packageCode, isdner, isdnee, name, proviceId, branchId, status, dateRanger);

        model.addAttribute("title", localeFactory.getMessage("title.list.LoyaltySouvenirOrder"));
        model.addAttribute("orderCode", orderCode);
        model.addAttribute("packageCode", packageCode);
        model.addAttribute("isdner", isdner);
        model.addAttribute("isdnee", isdnee);
        model.addAttribute("name", name);

        model.addAttribute("branchIdRole", userService.getBranchId());

        model.addAttribute("proviceId", proviceId);
        if(proviceId != null && !proviceId.isEmpty())
            model.addAttribute("province", provinceRepository.findById(Helper.processStringSearch(proviceId)).orElse(null));

        model.addAttribute("branchId", branchId);
        if(branchId != null)
            model.addAttribute("branch", ftthBranchRepository.findById(branchId).orElse(null));

        model.addAttribute("dateRanger", dateRanger);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "ftth/index";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, ModelMap model) throws Exception {
        log.info("id: {}", id);
        try {
            model.put("model", service.findById(id));
            return "ftth/detail :: detail";
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw new Exception(e);
        }
    }

    @GetMapping("/update-branch")
    public String updateBranch(@RequestParam("id") long id,
                               @RequestParam("branchId") long branchId,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(name = "orderCodeS", required = false, defaultValue = "") String orderCodeS,
                               @RequestParam(name = "packageCodeS", required = false, defaultValue = "") String packageCodeS,
                               @RequestParam(name = "isdnerS", required = false, defaultValue = "") String isdnerS,
                               @RequestParam(name = "isdneeS", required = false, defaultValue = "") String isdneeS,
                               @RequestParam(name = "nameS", required = false, defaultValue = "") String nameS,
                               @RequestParam(name = "proviceIdS", required = false, defaultValue = "") String proviceIdS,
                               @RequestParam(name = "branchIdS", required = false, defaultValue = "") Long branchIdS,
                               @RequestParam(name = "dateRangerS", required = false, defaultValue = "") String dateRangerS,
                               @RequestParam(name = "statusS", required = false, defaultValue = "") FtthRegisterStatus statusS,
                               RedirectAttributes redirectAttributes) throws Exception {
        log.info("branchId: {}", branchId);
        try {
            service.updateBranch(id, branchId);
            service.updateCallingStatus(id);

            redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-update-branch.success"));

            return "redirect:/selfcare/ftth-register/index/" + page + "?pageSize=" + pageSize + "&orderCode=" + orderCodeS + "&packageCode=" + packageCodeS + "&isdner=" + isdnerS + "&isdnee=" + isdneeS + "&name=" + nameS + "&proviceId=" + proviceIdS + "&branchId=" + branchIdS + "&dateRanger=" + dateRangerS + "&status=" + (statusS == null ? "" : statusS.getType());
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw new Exception(e);
        }
    }

    @GetMapping(value = {"/update-status", "/update-status/{page}"})
    public String updateProcessStatus(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "id") long id,
            @RequestParam(name = "status") FtthRegisterStatus status,
            @RequestParam(required = false) String customerFeedback,
            @RequestParam(required = false) String reasonCanNotContactSuccess,
            @RequestParam(required = false) String remarkAdditionInfo,
            @RequestParam(name = "orderCodeS", required = false, defaultValue = "") String orderCodeS,
            @RequestParam(name = "packageCodeS", required = false, defaultValue = "") String packageCodeS,
            @RequestParam(name = "isdnerS", required = false, defaultValue = "") String isdnerS,
            @RequestParam(name = "isdneeS", required = false, defaultValue = "") String isdneeS,
            @RequestParam(name = "nameS", required = false, defaultValue = "") String nameS,
            @RequestParam(name = "proviceIdS", required = false, defaultValue = "") String proviceIdS,
            @RequestParam(name = "branchIdS", required = false, defaultValue = "") String branchIdS,
            @RequestParam(name = "dateRangerS", required = false, defaultValue = "") String dateRangerS,
            @RequestParam(name = "statusS", required = false, defaultValue = "") FtthRegisterStatus statusS,
            RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;

        switch (status) {
            case FtthRegisterStatus.CALLING -> {
                service.updateCallingStatus(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-call-status.success"));
            }
            case FtthRegisterStatus.UNREACHABLE -> {
                service.updateCallFailStatus(id, reasonCanNotContactSuccess);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-unreachable-status.success"));
            }
            case FtthRegisterStatus.SURVEYING -> {
                service.updateCallSuccessStatus(id, customerFeedback);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-survey-status.success"));
            }
            case FtthRegisterStatus.SIGN_CONTRACT -> {
                service.updateSignContractStatus(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-sign-contract-status.success"));
            }
            case FtthRegisterStatus.DEPLOYING -> {
                service.updateDeployStatus(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-deploy-status.success"));
            }
            case FtthRegisterStatus.SUCCESS -> {
                service.updateSuccessStatus(id, remarkAdditionInfo);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-success-status.success"));
            }
            case FtthRegisterStatus.CANCEL -> {
                service.updateCancelStatus(id, remarkAdditionInfo);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-cancel-status.success"));
            }
        }
        return "redirect:/selfcare/ftth-register/index/" + page + "?pageSize=" + pageSize + "&orderCode=" + orderCodeS + "&packageCode=" + packageCodeS + "&isdner=" + isdnerS + "&isdnee=" + isdneeS + "&name=" + nameS + "&proviceId=" + proviceIdS + "&branchId=" + branchIdS + "&dateRanger=" + dateRangerS + "&status=" + (statusS != null ? statusS.getType() : "");
    }

    @PostMapping("/export")
    public void export(@RequestParam(name = "orderCode", required = false, defaultValue = "") String orderCode,
                       @RequestParam(name = "packageCode", required = false, defaultValue = "") String packageCode,
                       @RequestParam(name = "isdner", required = false, defaultValue = "") String isdner,
                       @RequestParam(name = "isdnee", required = false, defaultValue = "") String isdnee,
                       @RequestParam(name = "name", required = false, defaultValue = "") String name,
                       @RequestParam(name = "proviceId", required = false, defaultValue = "") String proviceId,
                       @RequestParam(name = "branchIdSS", required = false, defaultValue = "") Long branchId,
                       @RequestParam(name = "dateRanger", required = false, defaultValue = "") String dateRanger,
                       @RequestParam(name = "status", required = false, defaultValue = "") FtthRegisterStatus status,
                       HttpServletResponse response) {
        response = excel.setResponse(response);

        service.export(response, orderCode, packageCode, isdner, isdnee, name, proviceId, branchId, status, dateRanger);
    }
}
