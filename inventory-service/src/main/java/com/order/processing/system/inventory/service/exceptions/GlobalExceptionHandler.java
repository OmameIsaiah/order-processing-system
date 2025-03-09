package com.order.processing.system.inventory.service.exceptions;

import com.order.processing.system.inventory.service.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
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
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestExceptions(BadRequestException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity handleDuplicateRecordExceptions(DuplicateRecordException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentExceptions(BadRequestException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handlerGlobalErrors(Exception exception) {
        exception.printStackTrace();
        log.warn("An error occur  {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, exception.getMessage()));
    }
}
