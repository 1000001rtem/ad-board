package ru.eremin.ad.board.util.validation.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.eremin.ad.board.input.route.dto.UpgradeAdRequest;

@Component
public class UpgradeAdValidator implements Validator {
    @Override
    public boolean supports(final Class<?> clazz) {
        return UpgradeAdRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ValidationUtils.rejectIfEmpty(
            errors,
            "id",
            "id.empty",
            "id name can`t be empty"
        );
        ValidationUtils.rejectIfEmpty(
            errors,
            "duration",
            "duration.empty",
            "duration name can`t be empty"
        );
    }
}
