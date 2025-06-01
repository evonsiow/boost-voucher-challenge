package com.boost.module.voucher.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.SharedCacheMode;

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

    @Value("${voucher.db.dialect}")
    private String hibernateDialect;

    @Value("${voucher.db.ddl-auto}")
    private String hibernateHbm2ddlAuto;

    private final Environment environment;

    public VoucherDataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean("voucherDataSource")
    public DataSource voucherDataSource(
            @Value("${voucher.db.datasource.url}") String url,
            @Value("${voucher.db.datasource.username}") String username,
            @Value("${voucher.db.datasource.driver.class.name}") String driverClassName,
            @Value("${voucher.db.datasource.minIdle:10}") int minIdle,
            @Value("${voucher.db.datasource.max.pool.size:10}") int maxPoolSize) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(environment.getProperty("voucher.db.datasource.password"));
        config.setDriverClassName(driverClassName);
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxPoolSize);

        return new HikariDataSource(config);
    }

    @Bean("voucherEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean voucherEntityManagerFactory(
            @Qualifier("voucherDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPersistenceUnitName("BOOST.VOUCHER.DB");
        factoryBean.setPackagesToScan("com.boost.module.voucher.db.entity");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaDialect(new HibernateJpaDialect());
        factoryBean.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        factoryBean.setJpaProperties(additionalProperties());

        return factoryBean;
    }

    private Properties additionalProperties() {
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", hibernateDialect);
        props.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        return props;
    }

    @Bean("voucherTransactionManager")
    public PlatformTransactionManager voucherTransactionManager(
            @Qualifier("voucherEntityManagerFactory") LocalContainerEntityManagerFactoryBean factoryBean) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(factoryBean.getObject());
        return txManager;
    }

    @Bean(VOUCHER_JDBC_TEMPLATE)
    public JdbcTemplate jdbcTemplate(@Qualifier("voucherDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
