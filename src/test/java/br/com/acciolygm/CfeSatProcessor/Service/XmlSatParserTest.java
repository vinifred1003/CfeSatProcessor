package br.com.acciolygm.CfeSatProcessor.Service;

import br.com.acciolygm.CfeSatProcessor.Model.Cupom;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlSatParserTest {
    @Test
    void shouldExtractCupomDataCorrectly() throws Exception {
        XmlSatParser parser = new XmlSatParser();
        File xml = new File("src/main/resources/Data/files_cfe/CFe35241260892858000130590014453640106882115133.xml");

        var result = parser.parse(xml);

        assertThat(result.canceld).isFalse();
        assertThat(result.cupom).isNotNull();

        Cupom c = result.cupom;
        assertThat(c.getAccessKey()).isNotBlank();
        assertThat(c.getCfe()).isNotBlank();
        assertThat(c.getTotal()).isGreaterThan(BigDecimal.ZERO);
        assertThat(c.getItems()).isNotEmpty();
    }
}
