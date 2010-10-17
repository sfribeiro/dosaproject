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
package br.upe.ecomp.dosa.view.wizard;

import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import br.upe.ecomp.dosa.ApplicationContext;
import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.parser.AlgorithmXMLParser;
import br.upe.ecomp.doss.measurement.Measurement;
import br.upe.ecomp.doss.problem.Problem;
import br.upe.ecomp.doss.stopCondition.StopCondition;

/**
 * Wizard to help user to configure an {@link Algorithm}.
 * 
 * @author Rodrigo Castro
 */
public class WizardAction extends Wizard {

    private static final long serialVersionUID = 1L;

    private static final int LAST_PAGE = 4;
    private int currentPage;
    private ApplicationContext applicationContext;
    private WizardListener listener;

    /**
     * Default constructor.
     * 
     * @param parent the Frame from which the dialog is displayed.
     */
    public WizardAction(final Frame parent) {
        super(parent, true);
        applicationContext = ApplicationContext.getInstance();

        initAlgorithms();
        initProblems();
        initStopConditions();
        initMeasurements();
        initFileSettings();

        currentPage = 0;
        initNavigationButtons();
    }

    /**
     * Registers a listener in this Wizard.
     * 
     * @param listener The listener.
     */
    public void setWizardListener(WizardListener listener) {
        this.listener = listener;
    }

    private void initNavigationButtons() {
        backButton.setEnabled(false);
        nextButton.setEnabled(false);
        finishButton.setEnabled(false);
        cancelButton.setEnabled(true);
    }

    private void initFileSettings() {
        fileNameTextField.setText("");
        fileLocationTextField.setText("");

        fileNameTextField.addKeyListener(new TextFieldKeyListener());
        fileLocationTextField.addKeyListener(new TextFieldKeyListener());
    }

    /**
     * Listener for the Name and Path TextFields.
     */
    private final class TextFieldKeyListener implements KeyListener {
        private boolean isFinishEnabled;

        public void keyTyped(final KeyEvent key) {
            isFinishEnabled = !StringUtils.isBlank(fileNameTextField.getText())
                    && !StringUtils.isBlank(fileLocationTextField.getText());
            finishButton.setEnabled(isFinishEnabled);
        }

        public void keyReleased(final KeyEvent key) {
            // Do nothing here.
        }

        public void keyPressed(final KeyEvent key) {
            // Do nothing here.
        }
    }

