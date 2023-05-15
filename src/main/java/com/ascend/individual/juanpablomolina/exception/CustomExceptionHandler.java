package com.ascend.individual.juanpablomolina.exception;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import com.ascend.individual.juanpablomolina.dto.ErrorsDto;
import com.ascend.individual.juanpablomolina.dto.ExceptionDto;

/**
 * Controlador general de excepciones.
 *
 * <p>Desde esta clase se manejan la mayoría de las exceptiones que pueda tirar la
 * aplicación, y se respeta el contrato de salida especificado
 *
 * @author juan.molina
 *
 */
@ControllerAdvice
public class CustomExceptionHandler extends AbstractHandlerMethodExceptionResolver {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorsDto> handleAllExceptions(Exception ex, WebRequest request) {
        var errors = new ErrorsDto(new ArrayList<>());
        var errorDetails = new ExceptionDto(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        errors.addException(errorDetails);
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorsDto> handleRuntimeExceptionExceptions(RuntimeException ex, WebRequest request) {
        var errors = new ErrorsDto(new ArrayList<>());
        var errorDetails = new ExceptionDto(new Date(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        errors.addException(errorDetails);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<ErrorsDto> handleNullPointerExceptions(NullPointerException ex, WebRequest request) {
        var errors = new ErrorsDto(new ArrayList<>());
        var errorDetails = new ExceptionDto(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        errors.addException(errorDetails);
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenNotValidException.class)
    public final ResponseEntity<ErrorsDto> handleHttpMessageNotReadableExceptions(TokenNotValidException ex,
            WebRequest request) {
        var errors = new ErrorsDto(new ArrayList<>());
        var errorDetails = new ExceptionDto(new Date(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        errors.addException(errorDetails);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorsDto> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex,
            WebRequest request) {
        var errors = new ErrorsDto(new ArrayList<>());
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            var errorDetails = new ExceptionDto(new Date(), HttpStatus.BAD_REQUEST.value(),
                    error.getField() + ": " + error.getDefaultMessage());
            errors.addException(errorDetails);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Método para la compilación de esta clase.
     *
     * <p>Se tuvo que cambiar la clase desde la cual se extendía, porque al agregar
     * open-api, esta clase daba un referencia circular sobre ella misma.
     *
     * <p>Tal y como dice la documentación, se devuelve nulo para el procesamiento por
     * default.
     */
    @Override
    public ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Exception ex) {
        return null;
    }
}
