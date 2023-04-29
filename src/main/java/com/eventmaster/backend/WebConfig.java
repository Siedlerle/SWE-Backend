package com.eventmaster.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
          registry.addResourceHandler("/eventimages/**")
                  .addResourceLocations("classpath:/eventimages/");
    }
}
*/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + "src/main/upload/"); //+ System.getProperty("user.dir") "file:" + "src/main/upload/"  "classpath:/upload/"
      //  WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}

//"/upload/**"
//"file:" + "src/main/upload/"