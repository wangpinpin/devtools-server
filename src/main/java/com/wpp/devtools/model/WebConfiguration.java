package com.wpp.devtools.model;


import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-07-06
 **/

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    //spring拦截器加载在springcontentText之前，所以这里用@Bean提前加载。否则会导致过滤器中的@AutoWired注入为空

    @Bean
    FilterInterceptor filternterceptor() {
        return new FilterInterceptor();
    }

    @Bean
    JWTInterceptor jwtInterceptor() {
        return new JWTInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(filternterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**",
                        "/swagger-ui.html/**");

        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/user/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**",
                        "/swagger-ui.html/**")
                .excludePathPatterns(Arrays.asList("/unAuth/**", "/devTools/**"));


    }

}