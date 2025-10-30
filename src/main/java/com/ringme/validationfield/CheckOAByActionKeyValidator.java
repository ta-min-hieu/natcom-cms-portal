package com.ringme.validationfield;

import com.ringme.common.AppUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
@Log4j2
public class CheckOAByActionKeyValidator implements ConstraintValidator<CheckOAByActionKey, Object> {
    @Override
    public boolean isValid(Object annotatedObject, ConstraintValidatorContext context) {
        try {
            String actionKey = (String) AppUtils.getFieldValue(annotatedObject, "actionKey").orElse(null);
            Long oaId = (Long) AppUtils.getFieldValue(annotatedObject, "oaId").orElse(null);
            if (actionKey != null && actionKey.startsWith("OA")) {
                return oaId != null;
            }
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            return false;
        }
    }
}