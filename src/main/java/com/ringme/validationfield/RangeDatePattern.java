package com.ringme.validationfield;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RangeDatePatternValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RangeDatePattern {

    String message() default "- Invalid date format.";

    String split() default " - ";

    boolean required() default true;

    String pattern() default "dd/MM/yyyy HH:mm:ss - dd/MM/yyyy HH:mm:ss";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
