package com.emanuel;

import com.opencsv.CSVWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
* Bibliotecas:
* OpenCSV: para a escrita de arquivos .csv
* Apache PDFBox: para leitura de .pdf
* Tabula: extrais tabelas de dentre de um PDF
* */
public class Main {
    public static void main(String[] args) {


        File pastaSaida = new File("src/main/java/com/emanuel/output");
        if (!pastaSaida.exists()) {
            pastaSaida.mkdir();
        }

        //arquivos PDF
        String pdfPath = "src/main/java/com/emanuel/arquivos/Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";

        //.csv a ser salvo
        String csvPath = "src/main/java/com/emanuel/output/Anexo_I_Rol_2021RN_465.csv";

        //.zip do csv
        String zipPath = "src/main/java/com/emanuel/output/Teste_Emanuel.zip";


        //armazenar as linhas
        List<String[]> todasLinhas = new ArrayList<String[]>();

        //leitura do PDF
        try {
            PDDocument document = PDDocument.load(new File(pdfPath));
            ObjectExtractor extractor = new ObjectExtractor(document);

            //planilha
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

            //extrair o conteudos de todas a paginas
            for (int i = 1; i <= document.getNumberOfPages(); i++){
                Page page = extractor.extract(i);

                //tabela das paginas
                List<Table> tables = sea.extract(page);


                for (Table table : tables){
                    //obtem as linha da tabela
                    List<List<RectangularTextContainer>> linhas = table.getRows();

                    //cada linhda da tabela
                    for (List<RectangularTextContainer> linha : linhas){
                        //armazenar cada valor de cada celeula da tabale
                        List<String> cols = new ArrayList<>();

                        //cada celula da linha
                        for (RectangularTextContainer cell : linha){
                            //chamar função para substituir as siglas
                            cols.add(tratarSiglas(cell.getText()));
                        }

                        //add a linha processada na lista de todas as linhas
                        todasLinhas.add(cols.toArray(new String[0]));
                    }
                }
            }

            //salvando no .csv
            try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csvPath), StandardCharsets.UTF_8))) {
                for (String[] linha : todasLinhas) {
                    writer.writeNext(linha);
                }
            }


            try(ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath))){
                //cria arquivo para ser compactado
                File fileToZip = new File(csvPath);

                //abre para leitura do arquivo CSV
                try(FileInputStream fis = new FileInputStream(fileToZip)){
                    //nova entrada para arquivo zip
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    //le arquivo csv e escreve o ZIP
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0){
                        zipOut.write(bytes, 0, length);
                    }
                }
            }

            System.out.println("Processo concluido!!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    private static String tratarSiglas(String valor){
        //Mapeia as siglas
        Map<String, String> siglas = Map.of(
                "OD", "Seg. Odontológica",
                "AMB", "Seg. Ambulatorial",
                "HCO", "Seg. Hospitalar Com Obstetrícia",
                "HSO", "Seg. Hospitalar Sem Obstetrícia",
                "REF", "Plano Referência",
                "PAC", "Procedimento de Alta Complexidade",
                "DUT", "Diretriz de Utilização"
        );

        //substitui pela descicao completa
        for (Map.Entry<String, String> sigla : siglas.entrySet()){
            valor = valor.replace(sigla.getKey(), sigla.getValue());
        }

        return valor;
    }
}
