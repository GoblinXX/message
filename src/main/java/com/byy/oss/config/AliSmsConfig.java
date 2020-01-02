package com.byy.oss.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: message
 * @description: 阿里云短信初始化配置
 * @author: Goblin
 * @create: 2019-08-19 19:40
 **/
@Setter
@Getter
@ToString
public class AliSmsConfig {
  /**
   * accessKeyId
   */
  private String accessKeyId;
  /**
   * 秘钥
   */
  private String secret;
  /**
   * 区域
   */
  private String regionId;
  /**
   * 签名
   */
  private String signName;
  /**
   * code
   */
  private String code;

}
