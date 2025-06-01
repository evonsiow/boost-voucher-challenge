package com.boost.app.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.boost.app.user", "com.boost.module.user"})
@OpenAPIDefinition(info = @Info(title = "User MS Rest API"))
public class UserApplication {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(UserApplication.class, args);
        String port = context.getEnvironment().getProperty("server.port");
        String apiContextPath = context.getEnvironment().getProperty("server.servlet.context-path");

        LOGGER.info("User MS Rest API Swagger UI: http://localhost:{}{}/swagger-ui/index.html", port, apiContextPath);
    }
}
