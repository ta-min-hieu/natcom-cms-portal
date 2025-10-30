package com.ringme.config;

import com.ringme.dto.sys.UserSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Log4j2
@Component
public class CustomFilter extends OncePerRequestFilter {
    @Autowired
    @Lazy
    private AntPathMatcher antPathMatcher;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        log.debug("Custom filter " + request.getRequestURI());
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            if (!requestURI.equals(contextPath + "/")
                    && !requestURI.equals(contextPath + "/index")
                    && !requestURI.equals(contextPath + "/login")
                    && !requestURI.equals(contextPath + "/logout")
                    && !requestURI.equals(contextPath + "/error")
                    && !requestURI.equals(contextPath + "/403")
                    && !requestURI.equals(contextPath + "/404")
                    && !requestURI.equals(contextPath + "/alert-start")
                    && !requestURI.startsWith(contextPath + "/ws")
                    && !requestURI.startsWith(contextPath + "/api/v1")
                    && !requestURI.startsWith(contextPath + "/test/api")
                    && !requestURI.equals(contextPath + "/captcha.jpg") && !requestURI.startsWith(contextPath + "/API/")
                    && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSecurity userDetails) {
                Set<String> router = userDetails.getRouter();

                boolean check = false;
                for (String str : router) {
                    if (antPathMatcher.match(contextPath + str, requestURI)) {
                        check = true;
                        log.debug("MATCHED request: {} with {}",requestURI, str);
                    }
                }
                if (requestURI.matches(contextPath + "/error")) {
                    log.debug("Custom filter to error ");
                    request.getRequestDispatcher("/index").forward(request, response);
                }
                if (check)
                    request.getRequestDispatcher(requestURI).forward(request, response);
                else {
                    log.debug("Custom filter to 403 ");
                    response.sendRedirect(contextPath + "/403");
                }
            } else {
                log.debug("Custom filter go to here 1");
                request.getRequestDispatcher(requestURI).forward(request, response);
//                filterChain.doFilter(request, response);
            }
        } else {
            log.debug("Custom filter go to here 2");
            //request.getRequestDispatcher(requestURI).forward(request, response);
            filterChain.doFilter(request, response);

        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();

        boolean rs = uri.endsWith(".js")
                || uri.endsWith(".svg")
                || uri.endsWith(".css")
                || (uri.endsWith(".png"))
                || uri.endsWith(".jpeg")
                || (uri.endsWith(".jpg") && !uri.endsWith("captcha.jpg"))
                || uri.startsWith(contextPath + "/generate-security")
                || uri.startsWith(contextPath + "/check-captcha")
                || uri.startsWith(contextPath + "/images")
                || uri.startsWith(contextPath + "/img")
                || uri.startsWith(contextPath + "/file")
                || uri.startsWith(contextPath + "/css")
                || uri.startsWith(contextPath + "/js")
                || uri.startsWith(contextPath + "/actuator/")
                || uri.startsWith(contextPath + "/webjars/")
                || uri.startsWith(contextPath + "/vendor");

        log.info("context: {}, path: {}:  shouldNotFilter: {}", contextPath, uri, rs);
        return rs;
    }

}