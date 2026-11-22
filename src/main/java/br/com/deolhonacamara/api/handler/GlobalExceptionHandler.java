package br.com.deolhonacamara.api.handler;

import br.com.deolhonacamara.api.BusinessCode;
import br.com.deolhonacamara.api.model.ErrorResponse;
import br.com.deolhonacamara.exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("⚠️ Exceção de negócio capturada - Código: {} - Mensagem: {}", ex.getCode(), ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse,  BusinessCode.TOKEN_EXPIRED.equals(ex.getCode()) ? HttpStatus.UNAUTHORIZED: HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleStatus(ResponseStatusException ex) {
        String code = ex.getStatusCode().toString();
        return new ResponseEntity<>(new ErrorResponse(code, ex.getReason()), ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("💥 Exceção genérica capturada - Tipo: {} - Mensagem: {}", ex.getClass().getSimpleName(),
                ex.getMessage());
        log.error("📍 Stack trace:", ex);

        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_ERROR", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}