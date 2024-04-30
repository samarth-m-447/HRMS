package com.scii.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdditionalResourceWebConfiguration implements WebMvcConfigurer  {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	registry
	        .addResourceHandler("/externalImages/**")
	        .addResourceLocations("file:D:/HRMS/Images/")
	        .setCachePeriod(0);
	}
}
