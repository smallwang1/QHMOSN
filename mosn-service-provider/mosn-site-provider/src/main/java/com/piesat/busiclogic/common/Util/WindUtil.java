package com.piesat.busiclogic.common.Util;

import net.sf.json.JSONObject;

import java.text.DecimalFormat;

/**
 * 风场处理工具类
 */

public class WindUtil {


    /**
     *
     * 方法说明 :动态流线加载工具类 参数说明 :
     *
     * @param udata
     * @param vdata
     * @param lonstart
     * @param lonend
     * @param latstart
     * @param latend
     * @return
     */
    public static JSONObject getDynamicWindStream(Float[][] udata, Float[][] vdata, float lonstart, float lonend,
                                                  float latstart, float latend) {
        JSONObject jo = new JSONObject();
        try {
            DecimalFormat df = new DecimalFormat("0.0");
            jo.put("x0", lonstart);
            jo.put("x1", lonend);
            jo.put("y0", latend);
            jo.put("y1", latstart);
            jo.put("gridWidth", udata[0].length);
            jo.put("gridHeight", udata.length);
            int width = udata[0].length;
            int heigth = udata.length;
            float[] d = new float[width*heigth * 2];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < heigth; j++) {
                    // d[(j + i* heigth) * 2] = udata[heigth-j-1][i];
                    // d[(j + i* heigth) * 2+1 ] = vdata[heigth-j-1][i];
                    d[(j + i * heigth) * 2] = Float.valueOf(df.format(udata[heigth - j - 1][i]));
                    d[(j + i * heigth) * 2 + 1] = Float.valueOf(df.format(vdata[heigth - j - 1][i]));
                }
            }
            jo.put("field", d);

        } catch (Exception e) {
            // TODO: handle exception
        }
//		String res = null;
//		try {
//			res = zipString(jo.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 String deco = unzipString(res);
        return jo;
    }



}
