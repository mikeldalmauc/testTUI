package com.tui.proof.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
   * Example of custom validation tag,
   * ideally this should be externalized 
   */
  @Documented
  @Constraint(validatedBy = PilotesAmountValidator.class)
  @Target( { ElementType.METHOD, ElementType.FIELD })
  @Retention(RetentionPolicy.RUNTIME)
  public @interface PilotesAmountConstraint {
    String message() default "Invalid number of pilotes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
  }
