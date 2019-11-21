package ga.patrick.smns.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LatitudeValidator implements ConstraintValidator<LatitudeConstraint, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return -90.0 <= value && value <= 90.0;
    }

}
