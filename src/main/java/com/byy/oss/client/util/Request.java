package com.byy.oss.client.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Request {

  private static String requestUrl = "";
  private static String rawResponse = "";

  public static String getRequestUrl() {
    return requestUrl;
  }

  public static String getRawResponse() {
    return rawResponse;
  }

  public static String generateUrl(
      TreeMap<String, Object> params,
      String secretId,
      String secretKey,
      String requestMethod,
      String requestHost,
      String requestPath) {
    if (!params.containsKey("SecretId")) {
      params.put("SecretId", secretId);
    }
    if (!params.containsKey("Nonce")) {
      params.put("Nonce", (new Random()).nextInt(2147483647));
    }
    if (!params.containsKey("Timestamp")) {
      params.put("Timestamp", System.currentTimeMillis() / 1000L);
    }
    String plainText = Sign.makeSignPlainText(params, requestMethod, requestHost, requestPath);
    String signatureMethod = "HmacSHA1";
    if (params.containsKey("SignatureMethod")
        && params.get("SignatureMethod").toString().equals("HmacSHA256")) {
      signatureMethod = "HmacSHA256";
    }
    try {
      params.put("Signature", Sign.sign(plainText, secretKey, signatureMethod));
    } catch (Exception var13) {
      throw new IllegalStateException(var13.getMessage(), var13);
    }
    StringBuilder url = new StringBuilder("https://");
    url.append(requestHost).append(requestPath).append("?");
    if (requestMethod.equals("GET")) {
      for (String k : params.keySet()) {
        try {
          url.append(k.replace("_", "."))
              .append("=")
              .append(URLEncoder.encode(params.get(k).toString(), "utf-8"))
              .append("&");
        } catch (UnsupportedEncodingException var12) {
          return "https://" + requestHost + requestPath;
        }
      }
    }
    return url.toString().substring(0, url.length() - 1);
  }

  public static String send(
      TreeMap<String, Object> params,
      String secretId,
      String secretKey,
      String requestMethod,
      String requestHost,
      String requestPath) {
    if (!params.containsKey("SecretId")) {
      params.put("SecretId", secretId);
    }
    if (!params.containsKey("Nonce")) {
      params.put("Nonce", (new Random()).nextInt(2147483647));
    }
    if (!params.containsKey("Timestamp")) {
      params.put("Timestamp", System.currentTimeMillis() / 1000L);
    }
    params.remove("Signature");
    String plainText = Sign.makeSignPlainText(params, requestMethod, requestHost, requestPath);
    String signatureMethod = "HmacSHA1";
    if (params.containsKey("SignatureMethod")
        && params.get("SignatureMethod").toString().equals("HmacSHA256")) {
      signatureMethod = "HmacSHA256";
    }
    try {
      params.put("Signature", Sign.sign(plainText, secretKey, signatureMethod));
    } catch (Exception var9) {
      throw new IllegalStateException(var9.getMessage(), var9);
    }
    String url = "https://" + requestHost + requestPath;
    return sendRequest(url, params, requestMethod);
  }

  public static String sendRequest(
      String url, Map<String, Object> requestParams, String requestMethod) {
    StringBuilder result = new StringBuilder();
    BufferedReader in = null;
    StringBuilder paramStr = new StringBuilder();
    for (String key : requestParams.keySet()) {
      if (paramStr.length() > 0) {
        paramStr.append('&');
      }
      try {
        paramStr
            .append(key)
            .append('=')
            .append(URLEncoder.encode(requestParams.get(key).toString(), "utf-8"));
      } catch (UnsupportedEncodingException var18) {
        result =
            new StringBuilder(
                "{\"code\":-2300,\"location\":\"com.qcloud.Common.Request:129\",\"message\":\"api sdk throw exception! "
                    + var18.toString().replace("\"", "\\\"")
                    + "\"}");
      }
    }

    try {
      if (requestMethod.equals("GET")) {
        if (url.indexOf(63) > 0) {
          url = url + '&' + paramStr;
        } else {
          url = url + '?' + paramStr;
        }
      }
      requestUrl = url;
      URL realUrl = new URL(url);
      URLConnection connection;
      if (url.toLowerCase().startsWith("https")) {
        connection = realUrl.openConnection();
      } else {
        connection = realUrl.openConnection();
      }
      connection.setRequestProperty("accept", "*/*");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty(
          "user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      int connectTimeout = 5000;
      connection.setConnectTimeout(connectTimeout);
      int readTimeout = 90000;
      connection.setReadTimeout(readTimeout);
      if (requestMethod.equals("POST")) {
        ((HttpURLConnection) connection).setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(paramStr.toString().getBytes());
        out.flush();
        out.close();
      }
      connection.connect();
      String line;
      in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      while ((line = in.readLine()) != null) {
        result.append(line);
      }
    } catch (Exception var19) {
      result =
          new StringBuilder(
              "{\"code\":-2700,\"location\":\"com.qcloud.Common.Request:225\",\"message\":\"api sdk throw exception! "
                  + var19.toString().replace("\"", "\\\"")
                  + "\"}");
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (Exception var17) {
        result =
            new StringBuilder(
                "{\"code\":-2800,\"location\":\"com.qcloud.Common.Request:234\",\"message\":\"api sdk throw exception! "
                    + var17.toString().replace("\"", "\\\"")
                    + "\"}");
      }
    }
    rawResponse = result.toString();
    return result.toString();
  }
}
