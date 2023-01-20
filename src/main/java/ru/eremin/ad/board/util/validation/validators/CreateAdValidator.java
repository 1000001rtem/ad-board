package ru.eremin.ad.board.util.validation.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.eremin.ad.board.route.dto.CreateAdRequest;

@Component
public class CreateAdValidator implements Validator {
    @Override
    public boolean supports(final Class<?> clazz) {
        return CreateAdRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
            errors,
            "theme",
            "theme.empty",
            "Theme name can`t be empty"
        );
        ValidationUtils.rejectIfEmptyOrWhitespace(
            errors,
            "text",
            "text.empty",
            "Text name can`t be empty"
        );
        ValidationUtils.rejectIfEmpty(
            errors,
            "type",
            "type.empty",
            "Type name can`t be empty"
        );
        ValidationUtils.rejectIfEmpty(
            errors,
            "categoryId",
            "categoryId.empty",
            "categoryId name can`t be empty"
        );
    }
}
