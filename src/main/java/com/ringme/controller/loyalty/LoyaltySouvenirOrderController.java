package com.ringme.controller.loyalty;

import com.ringme.config.LocaleFactory;
import com.ringme.enums.loyalty.SouvenirOrderStatus;
import com.ringme.model.loyalty.LoyaltySouvenirOrder;
import com.ringme.service.loyalty.LoyaltySouvenirOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/loyalty/loyalty-souvenir-order")
public class LoyaltySouvenirOrderController {
    @Autowired
    LoyaltySouvenirOrderService service;

    @Autowired
    LocaleFactory localeFactory;

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "isdn", required = false, defaultValue = "") String isdn,
                             @RequestParam(name = "orderCode", required = false, defaultValue = "") String orderCode,
                             @RequestParam(name = "title", required = false, defaultValue = "") String name,
                             @RequestParam(name = "status", required = false, defaultValue = "") SouvenirOrderStatus status,
                             Model model) {
        if(page == null)
            page = 1;

        Page<LoyaltySouvenirOrder> objectPage = service.search(page, pageSize, isdn, orderCode, name, status);

        model.addAttribute("title", localeFactory.getMessage("title.list.LoyaltySouvenirOrder"));
        model.addAttribute("isdn", isdn);
        model.addAttribute("orderCode", orderCode);
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "loyalty/loyalty-souvenir-order/index";
    }

    @GetMapping(value = {"/update-status", "/update-status/{page}"})
    public String updateProcessStatus(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "id") long id,
            @RequestParam(name = "status") SouvenirOrderStatus status,
            @RequestParam(name = "isdnS", required = false, defaultValue = "") String isdnS,
            @RequestParam(name = "orderCodeS", required = false, defaultValue = "") String orderCodeS,
            @RequestParam(name = "titleS", required = false, defaultValue = "") String nameS,
            @RequestParam(name = "statusS", required = false, defaultValue = "") SouvenirOrderStatus statusS,
            RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        
        switch (status) {
            case SouvenirOrderStatus.PROCESSING -> {
                service.updateProcessingStatus(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.souvenir-process-status.success"));
            }
            case SouvenirOrderStatus.VALID -> {
                service.updateValidStatus(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.souvenir-valid-status.success"));
            }
            case SouvenirOrderStatus.RECEIVED -> {
                service.updateReceivedStatus(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.souvenir-received-status.success"));
            }
            case SouvenirOrderStatus.CANCEL -> {
                service.updateCancelStatus(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.souvenir-cancel-status.success"));
            }
        }
        return "redirect:/loyalty/loyalty-souvenir-order/index/" + page + "?pageSize=" + pageSize + "&isdn=" + isdnS + "&orderCode=" + orderCodeS + "&title=" + nameS + "&status=" + (statusS != null ? statusS.getType() : "");
    }
}
