package ru.eremin.ad.board.util.validation.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.util.error.Errors;
import ru.eremin.ad.board.util.validation.ValidationService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestValidationService implements ValidationService {

    private final List<Validator> validators;

    @Override
    public <T> Mono<T> validate(final T t) {
        var errors = new BeanPropertyBindingResult(t, t.getClass().getName());
        validators.forEach(validator -> {
            if (validator.supports(t.getClass())) {
                validator.validate(t, errors);
            }
        });
        if (errors.hasErrors()) {
            var fieldsWithError = errors.getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));
            var thr = Errors.BAD_REQUEST.format(fieldsWithError).asException();
            return Mono.error(thr);
        } else {
            return Mono.just(t);
        }
    }
}
