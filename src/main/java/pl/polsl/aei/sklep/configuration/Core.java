package pl.polsl.aei.sklep.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
@EnableSpringDataWebSupport
public class Core extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/");
    }

    @Bean
    public ThreadLocal<DateFormat> getSimpleDateFormat() {
        return ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd hh:mm"));
    }
}
