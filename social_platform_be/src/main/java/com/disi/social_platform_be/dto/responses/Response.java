package com.disi.social_platform_be.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {

    private T response;
    private Boolean isError;
    private String error;

    public Response() {
    }

    public Response(T response) {
        this.response = response;
        isError = false;
    }

    public Response(Exception exception) {
        this.isError = true;
        this.error = exception.getMessage();
    }
}
