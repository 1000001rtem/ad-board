package ru.eremin.ad.board.route.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AdBoardResponseItem<T> {
    ResponseStatus status;
    T data;
    ErrorResponse error;

    private AdBoardResponseItem(ResponseStatus status, T data, ErrorResponse error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <K> AdBoardResponseItem<K> success(K data) {
        return new AdBoardResponseItem<>(ResponseStatus.SUCCESS, data, null);
    }

    public static AdBoardResponseItem error(ErrorResponse error) {
        return new AdBoardResponseItem(ResponseStatus.ERROR, null, error);
    }
}

enum ResponseStatus {
    ERROR, SUCCESS
}
