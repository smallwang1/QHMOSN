package com.piesat.busiclogic.busic.noauth;

import com.piesat.auth.api.service.AuthTaskFeignApi;
import com.piesat.common.anno.Description;
import com.piesat.common.config.properties.MdesCloudProperties;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录，退出接口
 *
 * @Author Thomas 2021/1/26 10:25
 * The world of programs is a wonderful world
 */
@Controller
public class NoAuthController extends BaseController {

    @Autowired
    private AuthTaskFeignApi authTaskFeignApi;

    @Autowired
    private MdesCloudProperties mdesCloudProperties;

    @Description("验证应用信息，登录，生成ticket 地址")
    @RequestMapping(value = "/api/file/siteLogin", method = RequestMethod.GET)
    @ResponseBody
    public Wrapper verifyApp(HttpServletRequest request) {
        return authTaskFeignApi.verifyApp(mdesCloudProperties.getProvider().getAppId());
    }

    @Description("退出,生成登录地址")
    @RequestMapping(value = "/file/verifyApp", method = RequestMethod.GET)
    @ResponseBody
    public Wrapper siteLogin() {
        return authTaskFeignApi.verifyAppGetLoginUrl(mdesCloudProperties.getProvider().getAppId());
    }

    @Description("移动端app 登录")
    @RequestMapping(value = "/api/file/verifyMobileAppLogin", method = RequestMethod.GET)
    @ResponseBody
    public Wrapper verifyMobileApp(HttpServletRequest request) {
        //appId 移动端appId 功能
        return authTaskFeignApi.verifyApp("vaFFFY1K1615428117987");
    }

    @Description("移动端app退出登录")
    @RequestMapping(value = "/api/file/mobileAppLogout", method = RequestMethod.GET)
    @ResponseBody
    public Wrapper mobileAppLogout(HttpServletRequest request) {
        return authTaskFeignApi.verifyAppGetLoginUrl("vaFFFY1K1615428117987");
    }
}
