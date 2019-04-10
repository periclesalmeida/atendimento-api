package br.com.periclesalmeida.atendimento.util;

import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MENSAGEM_OBJETO_NAO_ENCONTRADO = "Objeto não encontrado.";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Erro> erros = criarListaDeErros(ex.getBindingResult());
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ EmptyResultDataAccessException.class })
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String mensagemUsuario = MENSAGEM_OBJETO_NAO_ENCONTRADO;
        String mensagemDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({NegocioException.class})
    public final ResponseEntity<Object> handleNegocioExceptionException(NegocioException ex, WebRequest request) {
        List<Erro> erros = Arrays.asList(new Erro(ex.getMessage(), ex.toString()));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private List<Erro> criarListaDeErros(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();
        bindingResult.getFieldErrors().stream().forEach(fieldError ->  erros.add(new Erro(fieldError.getDefaultMessage(), fieldError.toString())));
        bindingResult.getGlobalErrors().stream().forEach(fieldError ->  erros.add(new Erro(fieldError.getDefaultMessage(), fieldError.toString())));
        return erros.stream().distinct().collect(Collectors.toList());
    }

    public static class Erro {
        private String errorUser;
        private String errorDeveloper;

        public Erro(String errorUser, String errorDeveloper) {
            this.errorUser = errorUser;
            this.errorDeveloper = errorDeveloper;
        }

        public String getErrorUser() {
            return errorUser;
        }
        public void setErrorUser(String errorUser) {
            this.errorUser = errorUser;
        }

        public String getErrorDeveloper() {
            return errorDeveloper;
        }
        public void setErrorDeveloper(String errorDeveloper) {
            this.errorDeveloper = errorDeveloper;
        }
    }
}
