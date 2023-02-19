package ru.eremin.ad.board.util.validation.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.eremin.ad.board.route.dto.CreateCategoryRequest;

@Component
public class CreateCategoryValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return CreateCategoryRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
            errors,
            "categoryName",
            "categoryName.empty",
            "Category name can`t be empty"
        );
    }
}
