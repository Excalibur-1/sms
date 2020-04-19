package com.gxl.sms.listener;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.gxl.sms.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信监听器
 *
 * @author gxl
 */
@Slf4j
@Component
public class SmsListener {

  private final SmsUtil smsUtil;

  @Autowired
  public SmsListener(SmsUtil smsUtil) {
    this.smsUtil = smsUtil;
  }

  /**
   * 发送消息监听器
   *
   * @param map 参数
   */
  @JmsListener(destination = "sms")
  public void sendSms(Map<String, String> map) {
    try {
      SendSmsResponse sendSmsResponse = smsUtil.sendSms(
          map.get("mobile"),
          map.get("template_code"),
          map.get("sign_name"),
          map.get("param")
      );
      log.info("消息发送结果[流水号:{}, code:{}, message:{}, requestId:{}]",
          sendSmsResponse.getBizId(),
          sendSmsResponse.getCode(),
          sendSmsResponse.getMessage(),
          sendSmsResponse.getRequestId());
    } catch (ClientException e) {
      e.printStackTrace();
    }
  }

  /**
   * 查询消息监听器
   *
   * @param map 参数
   */
  @JmsListener(destination = "sms_query")
  public void querySms(Map<String, String> map) {
    try {
      QuerySendDetailsResponse response =
          smsUtil.querySendDetails(map.get("mobile"), map.get("bizId"));
      log.info("消息查询结果[code:{}, message:{}, requestId:{}, detail:{}, totalCount:{}]",
          response.getCode(),
          response.getMessage(),
          response.getRequestId(),
          response.getSmsSendDetailDTOs(),
          response.getTotalCount());
    } catch (ClientException e) {
      e.printStackTrace();
    }
  }

}
