package org.du2du.ensinaplus.model.enums;

public enum TypeResourceEnum {
  FILE("FILE"),
  VIDEO("VIDEO");

  private final String value;

  TypeResourceEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