    private void initAlgorithms() {
        addAlgorithms();

        algorithmDescriptionTextArea.setText("");
        algorithmComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                algorithmComboBoxActionPerformed();
            }
        });
    }

    private void addAlgorithms() {
        algorithmComboBox.setModel(new DefaultComboBoxModel());
        algorithmComboBox.addItem("");
        for (Class<? extends Algorithm> algorithm : applicationContext.getAlgorithmList()) {
            try {
                algorithmComboBox.addItem(algorithm.getConstructors()[0].newInstance());
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void initProblems() {
        addProblems();

        problemDescriptionTextArea.setText("");
        problemComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                problemComboBoxActionPerformed();
            }
        });
    }

    private void addProblems() {
        problemComboBox.setModel(new DefaultComboBoxModel());
        problemComboBox.addItem("");
        for (Class<? extends Problem> problem : applicationContext.getProblemList()) {
            try {
                problemComboBox.addItem(problem.getConstructors()[0].newInstance());
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /* Stop condition Panel */

    private void initStopConditions() {
        addStopConditions();
        stopConditionSelectedList.setModel(new DefaultListModel());
        stopConditionDescriptionTextArea.setText("");
        updateStopConditionButtonsStatus();

        stopConditionAvailableList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent evt) {
                stopConditionAvailableListSelectionListner();
            }
        });
        stopConditionSelectedList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent evt) {
                stopConditionSelectedListSelectionListner();
            }
        });
    }

    private void addStopConditions() {
        stopConditionAvailableList.setModel(new DefaultListModel());
        for (Class<? extends StopCondition> stopCondition : applicationContext.getStopConditionList()) {
            try {
                ((DefaultListModel) stopConditionAvailableList.getModel())
                        .addElement(stopCondition.getConstructors()[0].newInstance());
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void stopConditionAddButtonActionPerformed(final ActionEvent evt) {
        if (stopConditionAvailableList.getSelectedValue() != null) {
            pickListAddAction(stopConditionAvailableList, stopConditionSelectedList);
            stopConditionDescriptionTextArea.setText("");
            updateStopConditionButtonsStatus();
        }
    }

    @Override
    protected void stopConditionRemoveButtonActionPerformed(final ActionEvent evt) {
        if (stopConditionSelectedList.getSelectedValue() != null) {
            pickListRemoveAction(stopConditionSelectedList, stopConditionAvailableList);
            stopConditionDescriptionTextArea.setText("");
            updateStopConditionButtonsStatus();
        }
    }

    @Override
    protected void stopConditionAddAllButtonActionPerformed(final ActionEvent evt) {
        pickListAddAllAction(stopConditionAvailableList, stopConditionSelectedList);
        stopConditionDescriptionTextArea.setText("");
        updateStopConditionButtonsStatus();
    }

    @Override
    protected void stopConditionRemoveAllButtonActionPerformed(final ActionEvent evt) {
        pickListRemoveAllAction(stopConditionAvailableList, stopConditionSelectedList);
        stopConditionDescriptionTextArea.setText("");
        updateStopConditionButtonsStatus();
    }

    @Override
    protected void stopConditionUpButtonActionPerformed(final ActionEvent evt) {
        pickListUpAction(stopConditionSelectedList);
    }

    @Override
    protected void stopConditionDownButtonActionPerformed(final ActionEvent evt) {
        pickListDownAction(stopConditionSelectedList);
    }

    private void updateStopConditionButtonsStatus() {
        final boolean isAvailableList = stopConditionAvailableList.getModel().getSize() > 0;
        final boolean isSelectedList = stopConditionSelectedList.getModel().getSize() > 0;

        stopConditionAddButton.setEnabled(isAvailableList);
        stopConditionAddAllButton.setEnabled(isAvailableList);
        stopConditionRemoveButton.setEnabled(isSelectedList);
        stopConditionRemoveAllButton.setEnabled(isSelectedList);
        stopConditionUpButton.setEnabled(isSelectedList);
        stopConditionDownButton.setEnabled(isSelectedList);
        nextButton.setEnabled(isSelectedList);
    }

    /* Measurement Panel */

    private void initMeasurements() {
        addMeasurements();
        measurementSelectedList.setModel(new DefaultListModel());
        measurementDescriptionTextArea.setText("");
        updateMeasurementButtonsStatus();

        measurementAvailableList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent evt) {
                measurementAvailableListSelectionListner();
            }
        });
        measurementSelectedList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent evt) {
                measurementSelectedListSelectionListner();
            }
        });
    }

    private void addMeasurements() {
        measurementAvailableList.setModel(new DefaultListModel());
        for (Class<? extends Measurement> measurement : applicationContext.getMeasurementList()) {
            try {
                ((DefaultListModel) measurementAvailableList.getModel()).addElement(measurement.getConstructors()[0]
                        .newInstance());
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void measurementAddButtonActionPerformed(final ActionEvent evt) {
        if (measurementAvailableList.getSelectedValue() != null) {
            pickListAddAction(measurementAvailableList, measurementSelectedList);
            measurementDescriptionTextArea.setText("");
            updateMeasurementButtonsStatus();
        }
    }

    @Override
    protected void measurementRemoveButtonActionPerformed(final ActionEvent evt) {
        if (measurementSelectedList.getSelectedValue() != null) {
            pickListRemoveAction(measurementSelectedList, measurementAvailableList);
            measurementDescriptionTextArea.setText("");
            updateMeasurementButtonsStatus();
        }
    }

    @Override
    protected void measurementAddAllButtonActionPerformed(final ActionEvent evt) {
        pickListAddAllAction(measurementAvailableList, measurementSelectedList);
        measurementDescriptionTextArea.setText("");
        updateMeasurementButtonsStatus();
    }

    @Override
    protected void measurementRemoveAllButtonActionPerformed(final ActionEvent evt) {
        pickListRemoveAllAction(measurementAvailableList, measurementSelectedList);
        measurementDescriptionTextArea.setText("");
        updateMeasurementButtonsStatus();
    }

    @Override
    protected void measurementUpButtonActionPerformed(final ActionEvent evt) {
        pickListUpAction(measurementSelectedList);
    }

    @Override
    protected void measurementDownButtonActionPerformed(final ActionEvent evt) {
        pickListDownAction(measurementSelectedList);
    }

    private void measurementAvailableListSelectionListner() {
        if (measurementAvailableList.getSelectedValue() != null) {
            measurementDescriptionTextArea.setText(((Measurement) measurementAvailableList.getSelectedValue())
                    .getDescription());
            measurementSelectedList.clearSelection();
        }
    }

    private void measurementSelectedListSelectionListner() {
        if (measurementSelectedList.getSelectedValue() != null) {
            measurementDescriptionTextArea.setText(((Measurement) measurementSelectedList.getSelectedValue())
                    .getDescription());
            measurementAvailableList.clearSelection();
        }
    }

    private void updateMeasurementButtonsStatus() {
        final boolean isAvailableList = measurementAvailableList.getModel().getSize() > 0;
        final boolean isSelectedList = measurementSelectedList.getModel().getSize() > 0;

        measurementAddButton.setEnabled(isAvailableList);
        measurementAddAllButton.setEnabled(isAvailableList);
        measurementRemoveButton.setEnabled(isSelectedList);
        measurementRemoveAllButton.setEnabled(isSelectedList);
        measurementUpButton.setEnabled(isSelectedList);
        measurementDownButton.setEnabled(isSelectedList);
        nextButton.setEnabled(isSelectedList);
    }

    private void pickListAddAction(final JList sourceList, final JList destinationList) {
        if (sourceList.getSelectedValue() != null) {
            final Object element = sourceList.getSelectedValue();
            ((DefaultListModel) sourceList.getModel()).remove(sourceList.getSelectedIndex());
            ((DefaultListModel) destinationList.getModel()).addElement(element);
        }
    }

    private void pickListRemoveAction(final JList sourceList, final JList destinationList) {
        if (sourceList.getSelectedValue() != null) {
            final Object element = sourceList.getSelectedValue();
            ((DefaultListModel) sourceList.getModel()).remove(sourceList.getSelectedIndex());
            ((DefaultListModel) destinationList.getModel()).addElement(element);
        }
    }

    private void pickListAddAllAction(final JList sourceList, final JList destinationList) {
        final DefaultListModel availableListModel = (DefaultListModel) sourceList.getModel();
        final DefaultListModel selectedListModel = (DefaultListModel) destinationList.getModel();
        int size = sourceList.getModel().getSize();
        for (int i = 0; i < size; i++) {
            selectedListModel.addElement(availableListModel.getElementAt(i));
        }
        availableListModel.clear();
    }

    private void pickListRemoveAllAction(final JList availableList, final JList selectedList) {
        final DefaultListModel availableListModel = (DefaultListModel) availableList.getModel();
        final DefaultListModel selectedListModel = (DefaultListModel) selectedList.getModel();
        int size = selectedList.getModel().getSize();
        for (int i = 0; i < size; i++) {
            availableListModel.addElement(selectedListModel.getElementAt(i));
        }
        selectedListModel.clear();
    }

    private void pickListUpAction(final JList list) {
        final DefaultListModel selectedListModel = (DefaultListModel) list.getModel();
        final int selectedIndex = list.getSelectedIndex();
        Object element;
        if (selectedIndex > 0) {
            element = list.getSelectedValue();
            selectedListModel.remove(selectedIndex);
            selectedListModel.add(selectedIndex - 1, element);
        }
    }

    private void pickListDownAction(final JList list) {
        final DefaultListModel selectedListModel = (DefaultListModel) list.getModel();
        final int selectedIndex = list.getSelectedIndex();
        final int listSize = selectedListModel.getSize();
        Object element;
        if (selectedIndex < listSize - 1) {
            element = list.getSelectedValue();
            selectedListModel.remove(selectedIndex);
            selectedListModel.add(selectedIndex + 1, element);
        }
    }

    @Override
    public void nextButtonActionPerformed(final ActionEvent evt) {
        if (currentPage < LAST_PAGE) {
            currentPage += 1;
            ((CardLayout) defaultPanel.getLayout()).next(defaultPanel);
            updateButtonsStatus(false);
        }
    }

    @Override
    public void backButtonActionPerformed(final ActionEvent evt) {
        if (currentPage > 0) {
            currentPage -= 1;
            ((CardLayout) defaultPanel.getLayout()).previous(defaultPanel);
            updateButtonsStatus(true);
        }
    }

    @Override
    public void cancelButtonActionPerformed(final ActionEvent evt) {
        closeDialog();
    }

    private void closeDialog() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void updateButtonsStatus(final boolean isNextButtonEnabled) {
        backButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(isNextButtonEnabled);
    }

    private void algorithmComboBoxActionPerformed() {
        if (algorithmComboBox.getSelectedIndex() > 0) {
            algorithmDescriptionTextArea.setText(((Algorithm) algorithmComboBox.getSelectedItem()).getDescription());
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
            algorithmDescriptionTextArea.setText("");
        }
    }

    private void problemComboBoxActionPerformed() {
        if (problemComboBox.getSelectedIndex() > 0) {
            problemDescriptionTextArea.setText(((Problem) problemComboBox.getSelectedItem()).getDescription());
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
            problemDescriptionTextArea.setText("");
        }
    }

    private void stopConditionAvailableListSelectionListner() {
        if (stopConditionAvailableList.getSelectedValue() != null) {
            stopConditionDescriptionTextArea.setText(((StopCondition) stopConditionAvailableList.getSelectedValue())
                    .getDescription());
            stopConditionSelectedList.clearSelection();
        }
    }

    private void stopConditionSelectedListSelectionListner() {
        if (stopConditionSelectedList.getSelectedValue() != null) {
            stopConditionDescriptionTextArea.setText(((StopCondition) stopConditionSelectedList.getSelectedValue())
                    .getDescription());
            stopConditionAvailableList.clearSelection();
        }
    }

    @Override
    protected void fileBrowseButtonActionPerformed(ActionEvent evt) {
        JFileChooser fileopen = new JFileChooser();
        fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int ret = fileopen.showDialog(this, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION) {
            fileLocationTextField.setText(fileopen.getSelectedFile().toString());
        }
    }

    @Override
    protected void finishButtonActionPerformed(ActionEvent evt) {
        String filePath = fileLocationTextField.getText();
        String fileName = fileNameTextField.getText();
        Algorithm algorithm = getAlgorithm();
        AlgorithmXMLParser.save(algorithm, filePath, fileName);
        if (listener != null) {
            listener.onAlgorithmConfigured(algorithm, filePath, fileName);
        }
        closeDialog();
    }

    private Algorithm getAlgorithm() {
        Algorithm algorithm = (Algorithm) algorithmComboBox.getSelectedItem();
        algorithm.setProblem((Problem) problemComboBox.getSelectedItem());

        List<StopCondition> stopConditionList = new ArrayList<StopCondition>();
        DefaultListModel selectedListModel = (DefaultListModel) stopConditionSelectedList.getModel();
        int size = stopConditionSelectedList.getModel().getSize();
        for (int i = 0; i < size; i++) {
            stopConditionList.add((StopCondition) selectedListModel.getElementAt(i));
        }
        algorithm.setStopConditions(stopConditionList);

        List<Measurement> measurementList = new ArrayList<Measurement>();
        selectedListModel = (DefaultListModel) measurementSelectedList.getModel();
        size = measurementSelectedList.getModel().getSize();
        for (int i = 0; i < size; i++) {
            measurementList.add((Measurement) selectedListModel.getElementAt(i));
        }
        algorithm.setMeasurements(measurementList);

        return algorithm;
    }
}
