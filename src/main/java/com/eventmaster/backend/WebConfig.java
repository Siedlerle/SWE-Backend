package com.eventmaster.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/event_images/**").addResourceLocations("file:" + "src/main/upload/event_images/");
        registry.addResourceHandler("/preset_images/**").addResourceLocations("file:" + "src/main/upload/preset_images/");
        registry.addResourceHandler("/orga_images/**").addResourceLocations("file:" + "src/main/upload/orga_images/");
    }
}
