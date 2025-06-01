package com.boost.module.voucher.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class VoucherDataSourceConfig {
    @Value("${voucher.db.datasource.url}")
    private String url;

    @Value("${voucher.db.datasource.username}")
    private String username;

    @Value("${voucher.db.datasource.password}")
    private String password;

    @Value("${voucher.db.datasource.driver.class.name}")
    private String driverClassName;

    @Bean
    public DataSource voucherDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setPoolName("VoucherServiceHikariCP");

        return new HikariDataSource(hikariConfig);
    }
}
