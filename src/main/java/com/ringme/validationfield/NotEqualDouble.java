package com.ringme.validationfield;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotEqualDoubleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEqualDouble {

    String message() default "- Value must not be {value}.";

    boolean required() default true;

    double value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
