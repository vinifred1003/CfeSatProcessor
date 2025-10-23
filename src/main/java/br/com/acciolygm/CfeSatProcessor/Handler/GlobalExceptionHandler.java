package br.com.acciolygm.CfeSatProcessor.Handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Exceções de regra de negócio
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Regra de Negócio Violada",
                "message", ex.getMessage()
        ));
    }

    // Erros de parsing XML
    @ExceptionHandler(XmlParsingException.class)
    public ResponseEntity<Map<String, Object>> handleXml(XmlParsingException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "error", "Erro ao processar XML de CF-e SAT",
                "message", ex.getMessage()
        ));
    }

    // Captura erros não tratados (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Erro interno do servidor",
                "message", ex.getMessage()
        ));
    }
}
