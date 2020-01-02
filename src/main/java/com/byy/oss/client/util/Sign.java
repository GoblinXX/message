package com.byy.oss.client.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

/**
 * @author: yyc
 * @date: 19-7-1 下午7:41
 */
public class Sign {

  public static String sign(String signStr, String secret, String signatureMethod)
      throws NoSuchAlgorithmException, InvalidKeyException {
    Mac mac = Mac.getInstance(signatureMethod);
    SecretKeySpec secretKey =
        new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), mac.getAlgorithm());
    mac.init(secretKey);
    byte[] hash = mac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
    return Base64.encodeBase64String(hash);
  }

  public static String makeSignPlainText(
      TreeMap<String, Object> requestParams,
      String requestMethod,
      String requestHost,
      String requestPath) {
    return requestMethod + requestHost + requestPath + buildParamStr(requestParams, requestMethod);
  }

  private static String buildParamStr(TreeMap<String, Object> requestParams, String requestMethod) {
    if (requestParams == null || requestParams.isEmpty()) {
      return "";
    }
    StringBuilder retStr = new StringBuilder("?");
    requestParams.forEach(
        (k, v) -> {
          if (v != null) {
            boolean b = "POST".equals(requestMethod) && v.toString().startsWith("@");
            if (!b) {
              retStr.append(k.replace("_", ".")).append('=').append(v).append("&");
            }
          }
        });
    return retStr.substring(0, retStr.length() - 1);
  }
}
