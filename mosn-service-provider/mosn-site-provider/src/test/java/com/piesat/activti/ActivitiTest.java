package com.piesat.activti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试类
 *     作用：测试activiti
 */


public class ActivitiTest {



//    @Test
//    public void testGenTable(){
//        //条件：1.activiti配置文件名称：activiti.cfg.xml   2.bean的id="processEngineConfiguration"
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        System.out.println(processEngine);
//
//       // HistoryService historyService = processEngine.getHistoryService();
//
//    }

//    @Test
//    public void testGenTable(){
//        //1.创建ProcessEngineConfiguration对象  第一个参数:配置文件名称  第二个参数是processEngineConfiguration的bean的id
//        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
//                .createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
//        //2.创建ProcesEngine对象
//        ProcessEngine processEngine = configuration.buildProcessEngine();
//
//        //3.输出processEngine对象
//        System.out.println(processEngine);
//
//    }


    // 部署bpmn
    @Test
    public void testDeployment (){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");

        //2.创建ProcesEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        //部署对象
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("diagram/issueandfilein.bpmn")// bpmn文件
                .addClasspathResource("diagram/issueandfilein.png")// 图片文件
//              .name("文件下发流程")
                .deploy();
        System.out.println("流程部署id:" + deployment.getId());
        System.out.println("流程部署名称:" + deployment.getName());
    }

    // 启动一个流程实例
    @Test
    public void startProcessInstance() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
        //2.创建ProcesEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
     // 获取RunTimeService
        RuntimeService runtimeService =
                processEngine.getRuntimeService();
        // 根据流程定义key启动流程
//           ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday","1262");
        Map<String,Object> params  = new HashMap<String,Object>();
        params.put("assigner","tom");
            ProcessInstance processInstance = runtimeService.startProcessInstanceById("dataapply:1:52504","12306",params);
            System.out.println(" 流程定义 id ： " +
                    processInstance.getProcessDefinitionId());
            System.out.println("流程实例id：" + processInstance.getId());
            System.out.println(" 当前活动 Id ： " +
                    processInstance.getActivityId());
    }

    // 查询当前个人待执行的任务
//    @Test
    public void findPersonalTaskList() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
        //2.创建ProcesEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        // 任务负责人
        String assignee = "tom";
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()//
                .processDefinitionKey("holiday")//
                .taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        for (Task task : list) {
            System.out.println(" 流 程 实 例 id ： " +
                    task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
            }
        }
        // 完成任务
//        @Test
        public void completTask() {
            ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                    .createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
            //2.创建ProcesEngine对象
            ProcessEngine processEngine = configuration.buildProcessEngine();
            //任务id
            String taskId = "7505";
             // 创建TaskService
            TaskService taskService = processEngine.getTaskService();
            //完成任务
            taskService.complete(taskId);
            System.out.println("完成任务id="+taskId);
    }
    @Test
    public void testAttach(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
        //2.创建ProcesEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        taskService.createAttachment(".doc","65069","65064","数据接入附件","cehsi","D:\\template\\temp2.doc");


    }
}
