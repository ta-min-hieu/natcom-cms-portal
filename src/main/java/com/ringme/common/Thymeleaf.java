package com.ringme.common;

import com.ringme.config.AppConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Thymeleaf {
    @Autowired
    AppConfig appConfig;

    private static String domainCdn;

    @PostConstruct
    public void init() {
        domainCdn = appConfig.getDomainCdn();
    }

    public static List<Object> arrays(Object... args) {
        return Arrays.asList(args);
    }

    public static String formatNumber(Double input) {
        if(input == null)
            return "0";

        return Helper.formatNumber(input);
    }

    public String mediaUrl(String input) {
        if(input == null || input.isEmpty())
            return "";

        if(input.startsWith("http"))
            return input;

        return domainCdn + input;
    }
}
