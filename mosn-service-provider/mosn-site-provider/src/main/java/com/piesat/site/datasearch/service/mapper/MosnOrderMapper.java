package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnOrder;
import com.piesat.site.datasearch.service.vo.OrderVo;

import java.util.List;

public interface MosnOrderMapper {

    /**
     * 订单保存
     * @param order
     * @return
     */
    Integer insertOrder(MosnOrder order);

    /**
     * 更新订单
     * @param order
     * @return
     */
    Integer updateOrder(MosnOrder order);

    /**
     * 查询所有订单列表
     * @return
     */
    List<MosnOrder> selectOrderAll();

    /**
     * 根据套件查询订单
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
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    MosnOrder selectOrderById(String orderNo);
}
