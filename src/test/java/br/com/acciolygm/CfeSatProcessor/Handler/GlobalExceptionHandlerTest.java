package br.com.acciolygm.CfeSatProcessor.Handler;


import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
public class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mustHandleBusinessExceptionComStatus400() {
        var response = handler.handleBusiness(new BusinessException("Erro de negócio"));
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("error", "Regra de Negócio Violada");
    }

    @Test
    void mustHandleXmlParsingExceptionComStatus422() {
        var response = handler.handleXml(new XmlParsingException("Erro XML", null));
        assertThat(response.getStatusCode().value()).isEqualTo(422);
        assertThat(response.getBody()).containsEntry("error", "Erro ao processar XML de CF-e SAT");
    }

    @Test
    void mustHandleExceptionGenericaComStatus500() {
        var response = handler.handleGeneric(new RuntimeException("Erro interno"));
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).containsEntry("error", "Erro interno do servidor");
    }
}
