/**
 * Copyright (C) 2010
 * Rodrigo Castro
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
package br.upe.ecomp.dosa.view.mainwindow;

import java.awt.CardLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.commons.lang.StringUtils;

import br.upe.ecomp.dosa.controller.chart.FileBoxplotChartManager;
import br.upe.ecomp.dosa.controller.chart.FileLineChartManager;
import br.upe.ecomp.dosa.controller.chart.IChartManager;
import br.upe.ecomp.dosa.controller.resultsanalyzer.CSVParser;
import br.upe.ecomp.dosa.controller.resultsanalyzer.FileResultsAnalyzer;
import br.upe.ecomp.dosa.controller.resultsanalyzer.IResultsAnalyzer;
import br.upe.ecomp.dosa.view.mainwindow.table.ExtendedTableModel;
import br.upe.ecomp.dosa.view.mainwindow.tree.ExtendedTreeNode;
import br.upe.ecomp.dosa.view.mainwindow.tree.TreeNodeTypeEnum;
import br.upe.ecomp.dosa.view.wizard.WizardAction;
import br.upe.ecomp.dosa.view.wizard.WizardListener;
import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.entity.Entity;
import br.upe.ecomp.doss.core.entity.EntityPropertyManager;
import br.upe.ecomp.doss.core.parser.AlgorithmXMLParser;
import br.upe.ecomp.doss.measurement.Measurement;
import br.upe.ecomp.doss.recorder.FileRecorder;
import br.upe.ecomp.doss.stopCondition.StopCondition;

/**
 * Add all the actions to the Main application window.
 * 
 * @author Rodrigo Castro
 */
public class MainWindowActions extends MainWindow implements WizardListener {

    private String filePath;
    private String fileName;
    private Algorithm algorithm;
    private List<File> resultFiles;
    private IResultsAnalyzer resultsAnalyzer;
    private IChartManager chartManager;
    private List<String> measurementsResultList;

    private JPopupMenu popupAlgorithm;
    private JPopupMenu popupProblem;
    private JPopupMenu popupStopCondition;
    private JPopupMenu popupMeassurement;

    /**
     * Default constructor.
     */
    public MainWindowActions() {
        super();
        setTitle("Dynamic Optimization System Analyzer");

        createPopupMenus();
        updateToolBarButtonsState();

        initResultsTab();

        tree.setModel(new DefaultTreeModel(null));
        table.setModel(new DefaultTableModel());
    }

    /**
     * {@inheritDoc}
     */
    public void onAlgorithmConfigured(Algorithm algorithm, String filePath, String fileName) {
        this.algorithm = algorithm;
        this.filePath = filePath;
        this.fileName = fileName;
        configureTree();
        updateToolBarButtonsState();
        ((CardLayout) panelTestScenario.getLayout()).last(panelTestScenario);
    }

    private void updateToolBarButtonsState() {
        boolean enabled = algorithm != null;
        saveToolBarButton.setEnabled(enabled);
        startSimulationToolBarButton.setEnabled(enabled);
    }

