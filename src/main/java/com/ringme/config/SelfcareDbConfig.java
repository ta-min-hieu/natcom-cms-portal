package com.ringme.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "selfcareEntityManagerFactory",
        transactionManagerRef = "selfcareTransactionManager",
        basePackages = {"com.ringme.repository.sys", "com.ringme.repository.selfcare", "com.ringme.repository.loyalty", "com.ringme.repository.voucher"})
public class SelfcareDbConfig {
    @Bean(name = "selfcareDataSourceProperties")
    @ConfigurationProperties("spring.selfcare-datasource")
    public DataSourceProperties selfcareDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "selfcareDataSource")
    @ConfigurationProperties("spring.selfcare-datasource.configuration")
    public DataSource selfcareDataSource(@Qualifier("selfcareDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "selfcareEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean selfcareEntityManagerFactory(
            EntityManagerFactoryBuilder entityManagerFactoryBuilder,
            @Qualifier("selfcareDataSource") DataSource dataSource) {

        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        jpaProperties.put("hibernate.dialect.storage_engine", "innodb");
        jpaProperties.put("hibernate.hbm2ddl.auto", "none");

        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.ringme.model.sys", "com.ringme.model.selfcare", "com.ringme.model.loyalty", "com.ringme.model.voucher")
                .persistenceUnit("selfcareDataSource")
                .properties(jpaProperties)
                .build();
    }

    @Bean(name = "selfcareTransactionManager")
    public PlatformTransactionManager selfcareTransactionManager(
            @Qualifier("selfcareEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }

    @Primary
    @Bean(name = "selfcareJdbcTemplate")
    public NamedParameterJdbcTemplate selfcareJdbcTemplate(
            @Qualifier("selfcareDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
