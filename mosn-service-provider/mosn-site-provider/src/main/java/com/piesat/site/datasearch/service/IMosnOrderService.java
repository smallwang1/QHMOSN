package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.OrderDto;
import com.piesat.site.datasearch.service.entity.MosnOrder;
import com.piesat.site.datasearch.service.vo.OrderVo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMosnOrderService {

    /**
     * 生成订单
     * @param order
     * @return
     */
    Integer insertOrder(MosnOrder order, Long interfaceId);

    /**
     * 更新订单
     * @param order
     * @return
     */
    Integer updateOrder(MosnOrder order);

    /**
     * 查询订单列表
     * @param order
     * @return
     */
    List<MosnOrder> selectOrderList(OrderVo order);

    /**
     * 删除订单
     * @param orderNos
     * @return
     */
    Integer deleteOrder(String[] orderNos);

    /**
     * ftp下载
     * @return
     */
    byte[] download(OrderDto order);

    /**
     * 根据Id查询订单
     * @param orderNo
     * @return
     */
    OrderDto selectOrderById(String orderNo);

    /**
     * 上传文件
     * @param order
     * @return
     */
    Boolean upload(OrderDto order, Long serverId) throws FileNotFoundException;

    /**
     * 文件下载
     * @param order
     * @return
     */
    byte[] filedownload(OrderDto order) throws IOException;

    /**
     * 订单审核
     * @param paramMap
     */
    void auditOrder(Map<String, Object> paramMap);

    void makeOrder(Map<String, Object> paramMap, Long interfaceId);

    Map<String, Object> getOrderDetail(String orderNo);
}
