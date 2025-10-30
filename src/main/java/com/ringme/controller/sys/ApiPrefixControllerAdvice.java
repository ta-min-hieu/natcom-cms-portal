package com.ringme.controller.sys;

import com.google.gson.Gson;
import com.ringme.dto.sys.MenuDto;
import com.ringme.dto.sys.UserSecurity;
import com.ringme.model.sys.Menu;
import com.ringme.service.sys.MenuService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestControllerAdvice
@Log4j2
public class ApiPrefixControllerAdvice implements WebMvcConfigurer {
    @Value("${server.servlet.context-path}")
    String prefix;
    @Autowired
    MenuService menuService;
    @Autowired
    HttpSession session;

    private static final Locale localeContext = LocaleContextHolder.getLocale();

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(prefix, c -> true);
    }

    @ModelAttribute("listMenu")
    public List<MenuDto> getMenuItemsVHTM(Authentication authentication) {
        List<MenuDto> userMenus = new ArrayList<>();
        if (authentication != null && authentication.isAuthenticated()) {
            if (session.getAttribute("user-menu") != null) {
                return (List<MenuDto>) session.getAttribute("user-menu");
            }
            UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();

            Set<String> allowdRouter = userSecurity.getRouter();
            if (allowdRouter == null || allowdRouter.isEmpty())
                return new ArrayList<>();
            allowdRouter.add("/index");

            List<Menu> menus = menuService.findAll();

            userMenus = getListChild(menus, null, allowdRouter);

            Gson gson = new Gson();
            log.debug("Allow link: {}", gson.toJson(allowdRouter));
            session.setAttribute("user-menu", userMenus);
            log.debug("After filter|" + gson.toJson(userMenus));
        }
        return userMenus;
    }

    private List<MenuDto> getListChild(List<Menu> menus, Long id, Set<String> allowdRouter) {
        List<MenuDto> list = new ArrayList<>();
        List<Menu> menusNoParent = menus.stream()
                .filter(menu -> menu.getParentId() == id).toList();

        if(menusNoParent.isEmpty())
            return null;

        for (Menu menu : menusNoParent) {
            if(menu.getRouter() == null || !allowAccess(menu.getRouter().getRouter_link(), allowdRouter))
                continue;

            MenuDto dto = menu.convertToDto(localeContext.getLanguage());
            dto.setLstChildMenus(getListChild(menus, menu.getId(), allowdRouter));
            list.add(dto);
        }

        return list;
    }

    private boolean allowAccess(String routerLink, Set<String> allowdRouter) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String userRouter: allowdRouter) {
            if(antPathMatcher.match(userRouter, routerLink))
                return true;
        }
        return false;
    }
}

