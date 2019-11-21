package ga.patrick.smns.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TemperatureValidator implements ConstraintValidator<TemperatureConstraint, Double> {

    /**
     * Minimal values for different scales are: -273.15C, -459.67F and 0K.
     * Thus, valid temperature must be not less than -459.67.
     * At the moment we assume that all the values are in any one of these scales.
     */
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return -459.67 <= value;
    }

}
