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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "myNatcomEntityManagerFactory", transactionManagerRef = "myNatcomTransactionManager", basePackages = {
        "com.ringme.repository.mynatcom"})
public class MyNatComDBConfig {
    // ============ MY_NATCOM ============
    @Primary
    @Bean(name = "myNatcomDataSourceProperties")
    @ConfigurationProperties("oracle.datasource.my-natcom")
    public DataSourceProperties myNatcomDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "myNatcomDataSource")
    @ConfigurationProperties("oracle.datasource.my-natcom.configuration")
    public DataSource myNatcomDataSource(@Qualifier("myNatcomDataSourceProperties") DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        dataSource.setConnectionInitSql("ALTER SESSION SET CURRENT_SCHEMA = MY_NATCOM");
        return dataSource;
    }

    @Primary
    @Bean(name = "myNatcomTransactionManager")
    public PlatformTransactionManager myNatcomTransactionManager(
            @Qualifier("myNatcomDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "myNatcomEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder entityManagerFactoryBuilder,
            @Qualifier("myNatcomDataSource") DataSource dataSource) {

        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
        //properties.put("hibernate.dialect.storage_engine", "innodb");
        //properties.put("spring.jpa.properties.hibernate.dialect.storage_engine", "innodb");
        // spring.jpa.properties.hibernate.dialect.storage_engine=innodb
        properties.put("hibernate.hbm2ddl.auto", "none");

        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.ringme.model.mynatcom")
                .persistenceUnit("myNatcomDataSource")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "myNatcomJdbcTemplate")
    public NamedParameterJdbcTemplate myNatcomJdbcTemplate(
            @Qualifier("myNatcomDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
