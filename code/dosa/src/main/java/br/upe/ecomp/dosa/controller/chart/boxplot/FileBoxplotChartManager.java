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
package br.upe.ecomp.dosa.controller.chart.boxplot;

import java.awt.Color;
import java.awt.Panel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import br.upe.ecomp.dosa.controller.chart.IChartManager;

/**
 * Plot a boxplot chart.
 * 
 * @author Rodrigo Castro
 */
public class FileBoxplotChartManager implements IChartManager {

    private FileBoxPlotResultsAnalyser resultsAnalyzer;

    /**
     * Creates a new instance of this class.
     */
    public FileBoxplotChartManager() {
        resultsAnalyzer = new FileBoxPlotResultsAnalyser();
    }

    /**
     * {@inheritDoc}
     */
    public Panel plot(List<File> files, String measurement, int step, boolean logarithmicYAxis) {
        Integer lastIteration = resultsAnalyzer.getLastIteration(files);
        double[][] data = resultsAnalyzer.getData(files, measurement, lastIteration, step);
        return createContents(data, logarithmicYAxis);
    }

    private Panel createContents(double[][] values, boolean logarithmicYAxis) {
        Panel chartPanel = new Panel();

        chartPanel.setLayout(new java.awt.GridLayout(1, 1));

        JFreeChart chart = createChart("", "Iterations", "Fitness", createSampleDataset(values), logarithmicYAxis);

        ChartPanel jFreeChartPanel = new ChartPanel(chart);
        chartPanel.add(jFreeChartPanel);

        return chartPanel;
    }

    private JFreeChart createChart(String title, String xLabel, String yLabel, BoxAndWhiskerCategoryDataset dataset,
            boolean logarithmicYAxis) {

        final CategoryAxis xAxis = new CategoryAxis("Sample");
        final NumberAxis yAxis;
        if (logarithmicYAxis) {
            yAxis = new LogarithmicAxis("Fitness (Log10)");
            ((LogarithmicAxis) yAxis).setExpTickLabelsFlag(true);
        } else {
            yAxis = new NumberAxis("Fitness");
        }
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setAutoRange(true);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(true);
        renderer.setBaseCreateEntities(true);
        // renderer.setArtifactPaint(Color.green);
        renderer.setBaseOutlinePaint(Color.blue);
        renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        final JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(title, xAxis.getLabel(), yAxis.getLabel(),
                dataset, true);
        final CategoryPlot plot = chart.getCategoryPlot();// new CategoryPlot(dataset, xAxis, yAxis,
                                                          // renderer);
        plot.setRenderer(renderer);
        plot.setRangeAxis(yAxis);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }

    private BoxAndWhiskerCategoryDataset createSampleDataset(double[][] values) {

        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        ArrayList<Double> list = null;
        for (int i = 0; i < values.length; i++) {
            list = new ArrayList<Double>();
            for (int j = 0; j < values[i].length; j++) {
                list.add(values[i][j]);
            }
            Collections.sort(list);
            // dataset.add(list, "Series", " Iteration " + i);
            dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(list), "Boxplot Evolution chart", i);
        }
        return dataset;
    }
}
