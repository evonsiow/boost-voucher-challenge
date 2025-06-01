package com.boost.module.user.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class UserDataSourceConfig {
    @Value("${user.db.datasource.url}")
    private String url;

    @Value("${user.db.datasource.username}")
    private String username;

    @Value("${user.db.datasource.password}")
    private String password;

    @Value("${user.db.datasource.driver.class.name}")
    private String driverClassName;

    @Bean
    public DataSource userDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setPoolName("UserServiceHikariCP");

        return new HikariDataSource(hikariConfig);
    }
}
