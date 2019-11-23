package ga.patrick.smns.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TemperatureValidator implements ConstraintValidator<TemperatureConstraint, Double> {

    /**
     * Minimal values for different scales are: -273.15C, -459.67F and 0K.
     * Update: all values are in Celsius, so minimal is -273.15.
     */
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return -273.15 <= value;
    }

}
