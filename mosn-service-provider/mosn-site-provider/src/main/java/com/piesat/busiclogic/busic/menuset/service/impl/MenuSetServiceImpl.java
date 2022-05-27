package com.piesat.busiclogic.busic.menuset.service.impl;

import com.piesat.busiclogic.busic.menuset.dao.MenuSetDao;
import com.piesat.busiclogic.busic.menuset.service.MenuSetService;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.util.PublicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ShaoJinchao
 * @date 2020/7/30
 */
@Service
public class MenuSetServiceImpl implements MenuSetService {

    @Autowired
    private MenuSetDao menuSetDao;

    @Override
    public List<Map<String, Object>> getMenuSetData(String code, LoginAuthDto loginAuthDto) throws Exception {
        List<Map<String,Object>> Resultlist = menuSetDao.getMenuSetByCode(code,"");
        Map<String,Object> rootId = menuSetDao.getRootIdByCode(code);
        Resultlist = Misc.recursion(String.valueOf(rootId.get("ID")),Resultlist);
        return Resultlist;
    }

    @Override
    public List<Map<String, Object>> getMenuSetProData(String code, LoginAuthDto loginAuthDto) throws Exception {
        List<String> roleIdList = new ArrayList<>();
        if(PublicUtil.isEmpty(loginAuthDto)){// 如果用户是处于非登录状态，赋予游客角色
            roleIdList.add(Misc.getPropValue("transparent.properties","visitor"));
        }else{  // 查询用户所具有得所有角色
            roleIdList = menuSetDao.getRoleList(String.valueOf(loginAuthDto.getUserId()));
            if (PublicUtil.isEmpty(roleIdList)){ // 如果用户未赋予任何角色赋予普通用户角色
                roleIdList.add(Misc.getPropValue("transparent.properties","nonal"));
            }
        }
        List<Map<String,Object>> Resultlist = menuSetDao.getMenuSetProByCode(code,String.join("','",roleIdList));
        Map<String,Object> rootId = menuSetDao.getRootIdByCode(code);
        Resultlist = Misc.recursion(String.valueOf(rootId.get("ID")),Resultlist);
        return Resultlist;
    }

    @Override
    public List<Map<String, Object>> getMenuSetDataByTree(String code, LoginAuthDto loginAuthDto) throws Exception{
        List<String> roleIdList = new ArrayList<>();
        if(PublicUtil.isEmpty(loginAuthDto)){// 如果用户是处于非登录状态，赋予游客角色
            roleIdList.add(Misc.getPropValue("transparent.properties","visitor"));
        }else{  // 查询登录用户所具有得所有角色
            roleIdList = menuSetDao.getRoleList(String.valueOf(loginAuthDto.getUserId()));
            if (PublicUtil.isEmpty(roleIdList)){ // 如果用户未赋予任何角色赋予普通用户角色
                roleIdList.add(Misc.getPropValue("transparent.properties","nonal"));
            }
        }
        List<Map<String,Object>> Resultlist = menuSetDao.getMenuSetProByCode(code,String.join("','",roleIdList));
        Resultlist = Misc.recursion("R",Resultlist);
        return Resultlist;
    }

    @Override
    public List<Map<String, Object>> getCodeData(String code) {
        return menuSetDao.getCodeData(code);
    }

    @Override
    public List<Map<String, Object>> getMenuDataNoRule() {
        List<Map<String,Object>> Resultlist = menuSetDao.getMenuSetNoRule();
        try {
            Resultlist = Misc.recursion("R",Resultlist);
            return Resultlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
