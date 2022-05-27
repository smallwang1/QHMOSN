/**
 * 类名称: TestController.java
 * 包名称: com.piesat.busiclogic.conf
 * <p>
 * 修改履历:
 * 日期       2020/1/15 10:48
 * 修正者     Thomas
 * 主要内容   初版做成
 * <p>
 */
package com.piesat.busiclogic.conf;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Thomas 2020/1/15 10:48
 * The world of programs is a wonderful world
 */
@RestController
public class TestController {

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String get(){
        return "nihao";
    }

    @RequestMapping(value = "/test/noAuth",method = RequestMethod.GET)
    public String noAuth(){
        return "noAuth";
    }
}
