package com.ringme.controller.sys;


import com.google.code.kaptcha.Producer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;

@Log4j2
@Controller
public class CaptchaController {

    private final Producer captchaProducer;

    @Autowired
    public CaptchaController(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @GetMapping(value = {"/captcha.jpg", "/generate-security"})
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info(">>> ĐÃ VÀO CAPTCHA <<<");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String text = captchaProducer.createText();
        request.getSession().setAttribute("captcha", text);

        BufferedImage image = captchaProducer.createImage(text);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpeg", outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @GetMapping("/check-captcha")
    public String checkCaptcha(HttpServletRequest request) {
        log.info(">>> ĐÃ VÀO check CAPTCHA <<<");
        Object captcha = request.getSession().getAttribute("captcha");
        log.info("captcha: {}", captcha);
        return "Captcha in session: " + (captcha != null ? captcha.toString() : "null");
    }
}

