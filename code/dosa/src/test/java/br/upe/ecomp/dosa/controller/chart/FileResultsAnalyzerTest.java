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

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import br.upe.ecomp.dosa.controller.resultsanalyzer.FileBoxPlotResultsAnalyser;

/**
 * Test class for FilePreProcessor.
 */
public class FileResultsAnalyzerTest {

    /**
     * Scenario: <br>
     * 1. Since I have a list of lists of strings as follows: 'measure1, measure2, measure3';
     * 'measure2, measure3'; 'measure2, measure3, measure4'; <br>
     * 2. When we ask the system to get a list of commons measures in the lists of strings; <br>
     * 3. The system should return a list with the measures measure2 and measure3; <br>
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSearchCommonMeasures() {

        // Step 1
        List<String> list1 = Arrays.asList("measure1", "measure2", "measure3");
        List<String> list2 = Arrays.asList("measure2", "measure3");
        List<String> list3 = Arrays.asList("measure2", "measure3", "measure4");

        // Step 2
        List<String> returnedList = new FileBoxPlotResultsAnalyser().findCommonsMeasurements(Arrays
                .asList(list1, list2, list3));

        // Step 3
        Assert.assertEquals(2, returnedList.size());
        Assert.assertTrue("Measure2 was expected.", returnedList.contains("measure2"));
        Assert.assertTrue("Measure3 was expected.", returnedList.contains("measure3"));
    }
}
