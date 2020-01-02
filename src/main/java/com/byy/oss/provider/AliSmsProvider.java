package com.byy.oss.provider;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.byy.oss.config.AliSmsConfig;
import com.byy.oss.entity.SmsResult;
import com.byy.oss.helper.JsonHelper;
import lombok.AllArgsConstructor;

/**
 * @program: message
 * @description: 阿里云短信提供服务
 * @author: Goblin
 * @create: 2019-08-19 19:48
 **/
@AllArgsConstructor
public class AliSmsProvider {

  private final AliSmsConfig aliSmsConfig;

  /**
   * 发送短信
   */
  public SmsResult sendSms(String phone, String code) {
    DefaultProfile profile = DefaultProfile
        .getProfile(aliSmsConfig.getRegionId(), aliSmsConfig.getAccessKeyId(),
            aliSmsConfig.getSecret());
    IAcsClient client = new DefaultAcsClient(profile);
    SmsResult smsResult = null;
    CommonRequest request = new CommonRequest();
    request.setMethod(MethodType.POST);
    request.setDomain("dysmsapi.aliyuncs.com");
    request.setVersion("2017-05-25");
    request.setAction("SendSms");
    request.putQueryParameter("RegionId", aliSmsConfig.getRegionId());
    request.putQueryParameter("PhoneNumbers", phone);
    request.putQueryParameter("SignName", aliSmsConfig.getSignName());

    request.putQueryParameter("TemplateCode", aliSmsConfig.getCode());

    request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
    try {
      CommonResponse response = client.getCommonResponse(request);
      smsResult = JsonHelper.str2Obj(response.getData(), SmsResult.class);
      System.out.println(smsResult.getCode());

    } catch (ServerException e) {
      e.printStackTrace();
    } catch (ClientException e) {
      e.printStackTrace();
    }
    return smsResult;
  }
}
