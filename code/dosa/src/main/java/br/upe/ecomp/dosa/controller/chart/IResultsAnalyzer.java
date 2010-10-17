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

import java.io.File;
import java.util.List;

/**
 * Performs some analysis on the results of simulations.
 * 
 * @author Rodrigo Castro
 */
public interface IResultsAnalyzer {

    /**
     * Given the list of result files, returns the list of measurements that appears on all files.
     * 
     * @param files The list of file results.
     * @return The list of measurements that appears on all files.
     */
    List<String> searchCommonsMeasurements(List<File> files);

    /**
     * Given the list of result files, returns the biggest iteration that is common to all
     * simulations.
     * 
     * @param files The list of file results.
     * @return The biggest iteration that is common to all simulations.
     */
    Integer getLastIteration(List<File> files);

    /**
     * Gets the data to plot the chart.
     * 
     * @param files The list of file results.
     * @param measurement The measurement that will be analyzed.
     * @param lastIteration The biggest iteration that will be tacked into account.
     * @param step The interval into which the data will be read.
     * @return The data to plot the chart.
     */
    Double[][] getData(List<File> files, String measurement, Integer lastIteration, int step);
}
