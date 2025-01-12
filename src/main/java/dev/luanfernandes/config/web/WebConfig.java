package dev.luanfernandes.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String swaggerUrl = "/swagger-ui/index.html";
        registry.addRedirectViewController("/", swaggerUrl);
        registry.addRedirectViewController("/docs", swaggerUrl);
        registry.addRedirectViewController("/swagger", swaggerUrl);
        registry.addRedirectViewController("/swagger-ui", swaggerUrl);
    }
}
