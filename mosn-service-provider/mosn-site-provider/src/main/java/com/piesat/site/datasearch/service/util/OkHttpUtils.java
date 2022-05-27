package com.piesat.site.datasearch.service.util;

import com.piesat.site.datasearch.service.interceptor.NetInterceptor;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    /**
     * 默认超时时间
     */
    public static Long DEFAULT_TIME_OUT = 20L;

    /**
     * 不同timeout的连接池
     */
    public static ConcurrentHashMap<Long, OkHttpClient> cacheClients = new ConcurrentHashMap<>();

    /**
     * 全局实例可以保持http1.1 连接复用，线程池复用，减少tcp的网络连接和关闭
     */
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .addNetworkInterceptor(new NetInterceptor())//添加拦截器
            .retryOnConnectionFailure(false)//关闭失败重连
            .connectionPool(new ConnectionPool(100, 5, TimeUnit.MINUTES))//连接池
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .build();

    /**
     * 根据超时时间获取OkHttpClient
     * @param timeout 超时时间
     * @return
     */
    public static OkHttpClient getOkHttpClient(Long timeout) {
        if (timeout == 0L || DEFAULT_TIME_OUT == timeout)
            return OK_HTTP_CLIENT;
        else {
            OkHttpClient okHttpClient = cacheClients.get(timeout);
            if (null == okHttpClient)
                return syncCreateClient(timeout);
            return okHttpClient;
        }

    }

    /**
     * 创建新的连接并加入连接池
     * @param timeout 超时时间
     * @return
     */
    public static synchronized OkHttpClient syncCreateClient(Long timeout) {
        OkHttpClient okHttpClient = cacheClients.get(timeout);
        if (null != okHttpClient)
            return okHttpClient;

        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new NetInterceptor())//添加拦截器
                .retryOnConnectionFailure(false)//关闭失败重连
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .build();
        cacheClients.put(timeout, okHttpClient);
        return okHttpClient;
    }

    /**
     * 调用okhttp的newCall方法
     * @param request 请求
     * @param timeout 超时时间
     * @return
     */
    private static String execNewCall(Request request, Long timeout){
        String result = "";
        Response response = null;
        try {
            OkHttpClient okHttpClient = getOkHttpClient(timeout);
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("okhttp3 put error >> ex = {}", ExceptionUtils.getStackTrace(e));
            result = ExceptionUtils.getStackTrace(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return result;
    }

    /**
     * GET(默认超时时间)
     * @param url
     * @param queries
     * @return
     */
    public static String get(String url, Map<String, Object> queries) {
        return get(url, queries, DEFAULT_TIME_OUT);
    }

    /**
     * GET
     * @param url 请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @param timeout 超时时间
     * @return
     */
    public static String get(String url, Map<String, Object> queries, Long timeout) {
        StringBuffer sb = StringUtils.getQueryString(url, queries);
        logger.info("服务请求地址为: " + sb.toString().trim());
        Request request = new Request.Builder()
                .url(sb.toString().trim())
                .method("GET", null)
                .addHeader("Accept", "*/*")
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        return execNewCall(request, timeout);
    }

    /**
     * POST
     * @param url 请求的url
     * @param params post body 提交的参数
     * @param timeout 超时时间
     * @return
     */
    public static String postBodyParams(String url, Map<String, String> params, Long timeout) {
        MultipartBody.Builder builder = new MultipartBody
                .Builder()
                .setType(MultipartBody.FORM);
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
        RequestBody requestBody = builder.build();

        Request request = new Request
                .Builder()
                .url(url)
                .method("POST", requestBody)
                .header("Content-Type", "application/x-www-from-urlencoded")
                .build();
        return execNewCall(request, timeout);
    }

    /**
     * POST
     * @param url 请求的url
     * @param params post form 提交的参数
     * @param timeout 超时时间
     * @return
     */
    public static String postFormParams(String url, Map<String, String> params, Long timeout) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        return execNewCall(request, timeout);
    }

    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * @param url 请求的url
     * @param jsonParams 请求的JSON
     * @param timeout 超时时间
     * @return
     */
    public static String postJsonParams(String url, String jsonParams, Long timeout) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request, timeout);
    }

    /**
     * Post请求发送xml数据
     * @param url 请求的url
     * @param xml 请求的xmlString
     * @param timeout 超时时间
     * @return
     */
    public static String postXmlParams(String url, String xml, Long timeout) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request, timeout);
    }
}
