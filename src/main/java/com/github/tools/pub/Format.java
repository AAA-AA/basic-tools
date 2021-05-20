package com.github.tools.pub;

public enum Format {
    DATE_FORMAT("yyyy-MM-dd"),
    DATE_YYYYMMDD("yyyyMMdd"),
    DATE_YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    DATE_TIME_FORMAT("yyyy-MM-dd HH:mm:ss"),
    DATE_TIME_NO_SS("yyyy-MM-dd HH:mm"),
    DATE_TIME_SSSS_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private String value;

    Format(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
