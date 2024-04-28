package com.disi.social_platform_be.exception;

import com.disi.social_platform_be.dto.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Response> handleUnauthorizedExceptions(UnauthorizedException exception) {
        return new ResponseEntity<>(new Response(exception), HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Response> handleAuthenticationExceptions(InvalidTokenException exception) {
        return new ResponseEntity<>(new Response(exception), HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException exception) {
        var response = new Response(exception);
        var errors = exception.getFieldErrors().stream().map(e -> e.getField() + " " + e.getDefaultMessage()).toList();
        response.setError(errors.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InactiveAccountException.class, InvalidAccountException.class, InvalidResetTokenException.class, DuplicateAlbumException.class})
    public ResponseEntity<Response> handleBadRequestExceptions(Exception exception) {
        return new ResponseEntity<>(new Response(exception), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Response> handleConflictExceptions(Exception exception) {
        return new ResponseEntity<>(new Response(exception), HttpStatus.CONFLICT);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler({EmailSendingException.class, ProfilePictureScalingException.class, ImageCreateException.class})
    public ResponseEntity<Response> handleInternalServerError(Exception exception) {
        return new ResponseEntity<>(new Response(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handle(Exception exception) {
        return new ResponseEntity<>(new Response(exception), HttpStatus.NOT_FOUND);
    }
}
