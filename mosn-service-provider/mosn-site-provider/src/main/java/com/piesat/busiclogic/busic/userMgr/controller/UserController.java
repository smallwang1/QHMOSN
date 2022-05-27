package com.piesat.busiclogic.busic.userMgr.controller;

import com.piesat.busiclogic.busic.userMgr.entity.UserInfo;
import com.piesat.busiclogic.busic.userMgr.service.UserService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/useMgr")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Description("用户列表信息查询")
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public Wrapper getUserList(@RequestParam long currentPage, @RequestParam int pageSize, UserInfo userInfo) {
        return this.handleResult(userService.getUserData(currentPage, pageSize,userInfo));
    }

    @Description("增加用户信息")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Wrapper addUser(UserInfo userInfo) {
        try {
            userService.addUser(userInfo);
            return WrapMapper.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return  WrapMapper.error();
        }
    }

    @Description("重置密码")
    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
    public Wrapper resetPwd(UserInfo userInfo) {
        userService.resetPwd(userInfo);
        return WrapMapper.ok();
    }


    @Description("删除用户信息")
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public Wrapper deleteUser(UserInfo userInfo) {
        userService.deleteUser(userInfo);
        return WrapMapper.ok();
    }

    @Description("编辑用户信息")
    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public Wrapper editUser(UserInfo userInfo) {
        try {
            userService.editUser(userInfo);
            return WrapMapper.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return  WrapMapper.error();
        }
    }


    @Description("更改用户状态")
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public Wrapper changeStatus(UserInfo userInfo) {
            userService.changeStatus(userInfo);
            return WrapMapper.ok();
    }


    @Description("由ID查用户信息")
    @RequestMapping(value = "/getUserById", method = RequestMethod.POST)
    public Wrapper getUserById(UserInfo userInfo) {
        return this.handleResult( userService.getUserById(userInfo));
    }

    @Description("获取组织树结构")
    @RequestMapping(value = "/getOrganTree", method = RequestMethod.GET)
    public Wrapper getOrganTreeData(HttpServletRequest request) {
        List<Map<String, Object>> list = userService.getOrganTreeData();
        return WrapMapper.ok(list);
    }
}
