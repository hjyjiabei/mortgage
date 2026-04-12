package com.mortgage.enums;

public enum PlanStatus {
    VOID(0, "作废"),
    VALID(1, "有效"),
    SETTLED(2, "结清");

    private final Integer code;
    private final String name;

    PlanStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PlanStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PlanStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
