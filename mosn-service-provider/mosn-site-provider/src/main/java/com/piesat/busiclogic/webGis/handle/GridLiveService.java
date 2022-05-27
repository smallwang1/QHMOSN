package com.piesat.busiclogic.webGis.handle;

import com.piesat.busiclogic.common.Util.HttpUtil;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.webGis.entity.GrdBean;
import com.piesat.busiclogic.webGis.entity.GrdElement;
import com.piesat.cimiss.common.util.CimissUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 实况网格数据处理handle
 */
@Service
public class GridLiveService {

    @Autowired
    private HttpUtil httpUtil;

    public static GrdBean decodeGrd(List<String> rlist) throws Exception{
        GrdBean grdBean = new GrdBean();
        List<String> configList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String[] element = rlist.get(i).split("\\s+") ;
            configList.add(element[1]);
        }
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        grdBean.setNcols(configList.get(0));
        grdBean.setNrows(configList.get(1));
        grdBean.setCellsize(configList.get(4));
        grdBean.setX0(decimalFormat.format(Float.valueOf(configList.get(2))));
        grdBean.setY0(decimalFormat.format(Float.valueOf(configList.get(3))));

        grdBean.setX1(decimalFormat.format(Float.valueOf(configList.get(2)) + Float.valueOf(configList.get(4))*Float.valueOf(configList.get(0))));
        grdBean.setY1(decimalFormat.format(Float.valueOf(configList.get(3)) + Float.valueOf(configList.get(4))*Float.valueOf(configList.get(1))));
        grdBean.setNodata_value(configList.get(5));

        List<Float> eleList = new ArrayList<>();

        for (int i = 6; i < rlist.size(); i++) {
            String[] element = rlist.get(i).split("\\s+") ;
            for (int i1 = 1; i1 < element.length; i1++) {
                eleList.add(Float.valueOf(element[i1]));
            }
        }
        grdBean.setEleList(eleList);
        return grdBean;
    }

    /**
     * 数据处理成二维数组
     * @param elist
     * @param x
     * @param y
     * @return
     */
    public float[][] handleData(List<Float> elist,int x,int y){
        // 将一维数组转换为矩阵数据
        //[Integer.valueOf(configList.get(1))][Integer.valueOf(configList.get(0))
        float[][] data = new float[x][y];
        for(int i =0 ;i < elist.size();i++){
            int a = i % Integer.valueOf(y);
            int b = i / Integer.valueOf(y);
            data[b][a] =  elist.get(i);
        }
        return data;
    }

    /**
     * 添加预报数值经纬度信息
     * @param flist
     * @param x0
     * @param y0
     * @param ncol
     * @param size
     * @return
     */
    public List<GrdElement> addCoordinate(List<Float> flist,float x0,float y0,int ncol,float size){
        List<GrdElement> grdElements = new ArrayList<>();
        // 数据稀释为原数据1/5
        for (int i = 0; i < flist.size(); i= i+5) {
            GrdElement grdElement = new GrdElement();
            int a = i % ncol;
            int b = i / ncol;
            grdElement.setX0(String.valueOf(x0 - a*size - 0.0125));
            grdElement.setY0(String.valueOf(y0 - b*size - 0.0125));
            grdElement.setValue(flist.get(i));
            grdElements.add(grdElement);
        }
        return grdElements;
    }



    public JSONObject getProductData(Map<String,String> paramMap) throws Exception{
        String data = httpUtil.get(paramMap,Misc.getPropValue("transparent.properties","product_url"));
        JSONObject jsonData = JSONObject.fromObject(data);
        return jsonData;
    }

    public GrdBean getGrdData(LiveDataParam liveDataParam) throws  Exception {
        Map<String,String> paramMap = CimissUtils.bean2map(liveDataParam);
        JSONObject data = this.getProductData(paramMap);
        List<String> handleList = Misc.getListByIo(Misc.getIoByUrl(String.valueOf(data.getJSONArray("result").getJSONObject(0).get("FileUrl"))));
        GrdBean grdBean = GridLiveService.decodeGrd(handleList);
        grdBean.setData(this.handleData(grdBean.getEleList(),Integer.valueOf(grdBean.getNrows()),Integer.valueOf(grdBean.getNcols())));
        grdBean.setGrdEleData(this.addCoordinate(grdBean.getEleList(),Float.valueOf(grdBean.getX1()),Float.valueOf(grdBean.getY1())
                ,Integer.valueOf(grdBean.getNcols()),Float.valueOf(grdBean.getCellsize())));
        return  grdBean;
    }
}
