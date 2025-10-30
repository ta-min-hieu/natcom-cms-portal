package com.ringme.enums.selfcare;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ringme.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ScheduleCallStatus implements CodeEnum {
    NEW(0),
    IN_PROCESS(1),
    SUCCESS(2),
    CANCEL(3);

    private final int type;

    private static final Map<Integer, ScheduleCallStatus> CODE_MAP = new HashMap<>();

    static {
        for (ScheduleCallStatus value : ScheduleCallStatus.values())
            CODE_MAP.put(value.type, value);
    }

    @JsonCreator
    public static ScheduleCallStatus fromCode(int type) {
        ScheduleCallStatus result = CODE_MAP.get(type);
        if (result == null)
            throw new IllegalArgumentException("Invalid code: " + type);

        return result;
    }

    @JsonValue
    public Integer getType() {
        return type;
    }
}
