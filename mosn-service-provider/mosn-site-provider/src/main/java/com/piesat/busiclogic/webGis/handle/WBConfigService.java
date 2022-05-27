package com.piesat.busiclogic.webGis.handle;

import com.piesat.busiclogic.busic.menuset.dao.MenuSetDao;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.webGis.dao.WBConfigDao;
import com.piesat.busiclogic.webGis.entity.PlaneConfig;
import com.piesat.busiclogic.webGis.entity.TableConfig;
import com.piesat.busiclogic.webGis.entity.WebGisConfig;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.util.PublicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WBConfigService {

    @Autowired
    private MenuSetDao menuSetDao;

    @Autowired
    private WBConfigDao wbConfigDao;

    public List<Map<String,Object>> getPointTree() {
        List<Map<String,Object>> mapList = wbConfigDao.getPointTree();
        try {
            mapList = Misc.recursion("0",mapList);
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String,Object>> getPointTreeLogin(LoginAuthDto loginAuthDto) throws Exception {

        List<String> roleIdList = new ArrayList<>();
        if(PublicUtil.isEmpty(loginAuthDto)){// 如果用户是处于非登录状态，赋予游客角色
            roleIdList.add(Misc.getPropValue("transparent.properties","visitor"));
        }else{  // 查询用户所具有得所有角色
            roleIdList = menuSetDao.getRoleList(String.valueOf(loginAuthDto.getUserId()));
            if (PublicUtil.isEmpty(roleIdList)){ // 如果用户未赋予任何角色赋予普通用户角色
                roleIdList.add(Misc.getPropValue("transparent.properties","nonal"));
            }
        }
        List<Map<String,Object>> mapList = wbConfigDao.getPointTreeLogin(String.join("','",roleIdList));
        try {
            mapList = Misc.recursion("0",mapList);
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String,Object>> getPointTreeApp(LoginAuthDto loginAuthDto) throws Exception {
        List<Map<String,Object>> mapList = wbConfigDao.getPointTreeApp();
       return mapList;
    }



    public WebGisConfig getConfigData(String id) {
        WebGisConfig webGisConfig = new WebGisConfig();
        PlaneConfig planeConfig = wbConfigDao.getPlaneData(id);
        TableConfig tableConfig = wbConfigDao.getTableData(id);
        if(!PublicUtil.isEmpty(planeConfig)){
            webGisConfig.setPlaneConfig(planeConfig);
        }
        if(!PublicUtil.isEmpty(tableConfig)){
            webGisConfig.setTableConfig(tableConfig);
        }
        return webGisConfig;
    }

    public List<Map<String,Object>> getAhStation() {
        List<Map<String,Object>> dataList = wbConfigDao.getAhStation();

        // 数据别名处理, 去除应用气象观测、气象观测
        for (int i = 0; i < dataList.size(); i++) {
            Map<String,Object> map = dataList.get(i);
            String STATIONNAME = String.valueOf(map.get("STATIONNAME"));
            STATIONNAME = STATIONNAME.replace("应用气象观测","").replace("气象观测","");
            map.put("STATIONNAME",STATIONNAME);
        }
       return dataList;
    }
}
