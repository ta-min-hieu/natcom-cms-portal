package com.ringme.controller.loyalty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.loyalty.SubMerchantDto;
import com.ringme.model.voucher.SubMerchant;
import com.ringme.service.loyalty.SubMerchantService;
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
@RequestMapping("/loyalty/sub-merchant")
public class SubMerchantController {
    @Autowired
    SubMerchantService service;
    @Autowired
    LocaleFactory localeFactory;

    @GetMapping("/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearch(
            @RequestParam(name = "input", required = false) String input,
            @RequestParam(name = "idMerchant", required = false) String idMerchant
    ) {
        return new ResponseEntity<>(service.ajaxSearch(input, idMerchant), HttpStatus.OK);
    }

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "name", required = false, defaultValue = "") String name,
                             @RequestParam(name = "status", required = false, defaultValue = "") Integer status,
                             Model model) {
        if(page == null)
            page = 1;

        Page<SubMerchant> objectPage = service.search(page, pageSize, name, status);

        model.addAttribute("title", localeFactory.getMessage("title.list.SubMerchant"));
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "loyalty/sub-merchant/index";
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
        return "redirect:/loyalty/sub-merchant/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        SubMerchant dto;
        if(id == null) {
            dto = new SubMerchant();
            model.addAttribute("title", localeFactory.getMessage("title.create.SubMerchant"));
        } else {
            dto = service.findById(id);
            model.addAttribute("title", localeFactory.getMessage("title.update.SubMerchant"));
        }
        model.addAttribute("model", dto);
        return "loyalty/sub-merchant/form";
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("model") SubMerchantDto dto,
                       Errors error,
                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("dto|{}", dto);
        Long id = dto.getId();
        if(!error.hasErrors()) {
            SubMerchant obj;
            if(id == null) {
                obj = new SubMerchant();
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.create.success"));
            } else {
                obj = service.findById(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.update.success"));
            }

            obj.setName(dto.getName());
            obj.setAddress(dto.getAddress());
            obj.setOwnerName(dto.getOwnerName());
            obj.setOwnerPhoneNumber(dto.getOwnerPhoneNumber());
            obj.setIdMerchant(dto.getIdMerchant());
            obj.setStatus(dto.getStatus());

            service.save(obj);
        } else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.create.error"));
                return "redirect:/loyalty/sub-merchant/form";
            } else {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error"));
                return "redirect:/loyalty/sub-merchant/form?id=" + dto.getId();
            }
        }
        return "redirect:/loyalty/sub-merchant/index";
    }
}
