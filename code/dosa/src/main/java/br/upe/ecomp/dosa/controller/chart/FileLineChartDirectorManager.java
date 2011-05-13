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

import java.awt.Color;
import java.awt.Panel;
import java.io.File;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import ChartDirector.Chart;
import ChartDirector.ChartViewer;
import ChartDirector.LineLayer;
import ChartDirector.XYChart;
import br.upe.ecomp.dosa.controller.resultsanalyzer.FileLineResultsAnalyzer;

/**
 * Plot a line chart.
 * 
 * @author George Moraes
 */
public class FileLineChartDirectorManager implements IChartManager {

    private FileLineResultsAnalyzer resultsAnalyzer;

    /**
     * Creates a new instance of this class.
     */
    public FileLineChartDirectorManager() {
        resultsAnalyzer = new FileLineResultsAnalyzer();
    }

    /**
     * {@inheritDoc}
     */
    // public Panel plot(List<File> files, String measurement, int step, boolean logarithmicYAxis) {
    // Integer lastIteration = resultsAnalyzer.getLastIteration(files);
    // double[] data = resultsAnalyzer.getDataMeans(files, measurement, lastIteration, step);
    // return createContents(data, logarithmicYAxis, measurement, step);
    // }

    private Panel createContents(double[] values, boolean logarithmicYAxis, String measurement, int step) {
        Panel chartPanel = new Panel();

        chartPanel.setLayout(new java.awt.GridLayout(1, 1));

        JFreeChart chart = createChart("", "Sample", "Fitness", createSampleDataset(values, measurement, step), false);

        ChartPanel jFreeChartPanel = new ChartPanel(chart);
        chartPanel.add(jFreeChartPanel);

        return chartPanel;
    }

    private JFreeChart createChart(String title, String xLabel, String yLabel, CategoryDataset dataset,
            boolean logarithmicYAxis) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart("", // chart title
                xLabel, // domain axis label
                yLabel, // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
                );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        // final StandardLegend legend = (StandardLegend) chart.getLegend();
        // legend.setDisplaySeriesShapes(true);
        // legend.setShapeScaleX(1.5);
        // legend.setShapeScaleY(1.5);
        // legend.setDisplaySeriesLines(true);

        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.lightGray);

        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        return chart;
    }

    private CategoryDataset createSampleDataset(double[] values, String measurement, int step) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < values.length; i++) {
            dataset.addValue(values[i], measurement, "" + i * step);
        }
        return dataset;
    }

    private double[] createSampleDatasetChartDirector(double[] values, int step) {
        double[] dataset = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            dataset[i] = i * step;
        }
        return dataset;
    }

    public Panel plot(List<File> files, String measurement, int step, boolean logarithmicYAxis) {
        Integer lastIteration = resultsAnalyzer.getLastIteration(files);
        double[] data = resultsAnalyzer.getDataMeans(files, measurement, lastIteration, step);
        return createContentsChartDirector(data, logarithmicYAxis, measurement, step);
    }

    private Panel createContentsChartDirector(double[] values, boolean logarithmicYAxis, String measurement, int step) {
        Panel chartPanel = new Panel();

        chartPanel.setLayout(new java.awt.GridLayout(1, 1));

        chartPanel.setBackground(Color.white);

        // Create the chart and put them in the content pane
        ChartViewer viewer = new ChartViewer();
        this.createChart(viewer, chartPanel, values, step);
        chartPanel.add(viewer);

        // JFreeChart chart = createChart("", "Sample", "Fitness", createSampleDataset(values,
        // measurement, step), false);
        //
        // ChartPanel jFreeChartPanel = new ChartPanel(chart);
        // chartPanel.add(jFreeChartPanel);

        return chartPanel;
    }

    // Main code for creating charts
    public void createChart(ChartViewer viewer, Panel chartPanel, double[] data, int step) {
        // The data for the line chart
        // double[] data0 = { 42, 49, 33, 38, 51, 46, 29, 41, 44, 57, 59, 52, 37, 34, 51, 56, 56,
        // 60, 70, 76, 63, 67, 75,
        // 64, 51 };
        double[] data0 = data;

        // The labels for the line chart
        double[] labels = createSampleDatasetChartDirector(data, step);

        // Create an XYChart object of size 600 x 300 pixels, with a light blue
        // (EEEEFF) background, black border, 1 pixel 3D border effect and rounded
        // corners
        int width = 600; // chartPanel.getWidth();
        int height = 300;// chartPanel.getHeight();

        XYChart c = new XYChart(width, height, 0xeeeeff, 0x000000, 1);
        // c.setRoundedFrame();

        // Set the plotarea at (55, 58) and of size 520 x 195 pixels, with white
        // background. Turn on both horizontal and vertical grid lines with light
        // grey color (0xcccccc)
        c.setPlotArea(55, 58, width - 100, height - 100, 0xffffff, -1, -1, 0xcccccc, 0xcccccc);

        // Add a legend box at (50, 30) (top of the chart) with horizontal layout.
        // Use 9 pts Arial Bold font. Set the background and border color to
        // Transparent.
        c.addLegend(50, 30, false, "Arial Bold", 9).setBackground(Chart.Transparent);

        // Add a title box to the chart using 15 pts Times Bold Italic font, on a
        // light blue (CCCCFF) background with glass effect. white (0xffffff) on a
        // dark red (0x800000) background, with a 1 pixel 3D border.
        c.addTitle("Application Server Throughput", "Times New Roman Bold Italic", 15).setBackground(0xccccff,
                0x000000, Chart.glassEffect());

        // Add a title to the y axis
        c.yAxis().setTitle("Fitness");

        // Set the labels on the x axis.
        c.xAxis().setLabels(labels);

        // Display 1 out of 3 labels on the x-axis.
        c.xAxis().setLabelStep(1);

        // Add a title to the x axis
        c.xAxis().setTitle("Sample");

        // Add a line layer to the chart
        LineLayer layer = c.addLineLayer2();

        // Set the default line width to 2 pixels
        layer.setLineWidth(2);

        // Add the three data sets to the line layer. For demo purpose, we use a dash
        // line color for the last line
        layer.addDataSet(data0, 0xff0000, "Server #1");
        // layer.addDataSet(data2, c.dashLineColor(0x3333ff, Chart.DashLine), "Server #3");

        int sampleData = labels.length / 10;
        double[] scatterPointX = new double[11];
        double[] scatterPointY = new double[11];
        int index = 0;
        for (int i = 0; i < labels.length; i += sampleData) {
            scatterPointX[index] = labels[i];
            scatterPointY[index] = data0[i];
            index++;
        }

        c.addScatterLayer(scatterPointX, scatterPointY, "", Chart.Cross2Shape(0.2), 7, 0x000000);

        // Output the chart
        // viewer.setImage(c.makeImage());
        viewer.setChart(c);

        // include tool tip for the chart
        viewer.setImageMap(c.getHTMLImageMap("clickable", "", "title='[{dataSetName}] Hour {xLabel}: {value} MBytes'"));
    }

}
