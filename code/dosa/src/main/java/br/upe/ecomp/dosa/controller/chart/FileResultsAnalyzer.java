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
package br.upe.ecomp.dosa.controller.chart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.upe.ecomp.doss.core.exception.InfraException;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public class FileResultsAnalyzer implements IResultsAnalyzer {

    private static final String MEASUREMENTS = "Measurements:";
    protected static final String RESULT_TOKEN = ":";

    /**
     * {@inheritDoc}
     */
    public List<String> searchCommonsMeasurements(List<File> files) {
        List<List<String>> measurementLists = new ArrayList<List<String>>();
        for (File file : files) {
            measurementLists.add(getMeasurementList(file));
        }
        return findCommonsMeasurements(measurementLists);
    }

    /**
     * {@inheritDoc}
     */
    public Integer getLastIteration(List<File> files) {
        Set<Integer> lastIterations = new HashSet<Integer>();
        Integer current;
        for (File file : files) {
            current = getLastIteration(file);
            if (current != null) {
                lastIterations.add(current);
            }
        }
        return getLastCommonIteration(new ArrayList<Integer>(lastIterations));
    }

    private List<String> getMeasurementList(File file) {
        List<String> measurementList = new ArrayList<String>();
        BufferedReader reader = null;
        String measurements = null;
        String line = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(MEASUREMENTS)) {
                    measurements = line.split(RESULT_TOKEN)[1].trim();
                    break;
                }
            }
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

        for (String measurement : measurements.split(",")) {
            measurementList.add(measurement.trim());
        }
        return measurementList;
    }

    /**
     * Given the list of lists of measurements, returns the list of measurements that appears on all
     * files.
     * 
     * @param measurementLists The list of lists of measurements.
     * @return The list of measurements that appears on all files.
     */
    protected List<String> findCommonsMeasurements(List<List<String>> measurementLists) {
        List<String> measurements = new ArrayList<String>();
        boolean common;
        List<String> largestList = getLargestList(measurementLists);
        for (String measurement : largestList) {
            common = true;
            for (List<String> current : measurementLists) {
                if (!current.contains(measurement)) {
                    common = false;
                    break;
                }
            }
            if (common) {
                measurements.add(measurement);
            }
        }

        return measurements;
    }

    private List<String> getLargestList(List<List<String>> measurementLists) {
        List<String> largestList = new ArrayList<String>();
        for (List<String> current : measurementLists) {
            if (current.size() > largestList.size()) {
                largestList = current;
            }
        }
        return largestList;
    }

    private Integer getLastIteration(File file) {
        Integer lastIteration = null;
        String lastIterationLine = null;

        long position;
        RandomAccessFile fileReader = null;
        try {
            fileReader = new RandomAccessFile(file, "r");
            position = fileReader.length();
            do {
                fileReader.seek(position--);
                lastIterationLine = fileReader.readLine();
            } while (position >= 0 && (lastIterationLine == null || !lastIterationLine.startsWith("Total iterations:")));
        } catch (FileNotFoundException cause) {
            throw new InfraException("Unable to find the file: " + file.getName(), cause);
        } catch (IOException cause) {
            throw new InfraException("Error while reading the file: " + file.getName(), cause);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException cause) {
                    // TODO Log!
                    cause.printStackTrace();
                }
            }
        }

        if (lastIterationLine != null) {
            String iteration = lastIterationLine.split(RESULT_TOKEN)[1].trim();
            lastIteration = Integer.parseInt(iteration);
        }

        return lastIteration;
    }

    private Integer getLastCommonIteration(List<Integer> lastIterations) {
        Collections.sort(lastIterations);
        return lastIterations.get(0);
    }
}
