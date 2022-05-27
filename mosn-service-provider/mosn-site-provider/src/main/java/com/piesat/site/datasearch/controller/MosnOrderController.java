package com.piesat.site.datasearch.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datasearch.service.IMosnOrderService;
import com.piesat.site.datasearch.service.constant.Constants;
import com.piesat.site.datasearch.service.dto.OrderDto;
import com.piesat.site.datasearch.service.entity.MosnOrder;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.HttpUtils;
import com.piesat.site.datasearch.service.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class MosnOrderController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnOrderController.class);

    @Autowired
    private IMosnOrderService mosnOrderService;

    /**
     * 订单分页查询
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Wrapper list(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize, OrderVo order) {
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        List<OrderDto> orderList = BeanUtils.convertList2List(mosnOrderService.selectOrderList(order), OrderDto.class);
        PageInfo<OrderDto> orderPageInfo = new PageInfo<>(orderList);
        orderPageInfo.setTotal(page.getTotal());
        return WrapMapper.ok(orderPageInfo);
    }

    /**
     * 订单查询
     * @param order
     * @return
     */
    @GetMapping("/list")
    public Wrapper list(OrderVo order) {
        return WrapMapper.ok(BeanUtils.convertList2List(mosnOrderService.selectOrderList(order), OrderDto.class));
    }

    @GetMapping("/{orderNo}")
    public Wrapper getInfo(@PathVariable("orderNo") String orderNo) {
        return WrapMapper.ok(mosnOrderService.selectOrderById(orderNo));
    }

    /**
     * 订单生成
     * @param paramMap
     * @param interfaceId
     * @return
     */
    @PostMapping("/{interfaceId}")
    public Wrapper make(@RequestBody Map<String, Object> paramMap, @PathVariable("interfaceId") Long interfaceId) {
//        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
//        order.setCreateBy(loginAuthDto.getUsername());
        mosnOrderService.makeOrder(paramMap, interfaceId);
        return WrapMapper.ok();
    }

    /**
     * 订单审核详情
     * @param orderNo
     * @return
     */
    @GetMapping("/detail")
    public Wrapper detail(@RequestParam("orderNo") String orderNo) {
        return WrapMapper.ok(mosnOrderService.getOrderDetail(orderNo));
    }

    /**
     * 订单审核
     * @param paramMap
     * @return
     */
    @PostMapping("/audit")
    public Wrapper audit(@RequestBody Map<String, Object> paramMap) {
        OrderDto orderDto = mosnOrderService.selectOrderById(String.valueOf(paramMap.get("orderNo")));
        if (!Constants.ORDER_CREATE.equals(orderDto.getOrderStatus()))
            return WrapMapper.error("该订单" + orderDto.getOrderNo() + "已审核，无需再审核");
        mosnOrderService.auditOrder(paramMap);
        return WrapMapper.ok();
    }

    /**
     * 订单更新
     * @param order
     * @return
     */
    @PutMapping
    public Wrapper update(@Validated @RequestBody MosnOrder order) {
        return WrapMapper.judge(mosnOrderService.updateOrder(order));
    }


    /**
     * 删除订单
     * @param orderNos
     * @return
     */
    @DeleteMapping("/{orderNos}")
    public Wrapper remove(@PathVariable("orderNos") String[] orderNos) {
        return WrapMapper.judge(mosnOrderService.deleteOrder(orderNos));
    }

    /**
     * 文件下载
     * @param response
     * @param orderNo
     * @return
     * @throws IOException
     */
    @PostMapping("/download/{orderNo}")
    public void filedownload(HttpServletResponse response, @PathVariable("orderNo") String orderNo) throws Exception {
        OrderDto order = mosnOrderService.selectOrderById(orderNo);
        HttpUtils.setParam(response, mosnOrderService.filedownload(order), order.getFileName());
    }

    /**
     * 文件共享
     * @param orderNo
     * @return
     * @throws Exception
     */
    @GetMapping("/upload/{orderNo}/{serverId}")
    public Wrapper fileshare(@PathVariable("orderNo") String orderNo, @PathVariable("serverId") Long serverId) throws Exception {
        OrderDto order = mosnOrderService.selectOrderById(orderNo);
        return WrapMapper.judge(mosnOrderService.upload(order, serverId));
    }

    /**
     * FTP文件下载
     * @param response
     * @param orderNo
     * @throws IOException
     */
    @GetMapping("/ftp/{orderNo}")
    public void download(HttpServletResponse response, @PathVariable("orderNo") String orderNo) throws IOException {
        OrderDto order = mosnOrderService.selectOrderById(orderNo);
        HttpUtils.setParam(response, mosnOrderService.download(order), order.getFileName());
    }
}
