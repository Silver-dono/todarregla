package org.todarregla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.todarregla.configuration.AppConfiguration;

import java.util.HashMap;

@SpringBootApplication
public class TodarreglaApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AppConfiguration.class);
        application.setDefaultProperties(new HashMap<String, Object>() {{
            put("spring.main.allow-bean-definition-overriding", true);
        }});
        application.run();
    }
}