package com.mortgage.enums;

public enum RepaymentMethod {
    EQUAL_PRINCIPAL_INTEREST(1, "等额本息"),
    EQUAL_PRINCIPAL(2, "等额本金");

    private final Integer code;
    private final String name;

    RepaymentMethod(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static RepaymentMethod fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (RepaymentMethod method : values()) {
            if (method.getCode().equals(code)) {
                return method;
            }
        }
        return null;
    }
}
