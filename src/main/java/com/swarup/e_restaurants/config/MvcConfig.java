package com.swarup.e_restaurants.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("website/index");
        registry.addViewController("/index").setViewName("website/index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/logout").setViewName("login");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/403").setViewName("403");
        registry.addViewController("/home").setViewName("admin/home");
        registry.addViewController("/restrurent").setViewName("admin/restrurent");
        registry.addViewController("/city").setViewName("admin/city");
        registry.addViewController("/location").setViewName("admin/location");


       
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
    }

}
