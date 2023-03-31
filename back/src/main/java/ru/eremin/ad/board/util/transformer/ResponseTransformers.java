package ru.eremin.ad.board.util.transformer;

import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.input.route.dto.AdBoardResponseItem;
import ru.eremin.ad.board.input.route.dto.ErrorResponse;
import ru.eremin.ad.board.util.error.AdBoardException;
import ru.eremin.ad.board.util.error.CriticalException;
import ru.eremin.ad.board.util.error.Errors;

/**
 * Api response transformation
 */
@Log4j2
public class ResponseTransformers {

    /**
     * Transforming a response with an error signal into a typed service error
     */
    public static Function<Mono<ServerResponse>, Mono<ServerResponse>> errorResponseTransformer() {
        return (response) -> response
            .onErrorResume(err -> {
                if (err instanceof AdBoardException) {
                    final AdBoardException error = (AdBoardException) err;
                    log.error(error);
                    return ServerResponse.status(getResponseCode(error.getCode()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(AdBoardResponseItem.error(buildErrorResponse(error)));
                } else {
                    final CriticalException error = new CriticalException(err);
                    log.error(error);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(AdBoardResponseItem.error(buildErrorResponse(error)));
                }
            });
    }

    private static ErrorResponse buildErrorResponse(AdBoardException exception) {
        return new ErrorResponse()
            .setCode(exception.getCode())
            .setMessage(exception.getMessage())
            .setDisplayMessage(exception.getDisplayMessage());
    }

    private static HttpStatus getResponseCode(String errorCode) {
        var error = Errors.valueOf(errorCode);
        switch (error) {
            case BAD_REQUEST:
                return HttpStatus.BAD_REQUEST;
            case EMPTY_DURATION:
            case CATEGORY_DOES_NOT_EXIST:
            case AD_DOES_NOT_EXIST:
                return HttpStatus.INTERNAL_SERVER_ERROR;
            default:
                throw new IllegalStateException("Unexpected value: " + errorCode);
        }
    }
}
