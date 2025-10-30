package com.ringme.controller.selfcare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.selfcare.ScSubServiceDto;
import com.ringme.enums.sys.CommonStatus;
import com.ringme.model.selfcare.ScSubService;
import com.ringme.model.selfcare.SelfcareService;
import com.ringme.service.selfcare.ScServiceService;
import com.ringme.service.selfcare.ScSubServiceService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/selfcare/sc-sub-service")
public class ScSubServiceController {
    @Autowired
    ScSubServiceService service;
    @Autowired
    LocaleFactory localeFactory;
    @Autowired
    ScServiceService scService;

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "name", required = false, defaultValue = "") String name,
                             @RequestParam(name = "code", required = false, defaultValue = "") String code,
                             @RequestParam(name = "serviceId", required = false, defaultValue = "") Long serviceId,
                             @RequestParam(name = "status", required = false, defaultValue = "") CommonStatus status,
                             Model model) {
        if(page == null)
            page = 1;

        Page<ScSubService> objectPage = service.search(page, pageSize, name, code, status, serviceId);

        model.addAttribute("title", localeFactory.getMessage("title.list.ScSubService"));
        model.addAttribute("name", name);
        model.addAttribute("code", code);
        model.addAttribute("serviceId", serviceId);
        if(serviceId != null)
            model.addAttribute("scService", scService.findById(serviceId));

        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "selfcare/sc-sub-service/index";
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
        return "redirect:/selfcare/sc-sub-service/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/form-v2")
    public String formV2(
            @RequestParam(value = "atView") String atView,
            @RequestParam(value = "serviceId") long serviceId,
            @RequestParam(value = "id", required = false) Long id,
            ModelMap model) throws Exception {
        log.info("id: {}", id);
        try {
            ScSubService dto;
            if(id == null)
                dto = new ScSubService();
            else
                dto = service.findById(id);

            dto.setAtView(atView);
            dto.setServiceId(serviceId);
            model.put("modelS", dto);

            return "selfcare/sc-sub-service/form :: scSubServiceForm";
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw new Exception(e);
        }
    }

    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        ScSubService dto;
        if(id == null) {
            dto = new ScSubService();
            model.addAttribute("title", localeFactory.getMessage("title.create.ScSubService"));
        } else {
            dto = service.findById(id);
            model.addAttribute("title", localeFactory.getMessage("title.update.ScSubService"));
        }
        model.addAttribute("modelS", dto);
        return "selfcare/sc-sub-service/form";
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("model") ScSubServiceDto dto,
                       Errors error,
                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("dto|{}", dto);
        Long id = dto.getId();
        String atView = dto.getAtView();
        long serviceId = dto.getServiceId();
        if(!error.hasErrors()) {
            ScSubService obj;
            if(id == null) {
                obj = new ScSubService();
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.create.success"));
            } else {
                obj = service.findById(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.update.success"));
            }

            obj.setName(dto.getName());
            obj.setCode(dto.getCode());
            obj.setStatus(dto.getStatus());
            obj.setDescription(dto.getDescription());
            obj.setPrice(dto.getPrice());
//            obj.setUnit(dto.getUnit());
            obj.setRegistedBy(1);
            obj.setAllowPrepaid(dto.getAllowPrepaid());
            obj.setAllowPostpaid(dto.getAllowPostpaid());
            obj.setAllow3gsim(dto.getAllow3gsim());
            obj.setAllow4gsim(dto.getAllow4gsim());
            obj.setAllow5gsim(dto.getAllow5gsim());
            obj.setIsGift(dto.getIsGift());
            obj.setIsAutoRenew(dto.getIsAutoRenew());
            obj.setDkSoapTkg(dto.getDkSoapTkg());
            obj.setDkSoapVi(dto.getDkSoapVi());
            obj.setCancelSoap(dto.getCancelSoap());
            obj.setServiceId(dto.getServiceId());

            service.save(obj);
        } else {
            if(dto.getId() == null)
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.create.error"));
            else
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error"));
        }
        if(atView.equals("form"))
            return "redirect:/selfcare/sc-service/form?id=" + serviceId;
        else
            return "redirect:/selfcare/sc-service/index";
    }
}
