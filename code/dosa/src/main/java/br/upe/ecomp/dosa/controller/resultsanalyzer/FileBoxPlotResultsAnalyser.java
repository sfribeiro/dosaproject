/**
 * Copyright (C) 2010
 * Swarm Intelligence Team (SIT)
 * Department of Computer and Systems
 * University of Pernambuco
 * Brazil
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package br.upe.ecomp.dosa.controller.resultsanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import br.upe.ecomp.doss.core.exception.InfraException;

/**
 * Class to pre process files results.
 * 
 * @author Rodrigo Castro
 */
public class FileBoxPlotResultsAnalyser extends FileResultsAnalyzer implements ICompoundDataSet {

    /**
     * {@inheritDoc}
     */
    public double[][] getData(List<File> files, String measurement, Integer lastIteration, int step) {
        int iterations = lastIteration / step;

        // We want to guarantee that the last iteration is always computed.
        if (lastIteration % step != 0) {
            iterations += 1;
        }

        double[][] results = new double[iterations][files.size()];
        for (int i = 0; i < iterations; i++) {
            results[i] = new double[files.size()];
        }

        int currentFile = 0;
        for (File file : files) {
            readResults(file, measurement, lastIteration, results, step, currentFile);
            currentFile++;
        }
        return results;
    }

    private void readResults(File file, String measurement, Integer lastIteration, double[][] results, int step,
            int currentFile) {
        BufferedReader reader = null;
        String measurementValue = null;
        String line = null;
        String measurementLine = measurement + ":";
        try {
            reader = new BufferedReader(new FileReader(file));
            int i = 0;
            int iteration = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(measurementLine)) {
                    measurementValue = line.split(RESULT_TOKEN)[1].trim();
                    if (iteration % step == 0) {
                        results[i++][currentFile] = Double.parseDouble(measurementValue);
                    }
                    iteration += 1;
                }
            }

            // Guarantees that the last iteration is always computed.
            // if ((iteration - 1) % step != 0) {
            // results[i][currentFile] = Double.parseDouble(measurementValue);
            // }

        } catch (FileNotFoundException cause) {
            throw new InfraException("Unable to find the file: " + file.getName(), cause);
        } catch (IOException cause) {
            throw new InfraException("Error while reading the file: " + file.getName(), cause);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Log!
                    e.printStackTrace();
                }
            }
        }
    }
}
