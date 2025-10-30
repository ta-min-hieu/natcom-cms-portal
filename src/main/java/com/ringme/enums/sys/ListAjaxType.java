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
public enum ListAjaxType implements CodeEnum {
    ID_AND_TEXT(0),
    TEXT(1);

    private final int type;

    private static final Map<Integer, ListAjaxType> CODE_MAP = new HashMap<>();

    static {
        for (ListAjaxType value : ListAjaxType.values())
            CODE_MAP.put(value.type, value);
    }

    @JsonCreator
    public static ListAjaxType fromCode(Integer type) {
        ListAjaxType result = CODE_MAP.get(type);
        if (result == null)
            throw new IllegalArgumentException("Invalid code: " + type);

        return result;
    }

    @JsonValue
    public Integer getType() {
        return type;
    }
}
