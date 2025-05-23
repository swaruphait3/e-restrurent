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
        registry.addViewController("/admin").setViewName("admin/home");
        registry.addViewController("/rest").setViewName("admin/restrurent");
        registry.addViewController("/city").setViewName("admin/city");
        registry.addViewController("/location").setViewName("admin/location");
        registry.addViewController("/delivary_boy").setViewName("admin/delivaryboy");

        registry.addViewController("/restrurent").setViewName("restrurent/orderplace");
        registry.addViewController("/branch").setViewName("restrurent/branch");
        registry.addViewController("/foodtype").setViewName("restrurent/foodtype");
        registry.addViewController("/food").setViewName("restrurent/food");
        registry.addViewController("/orderplace").setViewName("restrurent/orderplace");
        registry.addViewController("/sales_report").setViewName("restrurent/report");
                registry.addViewController("/dboy").setViewName("delivary/delivaryorder");

        registry.addViewController("/home").setViewName("user/home");
        registry.addViewController("/restitem").setViewName("user/restitem");
        registry.addViewController("/orederitem").setViewName("user/orederitem");
        registry.addViewController("/orderlist").setViewName("user/orderlist");





       
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
    }
}
