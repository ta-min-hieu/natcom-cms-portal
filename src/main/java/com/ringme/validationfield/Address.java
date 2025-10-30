package com.ringme.validationfield;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AddressConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Address {
        //error message
        //public String message() default "*Không bao gồm các ký tự đặc biệt!";
        public String message() default "{validaitonAddress}";
        //represents group of constraint
        public Class<?>[] groups() default {};

        //represents additional information about annotation
        public Class<? extends Payload>[] payload() default {};
}
