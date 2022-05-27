package com.piesat.busiclogic.app.web;

import com.piesat.busiclogic.app.handle.AppIndexService;
import com.piesat.busiclogic.app.util.AppUtil;
import com.piesat.busiclogic.busic.productMgr.entity.Product;
import com.piesat.busiclogic.busic.productMgr.service.ProductServie;
import com.piesat.busiclogic.webGis.handle.LiveDataParam;
import com.piesat.common.anno.Description;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.util.PublicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * app首页web
 */
@RestController
@CrossOrigin
@RequestMapping("/noAuth/app/index")
public class AppIndexController {

    @Autowired
    private AppIndexService appIndexService;


    @Autowired
    private ProductServie productServie;

    @Description("行政区划下拉框")
    @RequestMapping(value = "/getAreaData",  method = RequestMethod.GET)
    public Wrapper getAreaData(LiveDataParam liveDataParam){
        try{
            List<Map<String,Object>> dataList = appIndexService.getAreaData();
            return WrapMapper.ok(dataList);
        }catch (Exception e){
            e.printStackTrace();
            return   WrapMapper.error(e.getMessage());
        }
    }

    @Description("查询产品树结构")
    @RequestMapping(value = "/getProductData",  method = RequestMethod.GET)
    public Wrapper getProductData(Product product){
        try {
            List<Map<String, Object>> list = productServie.getProductData(product,new LoginAuthDto());
            AppUtil appUtil = new AppUtil();
            list = appUtil.getNodeData(list);
            List<Map<String,Object>> data = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                AppUtil appUtil1 = new AppUtil();
                if(!PublicUtil.isEmpty(list.get(i).get("childrenList"))){
                    data.add(list.get(i));
                    data.get(i).put("childrenList",appUtil1.getHandleData(list.get(i)));
                }
            }
            return   WrapMapper.ok(data);
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }

    @Description("查询产品树结构、原始树结构保留第一层、第二层、最后一层产品层")
    @RequestMapping(value = "/getProductTree",  method = RequestMethod.GET)
    public Wrapper getProductTree(Product product){
        try {
            List<Map<String, Object>> list = productServie.getProductData(product,new LoginAuthDto());
            for (int i = 0; i < list.size(); i++) {
                List<Map<String,Object>> secondList = (List<Map<String,Object>>)list.get(i).get("childrenList");
                List<Map<String,Object>> data = new ArrayList<>();
                for (int i1 = 0; i1 < secondList.size(); i1++) {
                    AppUtil appUtil1 = new AppUtil();
                    if(!PublicUtil.isEmpty(secondList.get(i1).get("childrenList"))){
                        data.add(secondList.get(i1));
                        data.get(i1).put("childrenList",appUtil1.getHandleData(secondList.get(i1)));
                    }
                }
                list.get(i).put("childrenList",data);
            }
            return   WrapMapper.ok(list);
        }catch (Exception e){
            return   WrapMapper.ok(e.getMessage());
        }
    }
}
