package com.alert.alert.validation;

import com.alert.alert.entities.enums.PermissionType;
import com.alert.alert.validation.Validators.PermissionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PermissionValidator.class)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionCheck {
    String message() default "You do not have the right to do that";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    PermissionType value();
}
