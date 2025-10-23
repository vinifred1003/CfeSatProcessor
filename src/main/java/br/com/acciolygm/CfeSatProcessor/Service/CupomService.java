package br.com.acciolygm.CfeSatProcessor.Service;

import br.com.acciolygm.CfeSatProcessor.Handler.BusinessException;
import br.com.acciolygm.CfeSatProcessor.Handler.XmlParsingException;
import br.com.acciolygm.CfeSatProcessor.Repository.CupomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class CupomService {

    private final CupomRepository cupomRepository;
    private final XmlSatParser parser = new XmlSatParser();


    public void importDirectory(Path dir) {
        try (var stream = Files.walk(dir)) {
            stream.filter(p -> p.toString().endsWith(".xml"))
                    .forEach(this::processFileSafely);
        } catch (IOException e) {
            throw new BusinessException("Falha ao acessar diretório: " + dir);
        }
    }

    private void processFileSafely(Path path) {
        try {
            var result = parser.parse(path.toFile());

            if (result.canceld) {
                System.out.printf("⚠️ Cupom cancelado ignorado: %s%n", path.getFileName());
                return;
            }

            if (cupomRepository.existsByAccessKey(result.cupom.getAccessKey())) {
                System.out.printf("ℹ️ Cupom duplicado ignorado: %s%n", result.cupom.getAccessKey());
                return;
            }

            cupomRepository.save(result.cupom);
            System.out.printf("✅ Cupom importado: %s%n", path.getFileName());

        } catch (XmlParsingException e) {
            System.out.printf("❌ Erro ao processar %s: %s%n", path.getFileName(), e.getMessage());
        } catch (Exception e) {
            System.out.printf("❌ Erro inesperado no arquivo %s: %s%n",
                    path.getFileName(), e.getClass().getSimpleName());
        }
    }

    public void listByCfe() {
        cupomRepository.findAll(Sort.by(Sort.Direction.ASC, "cfe"))
                .forEach(c -> System.out.printf("CF-e %s | Chave %s | Emissão %s | Total %s%n",
                        c.getCfe(), c.getAccessKey(), c.getIssuedAt(), c.getTotal()));
    }

    public void listByValue() {
        cupomRepository.findAll(Sort.by(Sort.Direction.DESC, "total"))
                .forEach(c -> System.out.printf("Total %s | CF-e %s | Chave %s | Emissão %s%n",
                        c.getTotal(), c.getCfe(), c.getAccessKey(), c.getIssuedAt()));
    }
}
