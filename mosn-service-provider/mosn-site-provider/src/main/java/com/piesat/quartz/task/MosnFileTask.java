package com.piesat.quartz.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.quartz.util.StringUtils;
import com.piesat.quartz.util.ZipUtils;
import com.piesat.site.datasearch.service.IMosnInterfaceService;
import com.piesat.site.datasearch.service.IMosnOrderService;
import com.piesat.site.datasearch.service.IRemoteObsService;
import com.piesat.site.datasearch.service.constant.Constants;
import com.piesat.site.datasearch.service.entity.MosnOrder;
import com.piesat.site.datasearch.service.util.DateUtils;
import com.piesat.site.datasearch.service.util.HttpUtils;
import com.piesat.site.datasearch.service.util.OkHttpUtils;
import com.piesat.site.datasearch.service.vo.OrderVo;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文件生成并打包上传定时任务
 */
@Component("mosnFileTask")
public class MosnFileTask {

    private static final Logger logger = LoggerFactory.getLogger(MosnFileTask.class);

    @Value("${mosn.localPath}")
    private String localPath;

    @Resource
    private IMosnOrderService mosnOrderService;
    @Resource
    private IRemoteObsService remoteObsService;
    @Resource
    private IMosnInterfaceService mosnInterfaceService;

    public void generateFile(String orderNo) {
        logger.info("------------------文件生成并打包上传定时任务开始执行------------------");
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(orderNo);
        orderVo.setFileStatus(Constants.FILE_CREATE);
        List<MosnOrder> orderList = mosnOrderService.selectOrderList(orderVo);
        if (StringUtils.isEmpty(orderList))
            return;

        orderList.forEach(order -> {
            try {
                Map<String, Object> paramMap = JSONObject.parseObject(order.getOrderParam());
                long interfaceId = Long.parseLong(paramMap.get("interfaceId").toString());
                paramMap.remove("interfaceId");
                Map<String, Object> params = mosnInterfaceService.selectInfoById(interfaceId);
                String dataType = String.valueOf(params.get("dataType"));
                params.remove("dataType");
                params.remove("dataFormat");
                if (null != paramMap.get("limitCnt")) {
//                    paramMap.remove("limitCnt");
                    paramMap.put("limitCnt", "50000");
                }
                params.putAll(paramMap);
                StringUtils.removeNullKey(params);
                String result = remoteObsService.queryObsData(params);
                String filePath = localPath + File.separator + DateUtils.getCurrentTime("yyyyMMdd");
                String fileName = order.getOrderNo().concat(".").concat(order.getFileType());
                if (StringUtils.isNotEmpty(dataType) && Constants.RESOURCE_TYPE.equals(dataType)) {
                    filePath = localPath + File.separator + "tmp" + DateUtils.getCurrentTime("yyyyMMdd") + File.separator + order.getOrderNo();
                    parseResult(result, filePath, filePath+File.separator+fileName);
                }
                InputStream inputStream = IOUtils.toInputStream(result, StandardCharsets.UTF_8);
                HttpUtils.createLocalFile(inputStream, filePath, fileName);
                order.setFileName(fileName);
                order.setFileSize(String.valueOf(result.getBytes().length));
                order.setFilePath(filePath);
                order.setFileStatus(Constants.FILE_SUCCESS);
                order.setUpdateTime(new Date());
            } catch (Exception e) {
                order.setFileStatus(Constants.FILE_FAIL);
                order.setUpdateTime(new Date());
                order.setRemark(e.getMessage());
                logger.error("MosnFileTask.generateFile()文件生成失败---->{}", e.getMessage());
            }
            mosnOrderService.updateOrder(order);
        });

        logger.info("------------------文件生成并打包上传定时任务执行结束------------------");
    }

    private void parseResult(String result, String filePath, String name) throws Exception {
        JSONObject resultJson = JSONObject.parseObject(result);
        if (!"0".equals(resultJson.get("returnCode")))
            throw new RuntimeException("查询天擎异常: " + resultJson.get("returnMessage"));

        JSONArray dsArray = resultJson.getJSONArray("DS");
        if (dsArray.size() <= 0) return;
        for (int i = 0; i < dsArray.size(); i++) {
            JSONObject jsonObject = dsArray.getJSONObject(i);
            String fileName = jsonObject.get("FILE_NAME").toString();
            String fileUrl = jsonObject.get("FILE_URL").toString();
            String resultStr = OkHttpUtils.get(fileUrl, null);
            InputStream inputStream = IOUtils.toInputStream(resultStr, StandardCharsets.UTF_8);
            HttpUtils.createLocalFile(inputStream, filePath, fileName);
            // TODO 解密操作
        }
        ZipUtils.packet(Paths.get(filePath), Paths.get(name));
        ZipUtils.deletefile(filePath);
    }
}
