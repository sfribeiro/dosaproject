package br.upe.ecomp.dosa.controller.resultsanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Class responsible for converting the results of a simulation to the csv format.
 */
public class CSVParser {

    private HSSFWorkbook csv;
    private HSSFSheet sheet;
    private FileLineResultsAnalyzer resultsAnalyzer;

    /**
     * Creates a new instance of this class.
     */
    public CSVParser() {
        resultsAnalyzer = new FileLineResultsAnalyzer();
    }

    public void parse(String outputDirectory, String outputFile, List<File> files, String measurement, int step) {

        /* Cria o arquivo */
        this.csv = new HSSFWorkbook();

        writeRows(files, measurement, step);

        String csvFile = outputDirectory;
        if (outputFile.endsWith(".csv")) {
            csvFile += outputFile;
        } else {
            csvFile += outputFile + ".csv";
        }

        // Write the output to a file
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(csvFile);
            csv.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeRows(List<File> files, String measurement, int step) {
        createSheet("Results");

        double[] results = resultsAnalyzer.getData(files, measurement, resultsAnalyzer.getLastIteration(files), step);

        HSSFRow row;
        int rowNumber = 1;
        for (int i = 0; i < results.length; i++) {
            row = sheet.createRow(rowNumber++);
            writeCells(row, i, results[i]);
        }
    }

    private void writeCells(HSSFRow row, int sample, double fitness) {
        row.createCell(0).setCellValue(sample);
        row.createCell(1).setCellValue(fitness);
    }

    private void createSheet(String name) {
        this.sheet = csv.createSheet(name);

        // Configures the header.
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Sample");
        row.createCell(1).setCellValue("Fitness");
    }
}