    @Override
    protected void openToolBarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        openTestScenario();
    }

    @Override
    protected void newToolBarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        newTestScenario();
    }

    private void newTestScenario() {
        WizardAction wizard = new WizardAction(MainWindowActions.this);
        wizard.setWizardListener(MainWindowActions.this);
        wizard.setVisible(true);
    }

    @Override
    protected void saveToolBarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        AlgorithmXMLParser.save(algorithm, filePath, fileName);
    }

    @Override
    protected void startSimulationToolBarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        algorithm.setRecorder(new FileRecorder());
        ConfigureSimulation configureSimulation = new ConfigureSimulationActions(this, algorithm, filePath, fileName);
        configureSimulation.setVisible(true);
    }

    @Override
    protected void openTestScenarioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        openTestScenario();
    }

    private void openTestScenario() {
        FileDialog fileopen = new FileDialog(this, "Open Test Scenario", FileDialog.LOAD);
        fileopen.setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("xml");
            }
        });

        fileopen.setVisible(true);

        if (fileopen.getFile() != null) {
            filePath = fileopen.getDirectory();
            fileName = fileopen.getFile();

            algorithm = AlgorithmXMLParser.read(filePath, fileName);
            configureTree();
            updateToolBarButtonsState();
            ((CardLayout) panelTestScenario.getLayout()).last(panelTestScenario);
        }
    }

    @Override
    protected void newTestScenarioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        newTestScenario();
    }

    @Override
    protected void resultDirectoryButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (chooseDirectory(resultDirectoryTextField)) {
            updateMeasurementBoxplotComboBox();
        }
    }

    @Override
    protected void resultFileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (chooseDirectory(resultFileTextField)) {
            updateMeasurementLineComboBox();
        }
    }

    private boolean chooseDirectory(JTextField textField) {
        boolean directoryChoosed = false;
        FileDialog fileopen = new FileDialog(this, "Open Results Directory", FileDialog.LOAD);
        fileopen.setModalityType(ModalityType.DOCUMENT_MODAL);

        System.setProperty("apple.awt.fileDialogForDirectories", "true");
        fileopen.setVisible(true);
        System.setProperty("apple.awt.fileDialogForDirectories", "false");

        if (fileopen.getFile() != null) {
            File directory = new File(fileopen.getDirectory(), fileopen.getFile());
            textField.setText(directory.getPath());

            resultFiles = getFilesOnDirectory(directory);
            // List<File> foundFiles = ;
            // for (File file : foundFiles) {
            // if (file.getName().endsWith(".txt")) {
            // resultFiles.add(file);
            // }
            // }
            directoryChoosed = true;
        }
        return directoryChoosed;
    }

    @Override
    protected void createBoxplotChartButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String measurement = (String) measurementResultComboBox.getSelectedItem();
        Integer step = Integer.parseInt(stepResultTextField.getText());
        boolean logarithmicYAxis = logarithmicResultCheckBox.isSelected();

        Panel panel = new FileBoxplotChartManager().plot(resultFiles, measurement, step, logarithmicYAxis);
        resultsSplitPane.setRightComponent(panel);
    }

    @Override
    protected void createLineChartButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String measurement = (String) measurementLineResultComboBox.getSelectedItem();
        Integer step = Integer.parseInt(stepLineResultTextField.getText());

        Panel panel = new FileLineChartManager().plot(resultFiles, measurement, step, false);
        resultsSplitPane.setRightComponent(panel);
    }

    @Override
    protected void exportButtonActionPerformed(ActionEvent evt) {
        String outputDirectory = null;
        String outputFile = null;
        String measurement = (String) measurementLineResultComboBox.getSelectedItem();
        Integer step = Integer.parseInt(stepLineResultTextField.getText());

        FileDialog fileopen = new FileDialog(new Frame(), "Open Results Directory", FileDialog.SAVE);
        fileopen.setModalityType(ModalityType.DOCUMENT_MODAL);
        fileopen.setVisible(true);

        if (fileopen.getFile() != null) {
            outputDirectory = fileopen.getDirectory();
            outputFile = fileopen.getFile();
        }

        if (outputDirectory != null && outputFile != null) {
            new CSVParser().parse(outputDirectory, outputFile, resultFiles, measurement, step);
        }
    }

    private void initResultsTab() {
        chartTypeComboBox.setModel(new DefaultComboBoxModel());
        chartTypeComboBox.addItem("Boxplot");
        chartTypeComboBox.addItem("Line");
        chartTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (chartTypeComboBox.getSelectedIndex() == 0) {
                    ((CardLayout) chartPanel.getLayout()).first(chartPanel);
                } else {
                    ((CardLayout) chartPanel.getLayout()).last(chartPanel);
                }
            }
        });
        ((CardLayout) chartPanel.getLayout()).first(chartPanel);

        resultsAnalyzer = new FileResultsAnalyzer();
        // rtManager = new FileBoxplotChartManager();
        // chartManager = new FileLineChartManager();

        measurementResultComboBox.setEnabled(false);
        // createBoxplotChartButton.setEnabled(false);
        resultsSplitPane.setRightComponent(new Panel());

        // stepResultTextField.addKeyListener(new TextFieldKeyListener());
    }

    private void updateMeasurementBoxplotComboBox() {
        measurementsResultList = resultsAnalyzer.searchCommonsMeasurements(resultFiles);
        measurementResultComboBox.setModel(new DefaultComboBoxModel(measurementsResultList
                .toArray(new String[measurementsResultList.size()])));
        measurementResultComboBox.setEnabled(true);
    }

    private void updateMeasurementLineComboBox() {
        measurementsResultList = resultsAnalyzer.searchCommonsMeasurements(resultFiles);
        measurementLineResultComboBox.setModel(new DefaultComboBoxModel(measurementsResultList
                .toArray(new String[measurementsResultList.size()])));
        measurementLineResultComboBox.setEnabled(true);
    }

    private List<File> getFilesOnDirectory(File directory) {
        List<File> files = new ArrayList<File>();
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".txt")) {
                files.add(file);
            }
        }
        return files;
    }

    private void createPopupMenus() {

        JMenuItem algorithmMenu = new JMenuItem("Change algorithm...");
        algorithmMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                showUpdateAlgorithmDialog(UpdateAlgorithmDialogEnum.ALGORITHM);
            }
        });
        popupAlgorithm = new JPopupMenu();
        popupAlgorithm.add(algorithmMenu);

        JMenuItem problemMenu = new JMenuItem("Change problem...");
        problemMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                showUpdateAlgorithmDialog(UpdateAlgorithmDialogEnum.PROBLEM);
            }
        });
        popupProblem = new JPopupMenu();
        popupProblem.add(problemMenu);
        JMenuItem stopConditionAddMenu = new JMenuItem("Add stop condition...");
        stopConditionAddMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                showUpdateAlgorithmDialog(UpdateAlgorithmDialogEnum.STOP_CONDITIONS);
            }
        });

        JMenuItem stopConditionRemoveMenu = new JMenuItem("Remove stop condition...");
        stopConditionRemoveMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                StopCondition stopCondition = (StopCondition) ((ExtendedTreeNode) tree.getLastSelectedPathComponent())
                        .getUserObject();
                algorithm.getStopConditions().remove(stopCondition);
                createNodes();
            }
        });
        popupStopCondition = new JPopupMenu();
        popupStopCondition.add(stopConditionAddMenu);
        popupStopCondition.add(stopConditionRemoveMenu);

        JMenuItem measurementMenu = new JMenuItem("Add meassurement...");
        measurementMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                showUpdateAlgorithmDialog(UpdateAlgorithmDialogEnum.MEASUREMENTS);
            }
        });
        JMenuItem measurementRemoveMenu = new JMenuItem("Remove meassurement...");
        measurementRemoveMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                Measurement measurement = (Measurement) ((ExtendedTreeNode) tree.getLastSelectedPathComponent())
                        .getUserObject();
                algorithm.getMeasurements().remove(measurement);
                createNodes();
            }
        });
        popupMeassurement = new JPopupMenu();
        popupMeassurement.add(measurementMenu);
        popupMeassurement.add(measurementRemoveMenu);

        // Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener();
        tree.addMouseListener(popupListener);
    }

    private void showUpdateAlgorithmDialog(UpdateAlgorithmDialogEnum operation) {
        UpdateAlgorithmActions dialog = new UpdateAlgorithmActions(this, algorithm, operation);
        dialog.setVisible(true);
        createNodes();
    }

    /**
     * Add actions to the pop up menus.
     */
    private class PopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            showPopup(event);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            showPopup(event);
        }

        private void showPopup(MouseEvent event) {
            if (event.isPopupTrigger() && tree.getLastSelectedPathComponent() != null) {
                ExtendedTreeNode node = (ExtendedTreeNode) tree.getLastSelectedPathComponent();
                String label;

                switch (node.getType()) {
                    case ALGORITHM:
                        popupAlgorithm.show(event.getComponent(), event.getX(), event.getY());
                        break;
                    case PROBLEM:
                    case PROBLEM_CHILD:
                        label = algorithm.getProblem() != null ? "Change problem..." : "Add problem...";
                        ((JMenuItem) popupProblem.getComponent(0)).setText(label);
                        popupProblem.show(event.getComponent(), event.getX(), event.getY());
                        break;
                    case STOP_CONDITION:
                        popupStopCondition.getComponent(0).setVisible(true);
                        popupStopCondition.getComponent(1).setVisible(false);
                        popupStopCondition.show(event.getComponent(), event.getX(), event.getY());
                        break;
                    case STOP_CONDITION_CHILD:
                        popupStopCondition.getComponent(0).setVisible(false);
                        popupStopCondition.getComponent(1).setVisible(true);
                        popupStopCondition.show(event.getComponent(), event.getX(), event.getY());
                        break;
                    case MEASSUREMENT:
                        popupMeassurement.getComponent(0).setVisible(true);
                        popupMeassurement.getComponent(1).setVisible(false);
                        popupMeassurement.show(event.getComponent(), event.getX(), event.getY());
                        break;
                    case MEASSUREMENT_CHILD:
                        popupMeassurement.getComponent(0).setVisible(false);
                        popupMeassurement.getComponent(1).setVisible(true);
                        popupMeassurement.show(event.getComponent(), event.getX(), event.getY());
                        break;
                    default:
                        // Do nothing.
                }
            }
        }
    }

    private void configureTree() {
        createNodes();
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(final TreeSelectionEvent event) {
                ExtendedTreeNode node = (ExtendedTreeNode) tree.getLastSelectedPathComponent();
                if (node != null
                        && (node.isAlgorithm() || node.isProblemChild() || node.isStopConditionChild() || node
                                .isMeassurementChild())) {
                    configureTable((Entity) node.getUserObject());
                } else {
                    table.setModel(new DefaultTableModel());
                }
            }
        });
    }

    private void createNodes() {
        DefaultMutableTreeNode root = new ExtendedTreeNode(algorithm, TreeNodeTypeEnum.ALGORITHM);
        DefaultMutableTreeNode problemNode = new ExtendedTreeNode("Problem", TreeNodeTypeEnum.PROBLEM);
        DefaultMutableTreeNode stopConditionNode = new ExtendedTreeNode("Stop conditions",
                TreeNodeTypeEnum.STOP_CONDITION);
        DefaultMutableTreeNode meassurementNode = new ExtendedTreeNode("Meassurements", TreeNodeTypeEnum.MEASSUREMENT);

        addProblem(problemNode, algorithm);
        addStopCondition(stopConditionNode, algorithm);
        addMeasurement(meassurementNode, algorithm);

        root.add(problemNode);
        root.add(stopConditionNode);
        root.add(meassurementNode);
        TreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
    }

    private void addProblem(DefaultMutableTreeNode problemNode, Algorithm algorithm) {
        DefaultMutableTreeNode problem = new ExtendedTreeNode(algorithm.getProblem(), TreeNodeTypeEnum.PROBLEM_CHILD);
        problemNode.add(problem);
    }

    private void addStopCondition(DefaultMutableTreeNode stopConditionNode, Algorithm algorithm) {
        DefaultMutableTreeNode stopConditionChild;
        for (StopCondition stopCondition : algorithm.getStopConditions()) {
            stopConditionChild = new ExtendedTreeNode(stopCondition, TreeNodeTypeEnum.STOP_CONDITION_CHILD);
            stopConditionNode.add(stopConditionChild);
        }
    }

    private void addMeasurement(DefaultMutableTreeNode meassurementNode, Algorithm algorithm) {
        DefaultMutableTreeNode measurementChild;
        for (Measurement measurement : algorithm.getMeasurements()) {
            measurementChild = new ExtendedTreeNode(measurement, TreeNodeTypeEnum.MEASSUREMENT_CHILD);
            meassurementNode.add(measurementChild);
        }
    }

    private void configureTable(Object entity) {
        TableModel model = null;
        if (entity == null) {
            model = new DefaultTableModel();
        } else {
            String[] columnNames = new String[] { "Parameter", "Value" };
            Object[][] data = new Object[EntityPropertyManager.getParametersNameFromFields(entity).size()][2];
            prepareTableData(entity, data);

            model = new ExtendedTableModel(entity, columnNames, data);
        }
        table.setModel(model);
    }

    private void prepareTableData(Object entity, Object[][] data) {
        int i = 0;
        for (String parameterName : EntityPropertyManager.getParametersNameFromFields(entity)) {
            data[i][0] = parameterName;
            data[i][1] = EntityPropertyManager.getValue(entity, parameterName);
            i++;
        }
    }

    /**
     * Listener for the Step TextFields.
     */
    private final class TextFieldKeyListener implements KeyListener {
        private boolean isFinishEnabled;

        public void keyTyped(final KeyEvent key) {
            isFinishEnabled = !StringUtils.isBlank(stepResultTextField.getText()) && resultFiles != null
                    && measurementResultComboBox.getSelectedItem() != null;
            createBoxplotChartButton.setEnabled(isFinishEnabled);
        }

        public void keyReleased(final KeyEvent key) {
            // Do nothing here.
        }

        public void keyPressed(final KeyEvent key) {
            // Do nothing here.
        }
    }
}
