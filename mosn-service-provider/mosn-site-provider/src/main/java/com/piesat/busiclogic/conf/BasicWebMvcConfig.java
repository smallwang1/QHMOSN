package com.piesat.busiclogic.conf;


import com.piesat.common.config.PcObjectMapper;
import com.piesat.common.core.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@EnableWebMvc
public class BasicWebMvcConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor vueViewInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/",
                        "classpath:/resources/",
                        "classpath:/static/","classpath:/static/js/",
                        "classpath:/template/pageoffice/");
    }

    /**
     * Add interceptors.
     *
     * @param registry the registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(vueViewInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "*.js", "/**/*.js", "/**/*.exe","/**/jyInfo/**", "/**/file/**", "/**/dataSet/**","/**/*.js","/**/actv/**", "*.css","/**/*.zz", "/**/*.css", "*.html", "/**/*.html", "/**/noAuth/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        PcObjectMapper
                .buidMvcMessageConverter(converters);
    }

}
