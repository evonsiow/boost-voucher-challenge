package com.boost.module.recipient.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.SharedCacheMode;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.boost.module.recipient.db",
        entityManagerFactoryRef = "recipientEntityManagerFactory",
        transactionManagerRef = "recipientTransactionManager"
)
@EntityScan(basePackages = "com.boost.module.recipient.db.entity")
public class RecipientDataSourceConfig {
    public static final String RECIPIENT_JDBC_TEMPLATE = "RECIPIENT_JDBC_TEMPLATE";
    private static final String ENTITY_PACKAGE = "com.boost.module.recipient.db.entity";

    @Value("${recipient.db.dialect}")
    private String hibernateDialect;

    @Value("${recipient.db.ddl-auto}")
    private String hibernateHbm2ddlAuto;

    private final Environment environment;

    public RecipientDataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Primary
    @Bean("recipientDataSource")
    public DataSource recipientDataSource(
            @Value("${recipient.db.datasource.url}") final String url,
            @Value("${recipient.db.datasource.username}") final String username,
            @Value("${recipient.db.datasource.driver.class.name}") final String driverClassName,
            @Value("${recipient.db.datasource.minIdle:10}") final int minIdle,
            @Value("${recipient.db.datasource.max.pool.size:10}") final int maxPoolSize) {

        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setMaximumPoolSize(maxPoolSize);
        dataSourceConfig.setJdbcUrl(url);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(environment.getProperty("recipient.db.datasource.password"));
        dataSourceConfig.setMinimumIdle(minIdle);
        dataSourceConfig.setDriverClassName(driverClassName);

        return new HikariDataSource(dataSourceConfig);
    }

    @Primary
    @Bean("recipientEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean recipientEntityManagerFactory(
            @Qualifier("recipientDataSource") final DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("BOOST.RECIPIENT.DB");
        em.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPackagesToScan(ENTITY_PACKAGE);
        em.setDataSource(dataSource);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", hibernateDialect);
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        em.setJpaProperties(jpaProperties);

        return em;
    }

    @Primary
    @Bean("recipientTransactionManager")
    public PlatformTransactionManager recipientTransactionManager(
            @Qualifier("recipientEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }

    @Bean(RECIPIENT_JDBC_TEMPLATE)
    public JdbcTemplate jdbcTemplate(@Qualifier("recipientDataSource") final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
