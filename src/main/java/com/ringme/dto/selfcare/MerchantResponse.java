package com.ringme.dto.selfcare;

import com.ringme.enums.selfcare.NatcashResponseStatus;
import lombok.Data;

@Data
public class MerchantResponse {
    private NatcashResponseStatus status;
    private String code;
    private String message;
    private PaymentStatusData data;
}
