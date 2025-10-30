package com.ringme.controller.loyalty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ringme.common.UploadFile;
import com.ringme.config.AppConfig;
import com.ringme.config.LocaleFactory;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.dto.loyalty.VoucherTopicDto;
import com.ringme.model.voucher.VoucherTopic;
import com.ringme.service.loyalty.VoucherTopicService;
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
@RequestMapping("/loyalty/voucher-topic")
public class VoucherTopicController {
    @Autowired
    VoucherTopicService service;
    @Autowired
    LocaleFactory localeFactory;
    @Autowired
    UploadFile uploadFile;
    @Autowired
    AppConfig appConfig;

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

        Page<VoucherTopic> objectPage = service.search(page, pageSize, name, status);

        model.addAttribute("title", localeFactory.getMessage("title.list.VoucherTopic"));
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "loyalty/voucher-topic/index";
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
        return "redirect:/loyalty/voucher-topic/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        VoucherTopic dto;
        if(id == null) {
            dto = new VoucherTopic();
            model.addAttribute("title", localeFactory.getMessage("title.create.VoucherTopic"));
        } else {
            dto = service.findById(id);
            model.addAttribute("title", localeFactory.getMessage("title.update.VoucherTopic"));
        }
        model.addAttribute("model", dto);
        return "loyalty/voucher-topic/form";
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("model") VoucherTopicDto dto,
                       @RequestParam String thumbUpload1,
                       Errors error,
                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        log.info("dto|{}", dto);
        Long id = dto.getId();
        if(!error.hasErrors()) {
            VoucherTopic obj;
            if(id == null) {
                obj = new VoucherTopic();
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.create.success"));
            } else {
                obj = service.findById(id);
                redirectAttributes.addFlashAttribute("success", localeFactory.getMessage("title.update.success"));
            }

            obj.setName(dto.getName());
            obj.setStatus(dto.getStatus());
            obj.setDescription(dto.getDescription());

            Path fileNameVideoImage1 = uploadFile.createImageFileV1(thumbUpload1, "images", appConfig.getFileInDBPrefix(), appConfig.getRootPath());
            if (fileNameVideoImage1 != null)
                obj.setIconUrl(fileNameVideoImage1.toString());

            service.save(obj);
        } else {
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.create.error"));
                return "redirect:/loyalty/voucher-topic/form";
            } else {
                redirectAttributes.addFlashAttribute("error", localeFactory.getMessage("title.update.error"));
                return "redirect:/loyalty/voucher-topic/form?id=" + dto.getId();
            }
        }
        return "redirect:/loyalty/voucher-topic/index";
    }
}
