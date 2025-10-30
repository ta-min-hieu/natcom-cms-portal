package com.ringme.controller.sys;

import com.ringme.dto.sys.UserSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        log.info("vao đâyyyy");
        log.info("--- dev password: {} ---", passwordEncoder.encode("1"));
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSecurity) {
            return "redirect:/index";
        }
        return "login";
    }

    @GetMapping("/login-error")
    public String loginerror(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Tài khoản hoặc mật khẩu không chính xác. Vui lòng thử lại!");
        return "redirect:/login";
    }

    @GetMapping("/")
    public String getHome() {
        return "redirect:/index";
    }

    @PostMapping("/login")
    public String login(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Captcha không chính xác!");
        return "redirect:/login";
    }

    @GetMapping({"/403"})
    public String error403() {
        log.info(passwordEncoder.encode("ERROR 403"));
        return "403";
    }

    @GetMapping({"/404"})
    public String error404() {
        log.info(passwordEncoder.encode("123456"));
        return "404";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}