package com.scii.config;

import java.util.Collections;
import java.util.Locale;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.JspConfigDescriptorImpl;
import org.apache.tomcat.util.descriptor.web.JspPropertyGroup;
import org.apache.tomcat.util.descriptor.web.JspPropertyGroupDescriptorImpl;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MessageSourceConfig {
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("/WEB-INF/i18n/labels/label", 
								  "/WEB-INF/i18n/buttons/button", 
								  "/WEB-INF/i18n/messages/message");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
	@Bean 
    public ConfigurableServletWebServerFactory configurableServletWebServerFactory ( ) { 
        return (ConfigurableServletWebServerFactory) new TomcatServletWebServerFactory() { 
            @Override 
            protected void postProcessContext(Context context) { 
                super.postProcessContext(context); 
                JspPropertyGroup jspPropertyGroup = new JspPropertyGroup(); 
                jspPropertyGroup.addUrlPattern("*.jsp");
                jspPropertyGroup.setElIgnored("false");
                jspPropertyGroup.setPageEncoding("UTF-8");
                jspPropertyGroup.setScriptingInvalid("true");
                jspPropertyGroup.addIncludePrelude("/WEB-INF/jsp/common/include.jsp");
                JspPropertyGroupDescriptorImpl jspPropertyGroupDescriptor = new JspPropertyGroupDescriptorImpl(jspPropertyGroup); 
                context.setJspConfigDescriptor(new JspConfigDescriptorImpl(Collections.singletonList(jspPropertyGroupDescriptor), Collections.emptyList())); 
            } 
        }; 
    }
	
	 @Bean
	 public SessionLocaleResolver localeResolver() {
	    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
	    localeResolver.setDefaultLocale(Locale.ENGLISH);
	    return localeResolver;
	 }

	 @Bean
	 public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	    localeChangeInterceptor.setParamName("lang");
	    return localeChangeInterceptor;
	 }
}
