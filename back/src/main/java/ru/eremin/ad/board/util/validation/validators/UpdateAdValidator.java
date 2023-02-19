package ru.eremin.ad.board.util.validation.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.eremin.ad.board.route.dto.UpdateAdRequest;

@Component
public class UpdateAdValidator implements Validator {
    @Override
    public boolean supports(final Class<?> clazz) {
        return UpdateAdRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ValidationUtils.rejectIfEmpty(
            errors,
            "id",
            "id.empty",
            "id name can`t be empty"
        );
    }
}
