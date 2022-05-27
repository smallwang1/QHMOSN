package com.oid.active.listener;

import com.oid.active.entity.AjaxRes;
import com.oid.active.util.BeanUtils;
import com.piesat.quartz.util.SpringUtils;
import com.piesat.site.identifier.service.impl.MosnServiceOidServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class QueueConsumerListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(QueueConsumerListener.class);

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage)message;
        try {
            String messageText = textMessage.getText();
            logger.info("监听到MQ中有数据，并获取MQ中数据信息 ----> " + messageText);
            AjaxRes ajaxRes = BeanUtils.mqStrToBean(messageText);
            MosnServiceOidServiceImpl mosnServiceOidService = SpringUtils.getBean(MosnServiceOidServiceImpl.class);
            mosnServiceOidService.parseResult(ajaxRes);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
