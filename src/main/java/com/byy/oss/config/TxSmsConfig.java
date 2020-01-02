package com.byy.oss.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: message
 * @description: 腾讯云短信初始化配置
 * @author: Goblin
 * @create: 2019-08-19 19:40
 **/
@Setter
@Getter
@ToString
public class TxSmsConfig {

  /**
   * appId
   */
  private Integer appId;
  /**
   * appKey
   */
  private String appKey;
  /**
   * 模板id
   */
  private int templateId;
  /**
   * 签名
   */
  private String smsSign;


}
