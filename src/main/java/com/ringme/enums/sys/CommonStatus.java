package com.ringme.enums.sys;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ringme.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum CommonStatus implements CodeEnum {
    ACTIVE(1),
    INACTIVE(0);

    private final int type;

    private static final Map<Integer, CommonStatus> CODE_MAP = new HashMap<>();

    static {
        for (CommonStatus value : CommonStatus.values())
            CODE_MAP.put(value.type, value);
    }

    @JsonCreator
    public static CommonStatus fromCode(Integer type) {
        CommonStatus result = CODE_MAP.get(type);
        if (result == null)
            throw new IllegalArgumentException("Invalid code: " + type);

        return result;
    }

    @JsonValue
    public Integer getType() {
        return type;
    }
}
