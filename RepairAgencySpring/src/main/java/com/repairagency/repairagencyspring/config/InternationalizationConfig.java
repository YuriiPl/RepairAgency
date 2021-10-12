package com.repairagency.repairagencyspring.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class InternationalizationConfig {

    @Bean
    public ResourceBundleMessageSource messageSource(){
        ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
        bundleMessageSource.setBasenames("i18n");
        bundleMessageSource.setDefaultEncoding("UTF-8");
        return bundleMessageSource;
    }

    @Bean
    public LocaleResolver localeResolver(){
        CookieLocaleResolver lr = new CookieLocaleResolver();
        lr.setCookieName("lang");
        lr.setDefaultLocale(Locale.ENGLISH);
        return lr;
    }

}
