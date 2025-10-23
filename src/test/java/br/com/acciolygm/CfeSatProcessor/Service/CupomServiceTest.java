package br.com.acciolygm.CfeSatProcessor.Service;

import br.com.acciolygm.CfeSatProcessor.Handler.BusinessException;
import br.com.acciolygm.CfeSatProcessor.Model.Cupom;

import br.com.acciolygm.CfeSatProcessor.Repository.CupomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.File;
import java.nio.file.Path;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CupomServiceTest {

        @Mock
        private CupomRepository cupomRepository;

        @Mock
        private XmlSatParser parser;

        @InjectMocks
        private CupomService service;

        @BeforeEach
        void setup() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void shouldIgnoreDuplicatedCupom() throws Exception {
            // Arrange
            Cupom cupom = Cupom.builder().accessKey("123").build();
            XmlSatParser.ParseResult result = new XmlSatParser.ParseResult();
            result.cupom = cupom;
            result.canceld = false;

            when(parser.parse(any(File.class))).thenReturn(result);
            when(cupomRepository.existsByAccessKey("123")).thenReturn(true);

            // Act + Assert
            assertThatCode(() -> service.importDirectory(Path.of("src/main/resources/Data/files_cfe")))
                    .doesNotThrowAnyException();
        }

        @Test
        void shouldThroughBusinessExceptionWhenAccessFolderFailed() {
            // Act + Assert
            assertThatThrownBy(() -> service.importDirectory(Path.of("pasta-inexistente")))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Falha ao acessar diretório");
        }
}

