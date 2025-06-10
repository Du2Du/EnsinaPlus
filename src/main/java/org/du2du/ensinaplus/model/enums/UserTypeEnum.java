package org.du2du.ensinaplus.model.enums;

public enum UserTypeEnum {
  ADMIN("admin"),
  SUPER_ADMIN("super-admin"),
  COMMON("common");

  private final String value;

  UserTypeEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
