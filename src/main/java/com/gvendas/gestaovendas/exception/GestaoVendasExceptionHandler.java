package com.gvendas.gestaovendas.exception;

import org.hibernate.validator.constraints.Length;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.FacesWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// anotacao usada para ouvir e verificar se toda excecao no projeto tem tratamento
@ControllerAdvice
public class GestaoVendasExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String VALIDATION_NOT_BLANK = "NotBlank";
    public static final String VALIDATION_LENGTH = "Length";
    public static final String VALIDATION_NOT_NULL = "NotNull";
    public static final String VALIDATION_PATTERN = "Pattern";
    public static final String VALIDATION_MIN = "Min";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Error> errors = gerarListaDeErros(ex.getBindingResult());
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request){
        String msgUser = "Recurso nao encontrado";
        String msgDev = ex.toString();
        List<Error> errors = Arrays.asList(new Error(msgUser,msgDev));
        return handleExceptionInternal(ex,errors,new HttpHeaders(),HttpStatus.NOT_FOUND,request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){
        String msgUser = "Recurso nao encontrado";
        String msgDev = ex.toString();
        List<Error> errors = Arrays.asList(new Error(msgUser,msgDev));
        return handleExceptionInternal(ex,errors,new HttpHeaders(),HttpStatus.NOT_FOUND,request);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Object> handleRegraNegocioException(RegraNegocioException ex, WebRequest  request){
        String msgUser = ex.getMessage();
        String msgDev = ex.getMessage();
        List<Error> errors = Arrays.asList(new Error(msgUser,msgDev));
        return handleExceptionInternal(ex,errors,new HttpHeaders(),HttpStatus.BAD_REQUEST,request);
    }

    private List<Error> gerarListaDeErros(BindingResult bindingResult) {
        List<Error> errors = new ArrayList<Error>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            String msgUser = handleUserMessage(fieldError);
            String msgDev = fieldError.toString();
            errors.add(new Error(msgUser, msgDev));
        });
        return errors;
    }

    private String handleUserMessage(FieldError fieldError) {
        if (fieldError.getCode().equals(VALIDATION_NOT_BLANK)) {
            return fieldError.getDefaultMessage().concat(" é obrigatorio");
        }
        if (fieldError.getCode().equals(VALIDATION_NOT_NULL)) {
            return fieldError.getDefaultMessage().concat(" é obrigatorio");
        }
        if (fieldError.getCode().equals(VALIDATION_LENGTH)) {
            return fieldError.getDefaultMessage().concat(String.format(" Deve ter entre %s e %s caracteres",
                    fieldError.getArguments()[2], fieldError.getArguments()[1]));
        }
        if (fieldError.getCode().equals(VALIDATION_PATTERN)) {
            return fieldError.getDefaultMessage().concat(String.format(" Formato inválido.",
                    fieldError.getArguments()[2], fieldError.getArguments()[1]));
        }
        if (fieldError.getCode().equals(VALIDATION_MIN)) {
            return fieldError.getDefaultMessage().concat(String.format("Quantidade deve ser " +
                    "maior ou igual a %s",fieldError.getArguments()[1]));
        }
        return fieldError.toString();
    }
}
