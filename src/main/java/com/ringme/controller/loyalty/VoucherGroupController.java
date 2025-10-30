package com.ringme.controller.loyalty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.loyalty.VoucherGroupDto;
import com.ringme.model.voucher.VoucherGroup;
import com.ringme.service.loyalty.VoucherGroupService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/loyalty/voucher-group")
public class VoucherGroupController {
    @Autowired
    VoucherGroupService service;
    @Autowired
    LocaleFactory localeFactory;

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "name", required = false, defaultValue = "") String name,
                             @RequestParam(name = "status", required = false, defaultValue = "") Integer status,
                             @RequestParam(name = "dateExpireds", required = false, defaultValue = "") String dateExpireds,
                             Model model) {
        if(page == null)
            page = 1;

        Page<VoucherGroup> objectPage = service.search(page, pageSize, name, status, dateExpireds);

        model.addAttribute("title", localeFactory.getMessage("title.list.VoucherGroup"));
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("dateExpireds", dateExpireds);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "loyalty/voucher-group/index";
    }

    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                         @RequestParam(name = "id") Long id,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        log.info("id|{}", id);
        service.delete(id);
        redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.delete.success"));
        return "redirect:/loyalty/voucher-group/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        VoucherGroupDto dto;
        if(id == null) {
            dto = new VoucherGroupDto();
            model.addAttribute("title", localeFactory.getMessage("title.create.VoucherGroup"));
        } else {
            dto = new VoucherGroupDto(service.findById(id));
            model.addAttribute("title", localeFactory.getMessage("title.update.VoucherGroup"));
        }
        model.addAttribute("model", dto);
        return "loyalty/voucher-group/form";
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("model") VoucherGroupDto dto,
                       @RequestParam String thumbUpload1,
                       Errors error,
                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("dto|{}", dto);
        if(!error.hasErrors()) {
            int rs = service.saveHandler(redirectAttributes, dto, thumbUpload1);
            if(rs == 1)
                return "redirect:/loyalty/voucher-group/form?id=" + dto.getId();
            else if(rs == 2) {
                if(dto.getId() == null) {
                    redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.create.error"));
                    return "redirect:/loyalty/voucher-group/form";
                } else {
                    redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error"));
                    return "redirect:/loyalty/voucher-group/form?id=" + dto.getId();
                }
            }
        } else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.create.error"));
                return "redirect:/loyalty/voucher-group/form";
            } else {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error"));
                return "redirect:/loyalty/voucher-group/form?id=" + dto.getId();
            }
        }
        return "redirect:/loyalty/voucher-group/index";
    }
}
