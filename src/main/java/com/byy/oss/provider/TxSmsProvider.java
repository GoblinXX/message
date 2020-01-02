package com.byy.oss.provider;

import com.byy.oss.config.TxSmsConfig;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.json.JSONException;

/**
 * @program: message
 * @description: 腾讯云短信提供服务
 * @author: Goblin
 * @create: 2019-08-19 23:27
 **/
@AllArgsConstructor
public class TxSmsProvider {

  private final TxSmsConfig txSmsConfig;

  /***
   * 指定模板ID单发短信
   * @param params
   * @param phoneNumbers
   * @return
   */
  public SmsMultiSenderResult senderResult(String[] params, String[] phoneNumbers) {
    SmsMultiSenderResult result = null;
    try {
      SmsMultiSender msender = new SmsMultiSender(txSmsConfig.getAppId(), txSmsConfig.getAppKey());
      result = msender.sendWithParam("86", phoneNumbers,
          // 签名参数未提供或者为空时，会使用默认签名发送短信
          txSmsConfig.getTemplateId(), params, txSmsConfig.getSmsSign(), "", "");
      System.out.print(result);
    } catch (HTTPException e) {
      // HTTP响应码错误
      e.printStackTrace();
    } catch (JSONException e) {
      // json解析错误
      e.printStackTrace();
    } catch (IOException e) {
      // 网络IO错误
      e.printStackTrace();
    }
    return result;
  }

}
