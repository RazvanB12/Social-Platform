package com.disi.social_platform_be.util;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
@Configuration
@Data
public class InterceptorConfig implements WebMvcConfigurer {
    private static final List<String> WHITE_LIST = List.of("/login", "/register",
            "/reset-password", "/forgot-password/**");

    private final AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .excludePathPatterns(WHITE_LIST);
    }

    @Bean
    @RequestScope
    public TokenHolder operatorHolder() {
        return new TokenHolder();
    }
}
