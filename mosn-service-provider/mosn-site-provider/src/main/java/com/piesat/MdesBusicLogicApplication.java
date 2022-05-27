/**
 * 项目名称: trunk
 * 类名称: MdesBusicLogicApplication.java
 * 包名称: com.piesat.busiclogic
 * <p>
 * 修改履历:
 * 日期       2020/1/15 11:34
 * 修正者     Thomas
 * 主要内容   初版做成
 * <p>
 */

package com.piesat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 外网平台后台服务地址
 * @Author Thomas 2020/1/15 11:34
 * The world of programs is a wonderful world
 */
@EnableCircuitBreaker
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableEurekaClient
@EnableFeignClients
@MapperScan({"com.piesat.site", "com.piesat.site.datasearch.service.mapper", "com.piesat.site.datalist.service.mapper", "com.piesat.quartz.mapper", "com.piesat.busiclogic.busic.statistics.service.mapper"})
@ComponentScan(basePackages = {"com.piesat.busiclogic","com.piesat.site","com.piesat.jdbc","com.piesat"})
@EnableTransactionManagement
public class MdesBusicLogicApplication {


	public static void main(String[] args) {

		SpringApplication.run(MdesBusicLogicApplication.class, args);
    }

	@Bean
	public TomcatServletWebServerFactory tomcatServletWebServerFactory (){
	   // 修改内置的 tomcat 容器配置
	   TomcatServletWebServerFactory tomcatServlet = new TomcatServletWebServerFactory();
	   tomcatServlet .addConnectorCustomizers(
	      (TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "[]{}()")
	   );
	   return tomcatServlet ;
	}

	public class ServletInitializer extends SpringBootServletInitializer {
		@Override
		protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
			return builder.sources(MdesBusicLogicApplication.class);
		}
	}
}
