package com.piesat.busiclogic.busic.warninfo.task;

import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.util.PublicUtil;
import com.piesat.common.util.RedisUtils;
import com.piesat.jdbc.holder.SpringContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @Descripton 定时任务每分钟调度缓存预警数据
 * @Author sjc
 * @Date 2020/3/10
 **/

@Configuration
@EnableScheduling
public class WarnTask{
    /**
     * 当前预警接口名称
     */
    private static String nowWarnInterfaceName= "SWP.GetAlmtDataByStationCodes";

    private RedisUtils redisUtils=  (RedisUtils)SpringContextHolder.getApplicationContext().getBean("redisUtils");

    @Scheduled(cron = "0 */5 * * * ?")
    protected void execute() {

        String iquery = nowWarnInterfaceName+"|1|String;all";
        String resultData = "";
        try {
            URL url = new URI(Misc.getPropValue("transparent.properties","warn_url") + "iquery="+ URLEncoder.encode(iquery,"utf-8")).toURL();
            resultData   = PublicUtil.getResultData(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisUtils.set("warndata",String.valueOf(resultData));
    }
}
