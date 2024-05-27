package com.adman.craft.comments_mgmt.errorhandling;

import com.adman.craft.comments_mgmt.errorhandling.exception.InvalidCredentialException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adman.craft.comments_mgmt.errorhandling.exception.BadRequestException;
import com.adman.craft.comments_mgmt.errorhandling.exception.ExternalRuntimeException;
import com.adman.craft.comments_mgmt.errorhandling.exception.InternalRuntimeException;
import com.adman.craft.comments_mgmt.errorhandling.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler implements AccessDeniedHandler {

    @ExceptionHandler(InternalRuntimeException.class)
    public ResponseEntity<ProblemDetail> handleInternalRuntimeException(InternalRuntimeException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.error("[{}] {} ({}) : {}", exception.getClass().getName(), exception.getMessage(), errorCode.getMessage(), exception.getMessages(), exception);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                errorCode.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExternalRuntimeException.class)
    public ResponseEntity<ProblemDetail> handleExternalRuntimeException(ExternalRuntimeException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.error("[{}] {} ({}) : {}", exception.getClass().getName(), exception.getMessage(), errorCode.getMessage(),
                exception.getMessages().toString(), exception);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(getHttpStatus(errorCode), exception.getMessages().toString());
        return new ResponseEntity<>(pd, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> userAlreadyExistsException(UserAlreadyExistsException exception) {
        log.error("[{}] {} : {}", exception.getClass().getName(), exception.getMessage(), exception.getErrorCode().getMessage(), exception);
        HttpStatus httpStatus = getHttpStatus(exception.getErrorCode());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(httpStatus.value()), exception.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ProblemDetail> invalidCredentialException(InvalidCredentialException exception) {
        log.error("[{}] {} : {}", exception.getClass().getName(), exception.getMessage(), exception.getErrorCode().getMessage(), exception);
        HttpStatus httpStatus = getHttpStatus(exception.getErrorCode());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(httpStatus.value()), exception.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> badRequestException(BadRequestException exception) {
        log.error("[{}] {} : {}", exception.getClass().getName(), exception.getMessage(), exception.getErrorCode().getMessage(), exception);
        HttpStatus httpStatus = getHttpStatus(exception.getErrorCode());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(httpStatus.value()), exception.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("[{}] {}", exception.getClass().getName(), exception.getMessage(), exception);
        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors())
            errorMessages.add(error.getDefaultMessage());
        for (ObjectError error : exception.getBindingResult().getGlobalErrors())
            errorMessages.add(error.getDefaultMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), errorMessages.toString());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.error("[{}] {}", accessDeniedException.getClass().getName(), accessDeniedException.getMessage(), accessDeniedException);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()), "Access Denied!");
        response.getOutputStream().println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pd));
    }

    private HttpStatus getHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case USER_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case INVALID_CREDENTIALS -> HttpStatus.UNAUTHORIZED;
            case INTERNAL_SERVER_ERROR, UNKNOWN_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
