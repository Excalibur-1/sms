package com.gxl.sms.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息生产者
 *
 * @author gxl
 */
@RestController
public class QueueController {

  private final JmsMessagingTemplate jmsMessagingTemplate;

  @Autowired
  public QueueController(JmsMessagingTemplate jmsMessagingTemplate) {
    this.jmsMessagingTemplate = jmsMessagingTemplate;
  }

  @RequestMapping("/sendMap")
  public void sendMap() {
    Map<String, String> map = new HashMap<>(4);
    //发送手机号
    map.put("mobile", "18888888888");
    //短信模板code，从阿里大于账户获取
    map.put("template_code", "SMS_CODE");
    //短信签名配置，需要与阿里大于账户配置的短信签名名称一致
    map.put("sign_name", "短信签名");
    /*发送短信配置，根据阿里大于短信模板配置内容设置，
      例如短信模板中配置了 "您的验证码为：${code}" 则参数需要设置为 "{\"code\":\"888888\"}"
      配置了多个参数的情况下，则在后面拼接即可，例："{\"code\":\"888888\",\"name\":\"阿里大于\"}"
    */
    map.put("param", "{\"number\":\"888888\"}");
    //参数1：发送消息的队列名，参数2：发送短息所需参数
    jmsMessagingTemplate.convertAndSend("sms", map);
  }
}
