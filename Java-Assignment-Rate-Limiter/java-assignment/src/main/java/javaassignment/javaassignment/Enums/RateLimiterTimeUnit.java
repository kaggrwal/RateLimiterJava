package javaassignment.javaassignment.Enums;

public enum RateLimiterTimeUnit {

  SECOND(1000),
  MINUTE(60000),
  HOUR(3600000);

  private long value;

  RateLimiterTimeUnit(int i) {
    this.value = i;
  }

  public long getValue() {
    return value;
  }
}
