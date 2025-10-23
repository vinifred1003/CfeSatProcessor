package br.com.acciolygm.CfeSatProcessor.utils;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    public static Path unzip(Path zipFilePath) throws IOException {
        if (!Files.exists(zipFilePath) || !zipFilePath.toString().endsWith(".zip")) {
            throw new FileNotFoundException("Arquivo ZIP não encontrado ou inválido: " + zipFilePath);
        }

        Path tempDir = Files.createTempDirectory("cfe_xmls_");
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;
                Path newFile = tempDir.resolve(entry.getName());
                Files.createDirectories(newFile.getParent());
                try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(newFile))) {
                    zis.transferTo(bos);
                }
            }
        }
        return tempDir;
    }

}
