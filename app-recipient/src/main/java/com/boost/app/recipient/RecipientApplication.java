package com.boost.app.recipient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.boost.app.recipient", "com.boost.module.recipient"})
@OpenAPIDefinition(info = @Info(title = "Recipient MS Rest API"))
public class RecipientApplication {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RecipientApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RecipientApplication.class, args);
        String port = context.getEnvironment().getProperty("server.port");
        String apiContextPath = context.getEnvironment().getProperty("server.servlet.context-path");

        LOGGER.info("Recipient MS Rest API Swagger UI: http://localhost:{}{}/swagger-ui/index.html", port, apiContextPath);
    }
}
