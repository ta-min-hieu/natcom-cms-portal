package com.ringme.controller.loyalty;

import com.ringme.config.LocaleFactory;
import com.ringme.model.voucher.Voucher;
import com.ringme.service.loyalty.MerchantService;
import com.ringme.service.loyalty.VoucherService;
import com.ringme.service.loyalty.VoucherTopicService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/loyalty/voucher")
public class VoucherController {
    @Autowired
    VoucherService service;
    @Autowired
    LocaleFactory localeFactory;
    @Autowired
    VoucherTopicService topicService;
    @Autowired
    MerchantService merchantService;

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "isdn", required = false, defaultValue = "") String isdn,
                             @RequestParam(name = "voucherGroupName", required = false, defaultValue = "") String voucherGroupName,
                             @RequestParam(name = "topicId", required = false, defaultValue = "") Long topicId,
                             @RequestParam(name = "merchantId", required = false, defaultValue = "") Long merchantId,
//                             @RequestParam(name = "status", required = false, defaultValue = "") Integer status,
                             Model model) {
        if(page == null)
            page = 1;

        Page<Voucher> objectPage = service.search(page, pageSize, isdn, voucherGroupName, topicId, merchantId);

        model.addAttribute("title", localeFactory.getMessage("title.list.Voucher"));
        model.addAttribute("isdn", isdn);
        model.addAttribute("voucherGroupName", voucherGroupName);

        if(topicId != null)
            model.addAttribute("topic", topicService.findById(topicId));
        if(merchantId != null)
            model.addAttribute("merchant", merchantService.findById(merchantId));

        model.addAttribute("topicId", topicId);
        model.addAttribute("merchantId", merchantId);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "loyalty/voucher/index";
    }

    @GetMapping(value = {"/update-status", "/update-status/{page}"})
    public String updateProcessStatus(
            @PathVariable(required = false) Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "id") long id,
            @RequestParam(name = "status") int status,
            @RequestParam(name = "isdn", required = false, defaultValue = "") String isdnS,
            @RequestParam(name = "voucherGroupName", required = false, defaultValue = "") String voucherGroupNameS,
            @RequestParam(name = "topicId", required = false, defaultValue = "") Long topicIdS,
            @RequestParam(name = "merchantId", required = false, defaultValue = "") Long merchantIdS,
            RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;

        switch (status) {
            case 1 -> {
                service.updateUsedStatus(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.voucher-used-status.success"));
            }
        }
        return "redirect:/loyalty/voucher/index/" + page + "?pageSize=" + pageSize + "&isdn=" + isdnS + "&voucherGroupName=" + voucherGroupNameS + "&topicId=" + (topicIdS == null ? "" : topicIdS) + "&merchantId=" + (merchantIdS == null ? "" : merchantIdS);
    }
}
