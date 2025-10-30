package com.ringme.controller.selfcare;

import com.ringme.config.LocaleFactory;
import com.ringme.enums.selfcare.ScheduleCallStatus;
import com.ringme.enums.selfcare.ScheduleCallSupportType;
import com.ringme.model.selfcare.ScheduleCall;
import com.ringme.service.selfcare.ScheduleCallService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/selfcare/schedule-call")
public class ScheduleCallController {
    @Autowired
    ScheduleCallService service;

    @Autowired
    LocaleFactory localeFactory;

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "supportType", required = false, defaultValue = "") ScheduleCallSupportType supportType,
                             @RequestParam(name = "isdn", required = false, defaultValue = "") String isdn,
                             @RequestParam(name = "status", required = false, defaultValue = "") ScheduleCallStatus status,
                             @RequestParam(name = "dateRanger", required = false, defaultValue = "") String dateRanger,
                             Model model) {
        if(page == null)
            page = 1;

        Page<ScheduleCall> objectPage = service.search(page, pageSize, supportType, isdn, status, dateRanger);

        model.addAttribute("title", localeFactory.getMessage("title.list.ScheduleCall"));
        model.addAttribute("supportType", supportType);
        model.addAttribute("isdn", isdn);
        model.addAttribute("dateRanger", dateRanger);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());

        return "schedule-call/index";
    }

    @GetMapping(value = {"/update-status", "/update-status/{page}"})
    public String updateProcessStatus(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "id") long id,
            @RequestParam(name = "note", required = false) String note,
            @RequestParam(name = "status") ScheduleCallStatus status,
            RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;

        switch (status) {
            case ScheduleCallStatus.IN_PROCESS -> {
                service.updateProcessStatus(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-process-status.success"));
            }
            case ScheduleCallStatus.SUCCESS -> {
                service.updateSuccessStatus(id, note);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-success-status.success"));
            }
            case ScheduleCallStatus.CANCEL -> {
                service.updateCancelStatus(id, note);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.ftth-cancel-status.success"));
            }
        }
        return "redirect:/selfcare/schedule-call/index/" + page + "?pageSize=" + pageSize;
    }
}
