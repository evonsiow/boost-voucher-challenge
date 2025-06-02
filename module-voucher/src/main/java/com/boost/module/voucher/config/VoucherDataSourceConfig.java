package com.boost.module.voucher.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.boost.module.voucher.db",
        entityManagerFactoryRef = "voucherEntityManagerFactory",
        transactionManagerRef = "voucherTransactionManager"
)
@EntityScan(basePackages = "com.boost.module.voucher.db.entity")
public class VoucherDataSourceConfig {

    public static final String VOUCHER_JDBC_TEMPLATE = "VOUCHER_JDBC_TEMPLATE";
    private static final String ENTITY_PACKAGE = "com.boost.module.voucher.db.entity";

    @Value("${voucher.db.dialect}")
    private String hibernateDialect;

    @Value("${voucher.db.ddl-auto}")
    private String hibernateHbm2ddlAuto;

    @Bean("voucherDataSource")
    public DataSource voucherDataSource(
            @Value("${voucher.db.datasource.url}") String url,
            @Value("${voucher.db.datasource.username}") String username,
            @Value("${voucher.db.datasource.password}") String password,
            @Value("${voucher.db.datasource.driver.class.name}") String driverClassName) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        return new HikariDataSource(config);
    }

    @Bean("voucherEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean voucherEntityManagerFactory(
            @Qualifier("voucherDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPersistenceUnitName("BOOST.VOUCHER.DB");
        factoryBean.setPackagesToScan(ENTITY_PACKAGE);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", hibernateDialect);
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        factoryBean.setJpaProperties(jpaProperties);

        return factoryBean;
    }

    @Bean("voucherTransactionManager")
    public PlatformTransactionManager voucherTransactionManager(
            @Qualifier("voucherEntityManagerFactory") LocalContainerEntityManagerFactoryBean factoryBean) {
        return new JpaTransactionManager(factoryBean.getObject());
    }

    @Bean(VOUCHER_JDBC_TEMPLATE)
    public JdbcTemplate jdbcTemplate(@Qualifier("voucherDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
