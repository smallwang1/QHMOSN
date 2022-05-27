package com.piesat.busiclogic.app.handle;

import com.piesat.busiclogic.app.dao.AppIndexDao;
import com.piesat.busiclogic.common.Util.Misc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppIndexService {

    @Autowired
    private AppIndexDao appIndexDao;

    public List<Map<String, Object>> getAreaData() throws Exception{
        List<Map<String,Object>> list = appIndexDao.getAreaData();
        list = Misc.recursion("0",list);
        return list;
    }
}
