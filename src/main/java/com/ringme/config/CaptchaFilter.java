package com.ringme.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Log4j2
public class CaptchaFilter extends OncePerRequestFilter {
    private static final String CAPTCHA_SESSION_KEY = "captcha";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String contextPath = request.getContextPath();
        String path = request.getRequestURI();

        boolean rs = path.endsWith(".js")
                || path.endsWith(".svg")
                || path.endsWith(".jpg")
                || path.endsWith(".jpeg")
                || path.endsWith(".css")
                || path.endsWith(".png")
                || path.startsWith(contextPath + "/images")
                || path.startsWith(contextPath + "/file")
                || path.startsWith(contextPath + "/js")
                || path.startsWith(contextPath + "/css")
                || path.startsWith(contextPath + "/webjars/")
                || path.startsWith(contextPath + "/vendor");
        //log.info("context: {}, path: {}:  shouldNotFilter: {}", contextPath, path, rs);
        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Kiểm tra nếu đây là yêu cầu đăng nhập
        String contextPath = request.getContextPath();
        log.debug("Captcha filter: requestURI: {} contextPath: {}", request.getRequestURI(), contextPath);
//        chain.doFilter(request, response);

        if (request.getMethod().equalsIgnoreCase("GET")) {
            chain.doFilter(request, response);
        } else if ((contextPath + "/login").equals(request.getRequestURI())
                && request.getMethod().equalsIgnoreCase("POST")) {
            // Lấy giá trị của captcha từ session
            HttpSession session = request.getSession();
            String captcha = (String) session.getAttribute(CAPTCHA_SESSION_KEY);
            String captchaValue = request.getParameter("captcha");
            if (captcha == null || !captcha.equalsIgnoreCase(captchaValue)) {
                session.removeAttribute("captcha");
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}