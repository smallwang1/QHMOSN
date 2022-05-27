package com.piesat.quartz.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.util.PublicUtil;
import com.piesat.site.datasearch.service.*;
import com.piesat.site.datasearch.service.constant.Constants;
import com.piesat.site.datasearch.service.entity.MosnFtpServer;
import com.piesat.site.datasearch.service.entity.MosnSyncFtp;
import com.piesat.site.datasearch.service.entity.MosnSyncHistory;
import com.piesat.site.datasearch.service.util.DateUtils;
import com.piesat.site.datasearch.service.util.FtpUtil;
import com.piesat.site.datasearch.service.util.StringUtils;
import com.piesat.site.datasearch.service.vo.SyncVo;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("mosnSyncTask")
public class MosnSyncTask {

    private static final Logger logger = LoggerFactory.getLogger(MosnSyncTask.class);

    @Autowired
    private IRemoteObsService remoteObsService;
    @Autowired
    private IMosnSyncFtpService mosnSyncFtpService;
    @Autowired
    private IMosnInterfaceService mosnInterfaceService;
    @Autowired
    private IMosnFtpServerService mosnFtpServerService;
    @Autowired
    private IMosnSyncHistoryService mosnSyncHistoryService;

    public void generateFile() {
        List<SyncVo> syncVoList = mosnSyncFtpService.generatorSync();
        if (StringUtils.isEmpty(syncVoList)) return;
        for (SyncVo syncVo : syncVoList) {
            boolean flag = false;
            MosnSyncHistory mosnSyncHistory = new MosnSyncHistory();
            MosnSyncFtp mosnSyncFtp = new MosnSyncFtp();
            Map<String, Object> paramMap = JSONObject.parseObject(syncVo.getSyncParam());
            Map<String, Object> syncParam = JSONObject.parseObject(syncVo.getSyncPeriod());
            MosnFtpServer ftpServer = null;
            try {
                List<MosnFtpServer> ftpServerList = mosnFtpServerService.selectFtpServerByIds(new Long[]{syncVo.getSyncServerId()});
                if (StringUtils.isEmpty(ftpServerList)) return;
                ftpServer = ftpServerList.get(0);
                if (Constants.SYNC_HISTORY.equals(syncVo.getSyncType())) {
                    paramMap.putAll(syncParam);
                    this.syncFile(paramMap, syncVo, ftpServer);
                    mosnSyncFtp.setId(syncVo.getId());
                    mosnSyncFtp.setStatus("1");
                } else {
                    Date now = new Date();
                    flag = !StringUtils.isNull(syncVo.getNextSyncTime()) && syncVo.getNextSyncTime().before(now);
                    if (flag) continue;
                    String timeType = syncParam.get("timeType").toString();
                    String timeFigure = syncParam.get("timeFigure").toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(now);
                    String end = sdf.format(now);
                    if (Constants.UNIT_MINUTE.equals(timeType)) {
                        calendar.add(Calendar.MINUTE, Integer.valueOf("-".concat(timeFigure)));
                        String start = sdf.format(calendar.getTime());
                        paramMap.put("timeRange", "[" + start.concat(",").concat(end) + "]");
                        calendar.setTime(now);
                        calendar.add(Calendar.MINUTE, Integer.valueOf(timeFigure));
                        mosnSyncFtp.setNextSyncTime(DateUtils.fDate(calendar.getTime()));
                    } else if (Constants.UNIT_HOUR.equals(timeType)) {
                        calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf("-".concat(timeFigure)));
                        String start = sdf.format(calendar.getTime());
                        paramMap.put("timeRange", "[" + start.concat(",").concat(end) + "]");
                        calendar.setTime(now);
                        calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(timeFigure));
                        mosnSyncFtp.setNextSyncTime(DateUtils.fDate(calendar.getTime()));
                    } else {
                        calendar.add(Calendar.DAY_OF_MONTH, Integer.valueOf("-".concat(timeFigure)));
                        String start = sdf.format(calendar.getTime());
                        paramMap.put("timeRange", "[" + start.concat(",").concat(end) + "]");
                        calendar.setTime(now);
                        calendar.add(Calendar.DAY_OF_MONTH, Integer.valueOf(timeFigure));
                        mosnSyncFtp.setNextSyncTime(DateUtils.fDate(calendar.getTime()));
                    }
                    this.syncFile(paramMap, syncVo, ftpServer);

                    mosnSyncFtp.setId(syncVo.getId());
                }
                mosnSyncHistory.setSyncStatus("1");
            } catch (Exception e) {
                mosnSyncFtp.setStatus("1");
                mosnSyncHistory.setSyncStatus("2");
                mosnSyncHistory.setRemark(e.getMessage());
            }

            if (!flag) {
                mosnSyncFtpService.updateSyncFtp(mosnSyncFtp);
                mosnSyncHistory.setSyncNo(syncVo.getSyncNo());
                mosnSyncHistory.setSyncParam(JSON.toJSONString(paramMap));
                mosnSyncHistory.setSyncHost(ftpServer.getHost());
                mosnSyncHistory.setSyncPath(ftpServer.getFilePath());
                mosnSyncHistory.setSyncType(syncVo.getSyncType());
                mosnSyncHistoryService.insertSyncHistory(mosnSyncHistory);
            }
        }
    }

    public void syncFile(Map<String, Object> paramMap, SyncVo syncVo, MosnFtpServer ftpServer) {
        Map<String, Object> params = mosnInterfaceService.selectInfoById(Long.parseLong(paramMap.get("interfaceId").toString()));
        params.remove("dataType");
        paramMap.remove("interfaceId");
        params.remove("dataFormat");
        if (null != paramMap.get("limitCnt")) {
//            paramMap.remove("limitCnt");
            paramMap.put("limitCnt", "50000");
        }
        params.putAll(paramMap);
        String result = remoteObsService.queryObsData(params);
        String filePath = DateUtils.getCurrentTime("yyyyMMdd");
        String fileName = syncVo.getSyncNo().concat("-").concat(DateUtils.dateFormat(syncVo.getNextSyncTime())).concat(".").concat(syncVo.getFileType());
        InputStream inputStream = IOUtils.toInputStream(result, StandardCharsets.UTF_8);
        FtpUtil.uploadFile(ftpServer.getHost(), ftpServer.getPort(), ftpServer.getUserName(), ftpServer.getPassword(), syncVo.getSyncServerPath(), filePath, fileName, inputStream);
    }

}
