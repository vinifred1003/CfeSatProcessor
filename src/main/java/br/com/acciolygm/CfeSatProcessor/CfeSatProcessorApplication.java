package br.com.acciolygm.CfeSatProcessor;

import br.com.acciolygm.CfeSatProcessor.Service.CupomService;
import br.com.acciolygm.CfeSatProcessor.Utils.ZipUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;
import java.util.Scanner;

@SpringBootApplication
public class CfeSatProcessorApplication implements CommandLineRunner {
    private final CupomService service;

    @Value("${cfe.import.dir:./src/main/resources/Data/files_cfe}")
    private String defaultDir;

    @Value("${cfe.import.dir:./src/main/resources/Data/files_cfe_zipped/2024_12_28.zip}")
    private String defaultDirZip;

    /**
     * Este projeto tem como objetivo lêr arquivos XML de CF-e SAT (possivelmente dentro de arquivos ZIP),
     * extrair informações fiscais e as armazenar em banco de dados.
     */

    public CfeSatProcessorApplication(CupomService service) {
        this.service = service;
    }
	public static void main(String[] args) {
		SpringApplication.run(CfeSatProcessorApplication.class, args);
	}
    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("""
        ====== CF-e SAT Processor ======
        1) Importe XMLs de uma pasta
        2) Importe XMLs a partir de um arquivo ZIP
        3) Liste cupons por número do CF-e (ASC)
        4) Liste cupons por valor total (DESC)
        0) Sair
        Escolha: """);
            String opt = sc.nextLine().trim();

            switch (opt) {
                case "1" -> {
                    System.out.printf("Informe a pasta (ENTER para padrão: %s): ", defaultDir);
                    String in = sc.nextLine().trim();
                    String dir = in.isBlank() ? defaultDir : in;
                    service.importDirectory(Path.of(dir));
                }
                case "2" -> {
                    System.out.print("Informe o caminho do arquivo ZIP: ");
                    String in = sc.nextLine().trim();
                    String dirZip = in.isBlank() ? defaultDirZip : in;

                    Path extractedDir = ZipUtils.unzip(Path.of(dirZip));
                    System.out.println("✅ Arquivos extraídos para: " + extractedDir);
                    service.importDirectory(extractedDir);
                }
                case "3" -> service.listByCfe();
                case "4" -> service.listByValue();
                case "0" -> { System.out.println("Tchau!"); return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
}
