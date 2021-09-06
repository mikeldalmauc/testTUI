package com.tui.proof.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PilotesAmountValidator implements ConstraintValidator<PilotesAmountConstraint, Integer> {

    @Override
    public boolean isValid(Integer pilotes, ConstraintValidatorContext cxt) {
        return pilotes != null && (pilotes == 5 || pilotes == 10 || pilotes == 15);
    }
  }