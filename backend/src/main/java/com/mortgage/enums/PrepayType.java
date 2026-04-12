package com.mortgage.enums;

public enum PrepayType {
    SHORTEN_TERM(1, "缩期"),
    REDUCE_PAYMENT(2, "减月供");

    private final Integer code;
    private final String name;

    PrepayType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PrepayType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PrepayType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
