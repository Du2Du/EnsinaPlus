package org.du2du.ensinaplus.model.enums;

public enum RoleEnum {
  ADMIN("admin"),
  TEACHER("teacher"),
  STUDENT("student");

  public static final String ROLE_ADMIN = "admin";
  public static final String ROLE_TEACHER = "teacher";
  public static final String ROLE_STUDENT = "student";

  private final String value;

  RoleEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
