package com.piesat.busiclogic.common.Util;

/**
 * @Descripton 返回值
 * @Author sjc
 * @Date 2020/2/12
 **/
public class ResultData {


    private String code = "sucess";// 请求返回状态

    private String msg;//信息

    private Object obj;//返回数据

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
