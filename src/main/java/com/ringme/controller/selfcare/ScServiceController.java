package com.ringme.controller.selfcare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.common.UploadFile;
import com.ringme.config.AppConfig;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.selfcare.SelfcareServiceDto;
import com.ringme.enums.sys.CommonStatus;
import com.ringme.model.selfcare.SelfcareService;
import com.ringme.service.selfcare.ScServiceService;
import com.ringme.service.selfcare.ScSubServiceService;
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

import java.nio.file.Path;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/selfcare/sc-service")
public class ScServiceController {
    @Autowired
    ScServiceService service;
    @Autowired
    LocaleFactory localeFactory;
    @Autowired
    UploadFile uploadFile;
    @Autowired
    AppConfig appConfig;
    @Autowired
    ServiceCategoryService categoryService;
    @Autowired
    ScSubServiceService subService;

    @GetMapping("/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearch(@RequestParam(name = "input", required = false) String input) {
        return new ResponseEntity<>(service.ajaxSearch(input), HttpStatus.OK);
    }

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "name1", required = false, defaultValue = "") String name,
                             @RequestParam(name = "serviceCategoryId", required = false, defaultValue = "") Long serviceCategoryId,
                             @RequestParam(name = "status1", required = false, defaultValue = "") CommonStatus status,
                             Model model) {
        if(page == null)
            page = 1;

        Page<SelfcareService> objectPage = service.search(page, pageSize, name, serviceCategoryId, status);

        model.addAttribute("title", localeFactory.getMessage("title.list.SelfcareService"));
        model.addAttribute("name", name);
        model.addAttribute("serviceCategoryId", serviceCategoryId);
        if(serviceCategoryId != null)
            model.addAttribute("serviceCategory", categoryService.findById(serviceCategoryId));
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "selfcare/sc-service/index";
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
        return "redirect:/selfcare/sc-service/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        SelfcareService dto;
        if(id == null) {
            dto = new SelfcareService();
            model.addAttribute("title", localeFactory.getMessage("title.create.SelfcareService"));
        } else {
            dto = service.findById(id);
            model.addAttribute("title", localeFactory.getMessage("title.update.SelfcareService"));
        }
        model.addAttribute("model", dto);
        model.addAttribute("subServices", subService.getList(id));
        return "selfcare/sc-service/form";
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("model") SelfcareServiceDto dto,
                       @RequestParam(required = false) String thumbUpload11,
                       @RequestParam(required = false) String thumbUpload169,
                       Errors error,
                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("dto|{}", dto);
        Long id = dto.getId();
        if(!error.hasErrors()) {
            SelfcareService obj;
            if(id == null) {
                obj = new SelfcareService();
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.create.success"));
            } else {
                obj = service.findById(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.update.success"));
            }

            obj.setName(dto.getName());
            obj.setNameHT(dto.getNameHT());
            obj.setStatus(dto.getStatus());
            obj.setDescription(dto.getDescription());
            obj.setDescriptionHT(dto.getDescriptionHT());
            obj.setShortDes(dto.getShortDes());
            obj.setShortDesHT(dto.getShortDesHT());
            obj.setServiceCategoryId(dto.getServiceCategoryId());

            if(thumbUpload11 != null && !thumbUpload11.isEmpty()) {
                Path fileNameVideoImage1 = uploadFile.createImageFileV1(thumbUpload11, "images", appConfig.getFileInDBPrefix(), appConfig.getRootPath());
                if (fileNameVideoImage1 != null)
                    obj.setThumb11(fileNameVideoImage1.toString());
            }

            if(thumbUpload169 != null && !thumbUpload169.isEmpty()) {
                Path fileNameVideoImage169 = uploadFile.createImageFileV1(thumbUpload169, "images", appConfig.getFileInDBPrefix(), appConfig.getRootPath());
                if (fileNameVideoImage169 != null)
                    obj.setThumb169(fileNameVideoImage169.toString());
            }

            service.save(obj);
        } else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.create.error"));
                return "redirect:/selfcare/sc-service/form";
            } else {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error"));
                return "redirect:/selfcare/sc-service/form?id=" + dto.getId();
            }
        }
        return "redirect:/selfcare/sc-service/index";
    }
}
