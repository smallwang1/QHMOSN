package com.piesat.site.datasearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piesat.quartz.task.MosnFileTask;
import com.piesat.site.datasearch.service.IMosnFtpServerService;
import com.piesat.site.datasearch.service.IMosnOrderService;
import com.piesat.site.datasearch.service.IMosnSyncFtpService;
import com.piesat.site.datasearch.service.constant.Constants;
import com.piesat.site.datasearch.service.dto.OrderDto;
import com.piesat.site.datasearch.service.dto.SyncFtpDto;
import com.piesat.site.datasearch.service.entity.MosnFtpServer;
import com.piesat.site.datasearch.service.entity.MosnOrder;
import com.piesat.site.datasearch.service.entity.MosnSyncFtp;
import com.piesat.site.datasearch.service.mapper.MosnOrderMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.DateUtils;
import com.piesat.site.datasearch.service.util.FtpUtil;
import com.piesat.site.datasearch.service.util.StringUtils;
import com.piesat.site.datasearch.service.vo.ApplyDetailVo;
import com.piesat.site.datasearch.service.vo.OrderDetailVo;
import com.piesat.site.datasearch.service.vo.OrderVo;
import com.piesat.site.datasearch.service.vo.SyncDetailVo;
import com.piesat.site.identifier.service.IMosnServiceOidService;
import com.piesat.site.identifier.service.entity.MosnServiceOidEntity;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("mosnOrderService")
public class MosnOrderServiceImpl implements IMosnOrderService {

    private static final Logger logger = LoggerFactory.getLogger(MosnOrderServiceImpl.class);

    @Autowired
    private Environment environment;
    @Autowired
    private MosnOrderMapper mosnOrderMapper;
    @Autowired
    private IMosnSyncFtpService mosnSyncFtpService;
    @Autowired
    private IMosnFtpServerService mosnFtpServerService;
    @Autowired
    private IMosnServiceOidService mosnServiceOidService;
    @Autowired
    private MosnFileTask mosnFileTask;

    @Override
    public OrderDto selectOrderById(String orderNo) {
        return BeanUtils.convertObj2Obj(mosnOrderMapper.selectOrderById(orderNo), OrderDto.class);
    }

    @Override
    public List<MosnOrder> selectOrderList(OrderVo order) {
        return mosnOrderMapper.selectOrderList(order);
    }

    @Override
    public Integer insertOrder(MosnOrder order, Long interfaceId) {
        Date date = new Date();
        order.setOrderNo(DateUtils.createRandomNo(date));
        Map<String, Object> params = order.getParams();

        params.put("interfaceId", interfaceId);
        params.put("dataFormat", order.getFileType());
        order.setOrderParam(JSONObject.toJSONString(params));
        order.setOrderStatus(Constants.ORDER_CREATE); // TODO 待审核
        order.setFileStatus(Constants.FILE_CREATE);
        order.setCreateTime(date);
        return mosnOrderMapper.insertOrder(order);
    }

    @Override
    public Integer updateOrder(MosnOrder order) {
        return mosnOrderMapper.updateOrder(order);
    }

    @Override
    public Integer deleteOrder(String[] orderNos) {
        return mosnOrderMapper.deleteOrder(orderNos);
    }

    @Override
    public byte[] download(OrderDto order) {

        return FtpUtil.downloadFtpFile(environment.getProperty("mosn.ftpHost"),
                environment.getProperty("mosn.ftpPort", Integer.class),
                environment.getProperty("mosn.ftpUserName"),
                environment.getProperty("mosn.ftpPassword"),
                order.getFilePath(),
                order.getFileName());
    }

    @Override
    public Boolean upload(OrderDto order, Long serverId) throws FileNotFoundException {
        List<MosnFtpServer> ftpServerList = mosnFtpServerService.selectFtpServerByIds(new Long[]{serverId});
        if (StringUtils.isEmpty(ftpServerList)) return false;
        MosnFtpServer ftpServer = ftpServerList.get(0);
        return FtpUtil.uploadFile(ftpServer.getHost(),
                ftpServer.getPort(),
                ftpServer.getUserName(),
                ftpServer.getPassword(),
                ftpServer.getFilePath(),
                DateUtils.getCurrentTime("yyyyMMdd"),
                order.getFileName(), new FileInputStream(order.getFilePath() + File.separator + order.getFileName()));
    }

    @Override
    public byte[] filedownload(OrderDto order) throws IOException {

        return FileUtils.readFileToByteArray(new File(order.getFilePath() + File.separator + order.getFileName()));
    }

    @Override
    public void auditOrder(Map<String, Object> paramMap) {
        String orderNo = String.valueOf(paramMap.get("orderNo"));
        MosnOrder order = new MosnOrder();
        order.setOrderNo(orderNo);
        order.setAuditBy(String.valueOf(paramMap.get("auditBy")));
        order.setAuditTime(DateUtils.getNowDate());
        order.setAuditReason(String.valueOf(paramMap.get("auditReason")));
        if (Constants.ORDER_SUCCESS.equals(String.valueOf(paramMap.get("auditFlag")))) {
            order.setOrderStatus(Constants.ORDER_SUCCESS);
            mosnOrderMapper.updateOrder(order);
            mosnFileTask.generateFile(orderNo);
        } else {
            order.setOrderStatus(Constants.ORDER_FAIL);
            mosnOrderMapper.updateOrder(order);
        }

    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void makeOrder(Map<String, Object> paramMap, Long interfaceId) {
        Object orderInfo = paramMap.get("orderInfo");
        MosnOrder order = JSON.parseObject(JSON.toJSON(orderInfo).toString(), MosnOrder.class);
        this.insertOrder(order, interfaceId);

        Object applyInfo = paramMap.get("applyInfo");
        MosnServiceOidEntity serviceOidEntity = JSON.parseObject(JSON.toJSON(applyInfo).toString(), MosnServiceOidEntity.class);
        serviceOidEntity.setBusiId(order.getOrderNo());
        mosnServiceOidService.insertServiceOid(serviceOidEntity);

        Object syncInfo = paramMap.get("syncInfo");
        SyncFtpDto syncFtp = JSON.parseObject(JSON.toJSON(syncInfo).toString(), SyncFtpDto.class);
        syncFtp.setOrderNo(order.getOrderNo());
        mosnSyncFtpService.generatorSyncFtp(syncFtp);
    }

    @Override
    public Map<String, Object> getOrderDetail(String orderNo) {
        Map<String, Object> result = new HashMap<>();
        MosnOrder mosnOrder = mosnOrderMapper.selectOrderById(orderNo);
        OrderDetailVo orderDetailVo = BeanUtils.convertObj2Obj(mosnOrder, OrderDetailVo.class);
        Map<String, Object> paramMap = JSONObject.parseObject(mosnOrder.getOrderParam());
        paramMap.remove("dataFormat");
        paramMap.remove("interfaceId");
        orderDetailVo.setParams(paramMap);
        MosnServiceOidEntity mosnServiceOidEntity = mosnServiceOidService.selectServiceOidById(orderNo);
        SyncDetailVo syncDetailVo = mosnSyncFtpService.selectSyncDetailByorderNo(orderNo);
        result.put("orderInfo", orderDetailVo);
        result.put("applyInfo", BeanUtils.convertObj2Obj(mosnServiceOidEntity, ApplyDetailVo.class));
        result.put("syncInfo", syncDetailVo);
        return result;
    }
}
