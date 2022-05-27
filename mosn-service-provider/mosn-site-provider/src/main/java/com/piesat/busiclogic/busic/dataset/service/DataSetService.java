package com.piesat.busiclogic.busic.dataset.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface DataSetService {


    Map<String, String> getProduct(String params);

    void getImg(HttpServletResponse response, String url, String s) throws Exception;

    String getBase64Str(HttpServletResponse response, String url, String s) throws Exception;

    List<Map<String, Object>> getDamageData(String starttime, String endtime);
}
