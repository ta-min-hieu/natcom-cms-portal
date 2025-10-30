package com.ringme.controller.selfcare;

import com.ringme.config.LocaleFactory;
import com.ringme.model.selfcare.PaymentMsVasHistory;
import com.ringme.service.selfcare.PaymentMsVasHistoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequestMapping("/selfcare/payment-ms-vas-history")
public class PaymentMsVasHistoryController {
    @Autowired
    PaymentMsVasHistoryService service;

    @Autowired
    LocaleFactory localeFactory;

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "isdn", required = false, defaultValue = "") String isdn,
                             @RequestParam(name = "serviceCode", required = false, defaultValue = "") String serviceCode,
                             @RequestParam(name = "status", required = false, defaultValue = "") Integer status,
                             @RequestParam(name = "url", required = false, defaultValue = "") String url,
                             @RequestParam(name = "dateRanger", required = false, defaultValue = "") String dateRanger,
                             Model model) {
        if(page == null)
            page = 1;

        Page<PaymentMsVasHistory> objectPage = service.search(page, pageSize, isdn, serviceCode, status, url, dateRanger);

        model.addAttribute("title", localeFactory.getMessage("title.list.PaymentMsVasHistory"));
        model.addAttribute("isdn", isdn);
        model.addAttribute("serviceCode", serviceCode);
        model.addAttribute("status", status);
        model.addAttribute("url", url);
        model.addAttribute("dateRanger", dateRanger);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "payment-ms-vas-history/index";
    }
}
