package com.ringme.controller.selfcare;

import com.ringme.config.LocaleFactory;
import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.mynatcom.Shop;
import com.ringme.service.selfcare.ShopService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/selfcare/shop")
public class ShopController {
    @Autowired
    ShopService service;

    @Autowired
    LocaleFactory localeFactory;

    @GetMapping("/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> miniAppAjaxSearch(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(service.ajaxSearch(input), HttpStatus.OK);
    }

    @GetMapping(value = {"/index/{page}", "/index"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             Model model) {
        if(page == null)
            page = 1;

        Page<Shop> objectPage = service.search(page, pageSize);

        model.addAttribute("title", localeFactory.getMessage("title.list.Shop"));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        return "selfcare/shop/index";
    }
}
