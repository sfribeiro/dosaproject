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
package br.upe.ecomp.dosa.view.mainwindow;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import br.upe.ecomp.dosa.ApplicationContext;
import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.Configurable;
import br.upe.ecomp.doss.measurement.Measurement;
import br.upe.ecomp.doss.problem.Problem;
import br.upe.ecomp.doss.stopCondition.StopCondition;

/**
 * Add behavior to the {@link UpdateAlgorithmDialog} <code>Dialog</code>.
 * 
 * @author Rodrigo Castro
 */
public class UpdateAlgorithmActions extends UpdateAlgorithmDialog {

    private static final long serialVersionUID = 1L;

    private final UpdateAlgorithmDialogEnum operation;
    private Algorithm algorithm;
    private List<? extends Configurable> selectedElements;
    private final ApplicationContext applicationContext;

    /**
     * Default constructor.
     * 
     * @param parent the Frame from which the dialog is displayed.
     * @param algorithm The {@link Algorithm} we want to update.
     * @param operation The type of the operation that will be performed by this <code>Dialog</code>
     */
    public UpdateAlgorithmActions(final Frame parent, Algorithm algorithm, final UpdateAlgorithmDialogEnum operation) {
        super(parent, true);
        this.operation = operation;
        this.algorithm = algorithm;

        applicationContext = ApplicationContext.getInstance();

        configureTitleLabel();
        configureComboBox();

        updateDescriptionTextArea.setText("");
    }

    private void fillSelectedElements() {
        switch (operation) {
            case ALGORITHM:
                selectedElements = Arrays.asList(algorithm);
                break;
            case PROBLEM:
                selectedElements = Arrays.asList(algorithm.getProblem());
                break;
            case STOP_CONDITIONS:
                selectedElements = algorithm.getStopConditions();
                break;
            case MEASUREMENTS:
                selectedElements = algorithm.getMeasurements();
                break;
            default:
                // Do nothing.
        }
    }

    private void configureTitleLabel() {
        updateLabel.setText(operation.getDescription());
    }

    private void configureComboBox() {
        updateComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                String description = "";
                if (updateComboBox.getSelectedIndex() > 0) {
                    description = ((Configurable) updateComboBox.getSelectedItem()).getDescription();
                }
                updateDescriptionTextArea.setText(description);
            }
        });

        fillSelectedElements();

        updateComboBox.setModel(new DefaultComboBoxModel());
        updateComboBox.addItem("");
        switch (operation) {
            case ALGORITHM:
                addAlgorithmList();
                break;
            case PROBLEM:
                addProblemList();
                break;
            case STOP_CONDITIONS:
                addStopConditionList();
                break;
            case MEASUREMENTS:
                addMeasurementList();
                break;
            default:
                // Do nothing.
        }
    }

    private void addAlgorithmList() {
        Algorithm algorithm;

        for (Class<? extends Algorithm> clazz : applicationContext.getAlgorithmList()) {
            try {
                algorithm = (Algorithm) clazz.getConstructors()[0].newInstance();
                if (!selectedElements.contains(algorithm)) {
                    updateComboBox.addItem(algorithm);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void addProblemList() {
        Problem problem;
        for (Class<? extends Problem> clazz : applicationContext.getProblemList()) {
            try {
                problem = (Problem) clazz.getConstructors()[0].newInstance();
                if (!selectedElements.contains(problem)) {
                    updateComboBox.addItem(problem);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void addStopConditionList() {
        StopCondition stopCondition;
        for (Class<? extends StopCondition> clazz : applicationContext.getStopConditionList()) {
            try {
                stopCondition = (StopCondition) clazz.getConstructors()[0].newInstance();
                if (!selectedElements.contains(stopCondition)) {
                    updateComboBox.addItem(stopCondition);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMeasurementList() {
        Measurement measurement;
        for (Class<? extends Measurement> clazz : applicationContext.getMeasurementList()) {
            try {
                measurement = (Measurement) clazz.getConstructors()[0].newInstance();
                if (!selectedElements.contains(measurement)) {
                    updateComboBox.addItem(measurement);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finishButtonActionPerformed(ActionEvent evt) {
        switch (operation) {
            case ALGORITHM:
                Algorithm newAlgorithm = (Algorithm) updateComboBox.getSelectedItem();
                newAlgorithm.setProblem(algorithm.getProblem());
                newAlgorithm.setStopConditions(algorithm.getStopConditions());
                newAlgorithm.setMeasurements(algorithm.getMeasurements());
                break;
            case PROBLEM:
                algorithm.setProblem((Problem) updateComboBox.getSelectedItem());
                break;
            case STOP_CONDITIONS:
                algorithm.getStopConditions().add((StopCondition) updateComboBox.getSelectedItem());
                break;
            case MEASUREMENTS:
                algorithm.getMeasurements().add((Measurement) updateComboBox.getSelectedItem());
                break;
            default:
                // Do nothing.
        }
        closeDialog();
    }

    @Override
    protected void cancelButtonActionPerformed(ActionEvent evt) {
        closeDialog();
    }

    private void closeDialog() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
