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
public enum FtthRegisterStatus implements CodeEnum {
    NEW(0),
    CALLING(1),
    UNREACHABLE(2),
    SURVEYING(3),
    SIGN_CONTRACT(4),
    DEPLOYING(5),
    SUCCESS(6),
    CANCEL(7),
    ;

    private final int type;

    private static final Map<Integer, FtthRegisterStatus> CODE_MAP = new HashMap<>();

    static {
        for (FtthRegisterStatus value : FtthRegisterStatus.values())
            CODE_MAP.put(value.type, value);
    }

    @JsonCreator
    public static FtthRegisterStatus fromCode(int type) {
        FtthRegisterStatus result = CODE_MAP.get(type);
        if (result == null)
            throw new IllegalArgumentException("Invalid code: " + type);

        return result;
    }

    @JsonValue
    public Integer getType() {
        return type;
    }
}
