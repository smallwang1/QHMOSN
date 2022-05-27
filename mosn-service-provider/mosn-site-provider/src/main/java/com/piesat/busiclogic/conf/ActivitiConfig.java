//package com.piesat.busiclogic.conf;
//
//import org.activiti.engine.*;
//import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.MultipartConfigFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.MultipartConfigElement;
//import javax.sql.DataSource;
//
///**
// * @ClassName ActivitiConfig
// * @Descripton activiti 服务管理器
// * @Author sjc
// * @Date 2020/2/11 16:25
// **/
//@Configuration
//public class ActivitiConfig {
//
//    @Autowired
//    private DataSource dataSource;
//
//    // 流程配置管理
//    @Bean
//    public ProcessEngineConfiguration processEngineConfiguration(){
//        ProcessEngineConfiguration pec = new StandaloneInMemProcessEngineConfiguration();
//        pec.setDataSource(dataSource);
//        pec.setDatabaseSchemaUpdate("true");
//        return pec;
//    }
//
//    // 流程引擎
//    @Bean
//    public ProcessEngine processEngine(){
//       return  processEngineConfiguration().buildProcessEngine();
//    }
//
//    // 资源管理
//    @Bean
//    public RepositoryService repositoryService(){
//        return processEngine().getRepositoryService();
//    }
//
//    //运行管理
//    @Bean
//    public RuntimeService runtimeService(){
//        return processEngine().getRuntimeService();
//    }
//
//    // 任务管理
//    @Bean
//    public TaskService taskService() {
//        return processEngine().getTaskService();
//    }
//
//    // 历史管理
//    @Bean
//    public HistoryService historyService(){
//        return processEngine().getHistoryService();
//    }
//
//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        //  单个数据大小
//        factory.setMaxFileSize("10240KB"); // KB,MB
//        /// 总上传数据大小
//        factory.setMaxRequestSize("102400KB");
//        return factory.createMultipartConfig();
//    }
//}
