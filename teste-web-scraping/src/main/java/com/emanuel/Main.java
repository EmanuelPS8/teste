package com.emanuel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/*
* Biblioteca usada JSOUP, para acessar e processar o HTML
* */

public class Main {
    public static void main(String[] args) {

        try{
            //acessar o site da ANS
            String url = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
            Document doc = Jsoup.connect(url).get();

            //encontrar todos os links que terminam com .pdf
            Elements links = doc.select("a[href$=.pdf]");

            //cria uma pasta downloads
            Path downloadDir = Files.createDirectories(Paths.get("src/main/java/com/emanuel/downloads"));

            File outputDir = new File("src/main/java/com/emanuel/output");
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }

            for (Element link : links) {
                //filtar os links anexo i e anexo ii
                String linkText = link.text().toLowerCase();
                if (linkText.contains("anexo i") || linkText.contains("anexo ii")) {
                    //define o path onde vai ser salvo
                    String pdfUrl = link.absUrl("href");
                    String fileName = pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1);
                    Path filePath = downloadDir.resolve(fileName);

                    System.out.println("Baixando: " + filePath);

                    //baixa o pdf na pasata downloads
                    try(InputStream is = new URL(pdfUrl).openStream()) {
                        Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }

            //cria um arquivo .zip
            Path zipPath = Paths.get("src/main/java/com/emanuel/output/anexos.zip");
            //leitura dos aquivos da pasta e adiiciona no zip
            try(ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                Files.list(downloadDir).forEach(path -> {
                    try(InputStream fis = Files.newInputStream(path)){
                        ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                        zos.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            System.out.println("Compactação finalizada em: " + zipPath.toAbsolutePath());
        }catch (Exception e){
            System.out.println(e);
        }
        }
    }
