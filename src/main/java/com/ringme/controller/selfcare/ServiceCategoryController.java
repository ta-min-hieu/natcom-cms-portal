package com.ringme.controller.selfcare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.selfcare.ServiceCategoryDto;
import com.ringme.enums.sys.CommonStatus;
import com.ringme.model.selfcare.ServiceCategory;
import com.ringme.service.selfcare.ServiceCategoryService;
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
@RequestMapping("/selfcare/service-category")
public class ServiceCategoryController {
    @Autowired
    ServiceCategoryService service;
    @Autowired
    LocaleFactory localeFactory;

    @GetMapping("/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearch(@RequestParam(name = "input", required = false) String input) {
        return new ResponseEntity<>(service.ajaxSearch(input), HttpStatus.OK);
    }

    @GetMapping("/ajax-search-no-child")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearchNoChild(@RequestParam(name = "input", required = false) String input) {
        return new ResponseEntity<>(service.ajaxSearchNoChild(input), HttpStatus.OK);
    }

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "name", required = false, defaultValue = "") String name,
                             @RequestParam(name = "categoryLevel", required = false, defaultValue = "") Integer categoryLevel,
                             @RequestParam(name = "status", required = false, defaultValue = "") CommonStatus status,
                             Model model) {
        if(page == null)
            page = 1;

        Page<ServiceCategory> objectPage = service.search(page, pageSize, name, categoryLevel, status);

        model.addAttribute("title", localeFactory.getMessage("title.list.ServiceCategory"));
        model.addAttribute("name", name);
        model.addAttribute("categoryLevel", categoryLevel);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "selfcare/service-category/index";
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
        return "redirect:/selfcare/service-category/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        ServiceCategory dto;
        if(id == null) {
            dto = new ServiceCategory();
            model.addAttribute("title", localeFactory.getMessage("title.create.ServiceCategory"));
        } else {
            dto = service.findById(id);
            model.addAttribute("title", localeFactory.getMessage("title.update.ServiceCategory"));
        }
        model.addAttribute("model", dto);
        return "selfcare/service-category/form";
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("model") ServiceCategoryDto dto,
                       Errors error,
                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("dto|{}", dto);
        Long id = dto.getId();
        if(!error.hasErrors()) {
            ServiceCategory obj;
            if(id == null) {
                obj = new ServiceCategory();
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.create.success"));
            } else {
                obj = service.findById(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.update.success"));
            }

            obj.setName(dto.getName());
            obj.setStatus(dto.getStatus());
            obj.setDescription(dto.getDescription());
            obj.setParentId(dto.getParentId());

            if(dto.getParentId() == null)
                obj.setCategoryLevel(1);
            else {
                ServiceCategory parent = service.findById(dto.getParentId());
                if(parent == null)
                    obj.setCategoryLevel(1);
                else
                    obj.setCategoryLevel(parent.getCategoryLevel() + 1);
            }

            service.save(obj);
        } else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.create.error"));
                return "redirect:/selfcare/service-category/form";
            } else {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error"));
                return "redirect:/selfcare/service-category/form?id=" + dto.getId();
            }
        }
        return "redirect:/selfcare/service-category/index";
    }
}
