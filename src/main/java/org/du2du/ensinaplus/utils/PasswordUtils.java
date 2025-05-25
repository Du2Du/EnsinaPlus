package org.du2du.ensinaplus.utils;

import com.password4j.Hash;
import com.password4j.Password;

public class PasswordUtils {

  public static String hashPassword(String password) {
    try {
      Hash hash = Password.hash(password).addSalt("ensina-plus").withScrypt();
      return hash.getResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static Boolean comparePassword(String password, String hash) {
    try {
      return Password.check(password, hash).addSalt("ensina-plus").withScrypt();
    } catch (Exception e) {
      return false;
    }
  }
}
