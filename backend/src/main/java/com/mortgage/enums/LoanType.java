package com.mortgage.enums;

public enum LoanType {
    COMMERCIAL(1, "商业贷款"),
    PROVIDENT_FUND(2, "公积金贷款"),
    COMBINATION(3, "组合贷款");

    private final Integer code;
    private final String name;

    LoanType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static LoanType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (LoanType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
