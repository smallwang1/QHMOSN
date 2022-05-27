/**
 * 类名称: WebSecurityConfig.java
 * 包名称: com.piesat.busiclogic.conf
 * <p>
 * 修改履历:
 * 日期       2020/1/15 0:24
 * 修正者     Thomas
 * 主要内容   初版做成
 * <p>
 */
package com.piesat.busiclogic.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * 静态资源忽略
 * @Author Thomas 2020/1/15 0:24
 * The world of programs is a wonderful world
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**/**", "*.js", "/**/*.js","/swagger-resources/**","/**/office/**","/**/*.exe","/**/readFile/**", "/**/pageoffice/**","/**/jyInfo/**","*.js","/*.js", "/**/*.js", "*.css", "/**/*.css", "/**/*.zz", "*.html", "/**/*.html", "/**/noAuth/**");
    }
}
