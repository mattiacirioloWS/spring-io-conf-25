package net.springio.conference.internal.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleAll(Exception ex, HttpServletRequest request) throws Exception {
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
            throw ex;
        }

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        problem.setDetail(ex.getMessage());
        problem.setProperty("path", request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problem);
    }
}
