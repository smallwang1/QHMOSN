package com.piesat.busiclogic.webGis.handle;

import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.webGis.entity.GrbDataBean;
import com.piesat.common.util.PublicUtil;
import org.piesat.decode.grib.bean.Grib_Struct_Data;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class CldasService {

    public GrbDataBean handleCldasData(String elements, String times) {
        GrbHelper gribDecode = new GrbHelper();
        GrbDataBean grbDataBean = new GrbDataBean();
        String filePath = null;
        try {
            filePath = this.getFilePath(elements,times);
            Map<String, List<Grib_Struct_Data>> dataMap = gribDecode.read_grib2_data(filePath,"F_0044_0001_R001");
            if(!PublicUtil.isEmpty(dataMap)){
                Grib_Struct_Data grib_struct_data = null;
                for (Map.Entry<String, List<Grib_Struct_Data>> entry : dataMap.entrySet()) {
                    grib_struct_data = entry.getValue().get(0);
                }
                if(!PublicUtil.isEmpty(grib_struct_data)){
                    String[] bbox = new String[4];
                    bbox[0] = String.valueOf(grib_struct_data.getStartX());
                    bbox[1] = String.valueOf(grib_struct_data.getStartY());
                    bbox[2] = String.valueOf(grib_struct_data.getEndX());
                    bbox[3] = String.valueOf(grib_struct_data.getEndY());
                    grbDataBean.setBbox(bbox);
                    float[][] data = new float[grib_struct_data.getXCount()][grib_struct_data.getYCount()];
                    float[] odata = grib_struct_data.getdata();

                    if("tair".equals(elements)){ // 温度华氏转换为摄氏
                        odata = this.exchangeTemp(odata);
                    }

//                    float[] dataHandle = Misc.handleData(odata);
                    float[][] d1 = new float[grib_struct_data.getXCount()][grib_struct_data.getYCount()];

                    this.fload1dto2dYX(odata,d1);
                    float[][] rdata = Misc.shearDataNew(d1,grib_struct_data.getXCount(),grib_struct_data.getYCount(),6,6);
                    float[][] d2 = Misc.changePrecision(5,5,rdata);

                    float[][] d5 = Misc.upDown(d2);
                    grbDataBean.setXsize(194);
                    grbDataBean.setYsize(206);
                    grbDataBean.setXstep(0.05f);
                    grbDataBean.setYstep(0.05f);
                    grbDataBean.setData(d5);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grbDataBean;
    }

    private float[] exchangeTemp(float[] dataHandle) {
        float[] d1 = new float[dataHandle.length];
        for (int i = 0; i < dataHandle.length; i++) {
            d1[i] = dataHandle[i] - 273.15f;
        }
        return d1;
    }

    private void fload1dto2d(float[] getdata, float[][] data) {
        int c = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = getdata[c];
                c++;
            }
        }
    }


    private void fload1dto2dYX(float[] getdata, float[][] data) {
        int c = 0;
        for (int j = 0; j < data[0].length; j++) {
         for (int i = 0; i < data.length; i++) {
                data[i][j] = getdata[c];
                c++;
            }
        }
    }

    public String getFilePath(String elements,String times) throws Exception{
        // 文件名名关键词
        String keyWord = elements + "-" + times;
        String path = "";
        if("pre".equals(elements)){
            path =  Misc.getPropValue("transparent.properties","cldas_path1") + times.substring(0,4) + "/" +times.substring(0,8) + "/";
        }else{
            path =  Misc.getPropValue("transparent.properties","cldas_path2") + times.substring(0,4) + "/" + times.substring(4,6) + "/";
        }
        List<File> sfile = Misc.searchFile(new File(path),keyWord);
        if(sfile.size()>0){
            return sfile.get(0).getAbsolutePath();
        }else{
            return "";
        }
    }
}
