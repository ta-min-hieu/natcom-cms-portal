package com.ringme.controller.loyalty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.loyalty.MerchantDto;
import com.ringme.model.voucher.Merchant;
import com.ringme.service.loyalty.MerchantService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/loyalty/merchant")
public class MerchantController {
    @Autowired
    MerchantService service;
    @Autowired
    LocaleFactory localeFactory;

    @GetMapping("/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearch(@RequestParam(name = "input", required = false) String input) {
        return new ResponseEntity<>(service.ajaxSearch(input), HttpStatus.OK);
    }

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "name", required = false, defaultValue = "") String name,
                             @RequestParam(name = "status", required = false, defaultValue = "") Integer status,
                             Model model) {
        if(page == null)
            page = 1;

        Page<Merchant> objectPage = service.search(page, pageSize, name, status);

        model.addAttribute("title", localeFactory.getMessage("title.list.Merchant"));
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "loyalty/merchant/index";
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
        return "redirect:/loyalty/merchant/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        Merchant dto;
        if(id == null) {
            dto = new Merchant();
            model.addAttribute("title", localeFactory.getMessage("title.create.Merchant"));
        } else {
            dto = service.findById(id);
            model.addAttribute("title", localeFactory.getMessage("title.update.Merchant"));
        }
        model.addAttribute("model", dto);
        return "loyalty/merchant/form";
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("model") MerchantDto dto,
                       Errors error,
                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("dto|{}", dto);
        Long id = dto.getId();
        if(!error.hasErrors()) {
            Merchant obj;
            if(id == null) {
                obj = new Merchant();
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.create.success"));
            } else {
                obj = service.findById(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.update.success"));
            }

            obj.setName(dto.getName());
            obj.setOwnerName(dto.getOwnerName());
            obj.setOwnerPhoneNumber(dto.getOwnerPhoneNumber());
            obj.setStatus(dto.getStatus());
            obj.setNote(dto.getNote());

            service.save(obj);
        } else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.create.error"));
                return "redirect:/loyalty/merchant/form";
            } else {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error"));
                return "redirect:/loyalty/merchant/form?id=" + dto.getId();
            }
        }
        return "redirect:/loyalty/merchant/index";
    }
}
