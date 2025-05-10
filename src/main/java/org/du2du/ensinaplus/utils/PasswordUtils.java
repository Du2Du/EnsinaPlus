package org.du2du.ensinaplus.utils;

import java.security.MessageDigest;

public class PasswordUtils {

  public static String hashPassword(String password) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(password.getBytes());
      String stringHash = new String(messageDigest.digest());
      return stringHash;
    } catch (Exception e) {
      return null;
    }
  }
}
