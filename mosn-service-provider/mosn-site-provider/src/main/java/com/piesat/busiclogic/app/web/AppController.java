package com.piesat.busiclogic.app.web;

import com.piesat.busiclogic.app.util.AppUtil;
import com.piesat.busiclogic.busic.productMgr.entity.Product;
import com.piesat.busiclogic.busic.productMgr.service.ProductServie;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
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
@RequestMapping("/app/index")
public class AppController extends BaseController {

    @Autowired
    private ProductServie productServie;


    @Description("查询产品树结构")
    @RequestMapping(value = "/getProducts",  method = RequestMethod.GET)
    public Wrapper getProductData(Product product){
        try{
            List<Map<String, Object>> list = productServie.getProductData(product,getLoginAuthDto());
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
            return  WrapMapper.ok(data);
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }


    @Description("查询产品树结构")
    @RequestMapping(value = "/getProductsByCode",  method = RequestMethod.GET)
    public Wrapper getAppProductData(String code){
        try{
            List<Map<String, Object>> list = productServie.getProductDataByCode(code,getLoginAuthDto());
            return  WrapMapper.ok(list);
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }

}
