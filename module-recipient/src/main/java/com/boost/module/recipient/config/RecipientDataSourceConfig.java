package com.boost.module.recipient.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RecipientDataSourceConfig {
    @Value("${recipient.db.datasource.url}")
    private String url;

    @Value("${recipient.db.datasource.username}")
    private String username;

    @Value("${recipient.db.datasource.password}")
    private String password;

    @Value("${recipient.db.datasource.driver.class.name}")
    private String driverClassName;

    @Bean
    public DataSource recipientDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setPoolName("RecipientServiceHikariCP");

        return new HikariDataSource(hikariConfig);
    }
}
