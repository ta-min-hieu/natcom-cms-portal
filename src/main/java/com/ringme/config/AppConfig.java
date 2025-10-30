package com.ringme.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${cdn.domain.url}")
    private String domainCdn;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    @Value("${cms.file.in.db.prefix}")
    private String fileInDBPrefix;

    @Value("${cms.file.store.root-path}")
    private String rootPath;
}
