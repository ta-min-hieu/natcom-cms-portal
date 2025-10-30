package com.ringme.controller.sys;

import com.ringme.dto.AjaxSearchDto;
import com.ringme.model.sys.Icon;
import com.ringme.model.sys.Menu;
import com.ringme.model.sys.Router;
import com.ringme.repository.sys.IconRepository;
import com.ringme.repository.sys.MenuRepository;
import com.ringme.service.sys.MenuService;
import com.ringme.service.sys.RouterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequestMapping("/sys")
public class MenuController {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    MenuService menuService;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    RouterService routerService;

    @Autowired
    IconRepository iconRepository;

    @GetMapping("/menu/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearch(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(menuService.ajaxSearch(input), HttpStatus.OK);
    }

//    @GetMapping("/menu/index")
//    public String getAllMenu(Model model) {
//        return getAllMenuPage(1,10, model, null);
//    }

    @GetMapping(value = {"/menu/index/{pageNo}", "/menu/index"})
    public String getAllMenuPage(@PathVariable(name = "pageNo", required = false) Integer page,
                                 @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model,
                                 @RequestParam(name = "parentName", required = false) Long parentName) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Page<Menu> menuPageNo = menuService.findMenuPage(parentName, page, pageSize);
//        model.addAttribute("listMenu1", menuService.getListMenuNoParent());
        log.info("parentName|" + parentName);
        model.addAttribute("menus", menuPageNo.toList());
        model.addAttribute("parentNameMenu", menuRepository.getNameEn(parentName));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", menuPageNo.getTotalPages());
        model.addAttribute("totalItems", menuPageNo.getTotalElements());
        return "sys/menu";
    }

    @GetMapping("/menu/create")
    public String createMenu(Model model) {
        Menu menu = new Menu();
        model.addAttribute("menu", menu);
        List<Router> routers = routerService.findAll();
        routers.removeIf(router -> router.getRouter_link().contains("*"));
        List<Icon> icons = iconRepository.findAll();
//        model.addAttribute("listMenu1", menuService.getListMenuNoParent());
        model.addAttribute("listMenu1", menuRepository.findAll());
        model.addAttribute("routers", routers);
        model.addAttribute("icons", icons);
        return "sys/menu-create";
    }

    @GetMapping("/menu/update/{id}")
    public String updateMenu(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Menu> menu = menuService.findMenuById(id);
        if (menu.isPresent()) {
            List<Router> routers = routerService.findAll();
            routers.removeIf(router -> router.getRouter_link().contains("*"));
            List<Icon> icons = iconRepository.findAll();
            String updateMenu = "updateMenu";
//            model.addAttribute("listMenu1", menuService.getListMenuNoParent());
            model.addAttribute("listMenu1", menuRepository.findAll());
            model.addAttribute("menu", menu.get());
            model.addAttribute("routers", routers);
            model.addAttribute("icons", icons);
            model.addAttribute("updateMenu", updateMenu);
            return "sys/menu-create";
        }
        redirectAttributes.addFlashAttribute("error", messageSource.getMessage("menu.noExist", null, LocaleContextHolder.getLocale()));
        return "redirect:/sys/menu/index";
    }


    @PostMapping("/menu/save")
    public String saveMenu(@Valid @ModelAttribute("menu") Menu menu, BindingResult bindingResult, Errors error,@RequestParam("menuParentId")Long idMenuParent, @RequestParam("router") Long routerId, @RequestParam("icon") Long iconId, RedirectAttributes redirectAttributes, Model model) {
        if (error.hasErrors()) {
            List<Router> routers = routerService.findAllRouterActive();
            List<Icon> icons = iconRepository.findAll();
            model.addAttribute("routers", routers);
            model.addAttribute("icons", icons);
            model.addAttribute("listMenu1", menuService.getListMenuNoParent());
            return "sys/menu-create";
        } else {
            if (menu.getId() != null) {
                Optional<Menu> menuPresent = menuService.findMenuById(menu.getId());
                if (menuPresent.isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", messageSource.getMessage("menu.noExist", null, LocaleContextHolder.getLocale()));
                    return "redirect:/sys/menu/index";
                }
            }
            Optional<Router> router = routerService.findRouterById(routerId);
            Optional<Icon> icon = iconRepository.findById(iconId);
            if (router.isEmpty() || icon.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("menu.path1", null, LocaleContextHolder.getLocale()));
                return "redirect:/sys/menu/index";
            }
            if (menu.getOrderNum() < 0) {
                redirectAttributes.addFlashAttribute("error", messageSource.getMessage("menu.menu1", null, LocaleContextHolder.getLocale()));
                return "redirect:/sys/menu/index";
            }
            menu.setNameEn(menu.getNameEn().trim().replaceAll("\s+", " "));
            Optional<Menu> menuParent = menuService.findMenuById(idMenuParent);
            menuParent.ifPresent(menu::setParentName);
            menu.setNameVn(menu.getNameVn().trim().replaceAll("\s+", " "));
            menu.setRouter(router.get());
            menu.setIcon(icon.get());
            menuService.saveMenu(menu);
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
            return "redirect:/sys/menu/index";
        }
    }

    @GetMapping(value = {"/menu/delete", "/menu/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(name = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Optional<Menu> menu = menuService.findMenuById(id);
        if (menu.isPresent()) {
            menuService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        } else {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("menu.noExist", null, LocaleContextHolder.getLocale()));
        }
        return "redirect:/sys/menu/index/" + page + "?pageSize=" + pageSize;
    }
}
