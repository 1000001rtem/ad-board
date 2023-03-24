package ru.eremin.ad.board.util.validation;

import reactor.core.publisher.Mono;

public interface ValidationService {

    <T> Mono<T> validate(T t);
}
