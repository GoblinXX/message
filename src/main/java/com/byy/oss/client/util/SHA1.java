package com.byy.oss.client.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class SHA1 {

  public static String stringToSHA(String str) {
    try {
      byte[] strTemp = str.getBytes();
      MessageDigest mdTemp = MessageDigest.getInstance("SHA-1");
      mdTemp.update(strTemp);
      return toHexString(mdTemp.digest());
    } catch (Exception var3) {
      return null;
    }
  }

  public static String fileNameToSHA(String fileName) {
    try (FileInputStream inputStream = new FileInputStream(fileName)) {
      return streamToSHA(inputStream);
    } catch (IOException e) {
      return null;
    }
  }

  public static String streamToSHA(InputStream inputStream) {
    try {
      MessageDigest mdTemp = MessageDigest.getInstance("SHA-1");
      byte[] buffer = new byte[1024];
      int numRead;
      while ((numRead = inputStream.read(buffer)) > 0) {
        mdTemp.update(buffer, 0, numRead);
      }
      return toHexString(mdTemp.digest());
    } catch (Exception var4) {
      return null;
    }
  }

  private static String toHexString(byte[] md) {
    char[] hexDigits =
        new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    int j = md.length;
    char[] str = new char[j * 2];

    for (int i = 0; i < j; ++i) {
      byte byte0 = md[i];
      str[2 * i] = hexDigits[byte0 >>> 4 & 15];
      str[i * 2 + 1] = hexDigits[byte0 & 15];
    }
    return new String(str);
  }
}
