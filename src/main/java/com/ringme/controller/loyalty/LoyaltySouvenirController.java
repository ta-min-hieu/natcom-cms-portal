package com.ringme.controller.loyalty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.common.Helper;
import com.ringme.common.UploadFile;
import com.ringme.config.AppConfig;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.loyalty.LoyaltySouvenirDto;
import com.ringme.model.loyalty.LoyaltySouvenir;
import com.ringme.service.loyalty.LoyaltySouvenirService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.nio.file.Path;

@Controller
@Log4j2
@RequestMapping("/loyalty/loyalty-souvenir")
public class LoyaltySouvenirController {
    @Autowired
    LoyaltySouvenirService service;
    @Autowired
    LocaleFactory localeFactory;
    @Autowired
    UploadFile uploadFile;
    @Autowired
    AppConfig appConfig;

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(name = "title", required = false, defaultValue = "") String name,
                             @RequestParam(name = "status", required = false, defaultValue = "") Integer status,
                             @RequestParam(name = "dateExpireds", required = false, defaultValue = "") String dateExpireds,
                             Model model) {
        if(page == null)
            page = 1;

        Page<LoyaltySouvenir> objectPage = service.search(page, pageSize, name, status, dateExpireds);

        model.addAttribute("title", localeFactory.getMessage("title.list.LoyaltySouvenirReview"));
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("dateExpireds", dateExpireds);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "loyalty/loyalty-souvenir/index";
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
        return "redirect:/loyalty/loyalty-souvenir/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        LoyaltySouvenir dto;
        if(id == null) {
            dto = new LoyaltySouvenir();
            model.addAttribute("title", localeFactory.getMessage("title.create.LoyaltySouvenirReview"));
        } else {
            dto = service.findById(id);
            model.addAttribute("title", localeFactory.getMessage("title.update.LoyaltySouvenirReview"));
        }
        model.addAttribute("model", dto);
        return "loyalty/loyalty-souvenir/form";
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("model") LoyaltySouvenirDto dto,
                       @RequestParam String thumbUpload1,
                       Errors error,
                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("dto|{}", dto);
        Long id = dto.getId();
        if(!error.hasErrors()) {
            LoyaltySouvenir obj;
            if(id == null) {
                obj = new LoyaltySouvenir();
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.create.success"));
                obj.setQuantityReal(0);
                obj.setQuantityExchanged(0);
            } else {
                obj = service.findById(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.update.success"));

                if(dto.getQuantityTotal() < obj.getQuantityExchanged()) {
                    redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error.total-souvenir"));
                    return "redirect:/loyalty/loyalty-souvenir/form?id=" + dto.getId();
                }
            }

            obj.setTitle(dto.getTitle());
            obj.setStatus(dto.getStatus());
            obj.setPoint(dto.getPoint());
            obj.setUnit("points");
            obj.setDescription(dto.getDescription());
            obj.setStartDate(Helper.convertDate(dto.getStartDate()));
            obj.setDateExpired(Helper.convertDate(dto.getDateExpired()));
            obj.setQuantityTotal(dto.getQuantityTotal());

            Path fileNameVideoImage1 = uploadFile.createImageFileV1(thumbUpload1, "images", appConfig.getFileInDBPrefix(), appConfig.getRootPath());
            if (fileNameVideoImage1 != null)
                obj.setIconUrl(fileNameVideoImage1.toString());
            service.save(obj);
        } else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.create.error"));
                return "redirect:/loyalty/loyalty-souvenir/form";
            } else {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error"));
                return "redirect:/loyalty/loyalty-souvenir/form?id=" + dto.getId();
            }
        }
        return "redirect:/loyalty/loyalty-souvenir/index";
    }
}
