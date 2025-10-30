package com.ringme.validationfield;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RangeDatePatternValidator implements ConstraintValidator<RangeDatePattern, String> {

    private String pattern;
    private boolean required;
    private String split;

    @Override
    public void initialize(RangeDatePattern datePattern) {
        this.pattern = datePattern.pattern();
        this.required = datePattern.required();
        this.split = datePattern.split();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return !required;
        }

        String[] patternParts = pattern.split(split);
        if (patternParts.length != 2) {
            return false;
        }
        String[] dateParts = value.split(split);
        if (dateParts.length != 2) {
            return false;
        }

        SimpleDateFormat dateFormat0 = new SimpleDateFormat(patternParts[0]);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat(patternParts[1]);
        dateFormat0.setLenient(false);
        dateFormat1.setLenient(false);

        try {
            Date startDate = dateFormat0.parse(dateParts[0].trim());
            Date endDate = dateFormat1.parse(dateParts[1].trim());

            return !startDate.after(endDate);
        } catch (ParseException e) {
            return false;
        }
    }
}
