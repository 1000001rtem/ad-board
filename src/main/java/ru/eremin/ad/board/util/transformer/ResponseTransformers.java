package ru.eremin.ad.board.util.transformer;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.eremin.ad.board.route.dto.AdBoardResponseItem;
import ru.eremin.ad.board.route.dto.ErrorResponse;
import ru.eremin.ad.board.util.error.AdBoardException;
import ru.eremin.ad.board.util.error.CriticalException;

import java.util.function.Function;

/**
 * Api response transformation
 */
public class ResponseTransformers {

    /**
     * Transforming a response with an error signal into a typed service error
     */
    public static Function<Mono<ServerResponse>, Mono<ServerResponse>> errorResponseTransformer() {
        return (response) -> response
            .onErrorResume(err -> {
                if (err instanceof AdBoardException) {
                    final AdBoardException error = (AdBoardException) err;
                    final ErrorResponse errorResponse = new ErrorResponse()
                        .setCode(error.getCode())
                        .setMessage(error.getMessage())
                        .setDisplayMessage(error.getDisplayMessage());
                    return ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(AdBoardResponseItem.error(errorResponse));
                } else {
                    final CriticalException error = new CriticalException(err);
                    final ErrorResponse errorResponse = new ErrorResponse()
                        .setCode(error.getCode())
                        .setMessage(error.getMessage());
                    err.printStackTrace();
                    return ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(AdBoardResponseItem.error(errorResponse));
                }
            });
    }
}
