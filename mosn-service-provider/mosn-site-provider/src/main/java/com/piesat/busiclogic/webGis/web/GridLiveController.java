package com.piesat.busiclogic.webGis.web;

import com.piesat.busiclogic.webGis.entity.GrdBean;
import com.piesat.busiclogic.webGis.handle.GridLiveService;
import com.piesat.busiclogic.webGis.handle.LiveDataParam;
import com.piesat.common.anno.Description;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 格点实况controller
 */

@RestController
@CrossOrigin
@RequestMapping("/noAuth/live")
public class GridLiveController {

    @Autowired
    private GridLiveService gridLiveService;

    @Description("预报矩阵数据")
    @RequestMapping(value = "/getData",  method = RequestMethod.GET)
    public Wrapper liveData(LiveDataParam liveDataParam){
        try{
            GrdBean grdBean = gridLiveService.getGrdData(liveDataParam);
            return WrapMapper.ok(grdBean);
        }catch (Exception e){
            e.printStackTrace();
            return   WrapMapper.error(e.getMessage());
        }
    }
}
