package com.order.processing.system.account.service.exceptions;

import com.order.processing.system.account.service.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {
        return ResponseEntity.status(statusCode)
                .body(new ApiResponse<>(false, statusCode.value(), HttpStatus.valueOf(statusCode.value()), ex.getMessage()));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity handleRecordNotFoundExceptions(RecordNotFoundException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn("{} access through {}", exception.getMessage(), requestUrl);
        //exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestExceptions(BadRequestException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        //exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity handleDuplicateRecordExceptions(DuplicateRecordException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        //exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedExceptions(UnauthorizedException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        //exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedExceptions(AccessDeniedException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        //exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentExceptions(BadRequestException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        //exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchExceptions(MethodArgumentTypeMismatchException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        String error = exception.getName() + " should be of type " + exception.getRequiredType().getName();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        //exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, error));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handlerGlobalErrors(Exception exception) {
        //exception.printStackTrace();
        log.warn("An error occur  {}", exception.fillInStackTrace());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, exception.getMessage()));
    }
}
