package com.boost.app.voucher;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.boost.app.voucher", "com.boost.module.voucher", "com.boost.module.core"})
@OpenAPIDefinition(info = @Info(title = "Voucher MS Rest API"))
public class VoucherApplication {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(VoucherApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(VoucherApplication.class, args);

        String port = context.getEnvironment().getProperty("server.port");
        String apiContextPath = context.getEnvironment().getProperty("server.servlet.context-path");

        LOGGER.info("Voucher MS Rest API Swagger UI: http://localhost:{}{}/swagger-ui/index.html", port, apiContextPath);
    }
}
