package com.ringme.controller.sys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.common.UploadFile;
import com.ringme.config.AppConfig;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.sys.PartnerDto;
import com.ringme.model.sys.Partner;
import com.ringme.repository.sys.UserRepository;
import com.ringme.service.sys.PartnerService;
import com.ringme.service.sys.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/sys/partner")
public class PartnerController {
    @Autowired
    PartnerService service;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserService userService;

    @Autowired
    UploadFile uploadFile;

    @Autowired
    AppConfig appConfiguration;

    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final int LENGTH = 32;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "name", required = false) String name,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             Model model) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        PartnerDto dto = service.processSearch(name);
        Page<Partner> objectPage = service.get(dto, page, pageSize);
        List<Partner> objectList = objectPage.toList();
        model.addAttribute("name", name);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectList);
        model.addAttribute("title", messageSource.getMessage(
                "title.list.partner", null,
                LocaleContextHolder.getLocale()));
        return "sys/partner/index";
    }

    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                         @RequestParam(name = "id") int id,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;

        try {
            log.info("id|{}", id);
            service.delete(id);
            redirectAttributes.addFlashAttribute(
                    "success", messageSource.getMessage(
                            "title.delete.success", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("ERROR" + e.getMessage(), e);
        }
        return "redirect:/sys/partner/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(Model model) {
        Partner dto = new Partner();
        model.addAttribute("model", dto);
        boolean check = false;
        model.addAttribute("check", check);
        model.addAttribute("title", messageSource.getMessage(
                "title.create.partner",
                null, LocaleContextHolder.getLocale()));
        return "sys/partner/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Model model) {
        Partner dto = service.findById(id);
        model.addAttribute("model", dto);
        boolean check = true;
        model.addAttribute("check", check);
        model.addAttribute("title", messageSource.getMessage(
                "title.update.partner",
                null, LocaleContextHolder.getLocale()));
        return "sys/partner/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") PartnerDto dto,
                       Errors error,
                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        if(!error.hasErrors()){
            Partner object = new Partner();

            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("success",
                        messageSource.getMessage("title.create.success",
                                null, LocaleContextHolder.getLocale()));
                object.setCreatedBy(userService.getId());
                object.setCreatedAt(new Date());
                object.setToken(generateRandomString());
            } else {
                object = service.findById(dto.getId());
                redirectAttributes.addFlashAttribute("success",
                        messageSource.getMessage("title.update.success",
                                null, LocaleContextHolder.getLocale()));
                object.setUpdatedBy(userService.getId());
                object.setUpdatedAt(new Date());
            }

            object.setName(dto.getName());
//            object.setCode(dto.getCode());
            object.setStatus(dto.getStatus());
            object.setDescription(dto.getDescription());

            object.setAddress(dto.getAddress());
            object.setEmail(dto.getEmail());
            object.setContact(dto.getContact());
            object.setDirector(dto.getDirector());
            object.setCallbackUrl(dto.getCallbackUrl());

            log.info("objectSave|" + object);
            service.save(object);
        } else {
            if(dto.getId() == null)
                return "redirect:/sys/partner/create";
            else
                return "redirect:/sys/partner/update/" + dto.getId();
        }
        return "redirect:/sys/partner/index";
    }

    @GetMapping("/partnerId-ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> partnerIdAjaxSearch(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(service.ajaxSearch(input), HttpStatus.OK);
    }

    @GetMapping("/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearch(@RequestParam(name = "input", required = false) String input) {
        return new ResponseEntity<>(service.ajaxSearch(input), HttpStatus.OK);
    }

    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

}