//package com.piesat.quartz.config;
//
//import com.piesat.common.config.properties.MdesCloudProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//
//import javax.annotation.Resource;
//
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//    @Resource
//    private MdesCloudProperties mdesCloudProperties;
//
//
//    /**
//     * 一些链接配置全部在这里，过滤
//     *
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
//        mdesCloudProperties.getSecurity().getOauth2().getIgnore().getUrls().forEach(url -> registry.antMatchers(url).permitAll());
//        registry.anyRequest().authenticated()
//                .and()
//                .csrf().disable();
//        http.headers().frameOptions().disable();
//
//    }
//}
