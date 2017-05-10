package com.njupt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * 异常捕获
 * @author weixk
 * @version Created time 17/5/9. Last-modified time 17/5/9.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = ConstraintViolationException.class)
    public void validHandler(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException exception) throws IOException {
        Set<ConstraintViolation<?>> set = exception.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = set.iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            ConstraintViolation<?> violation = iterator.next();
            String error = violation.getMessage();
            builder.append(error).append(";");
        }
        String err = builder.toString();
        log.error("400 - " + err);
        response.sendError(400, err);
    }
}
