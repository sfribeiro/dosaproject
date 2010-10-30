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

import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.runner.Runner;
import br.upe.ecomp.doss.runner.RunnerListener;

/**
 * 
 * @author Rodrigo Castro
 */
public class ConfigureSimulationActions extends ConfigureSimulation implements RunnerListener {

    private static final long serialVersionUID = 1L;

    private String filePath;
    private String fileName;

    /**
     * Default constructor.
     * 
     * @param parent the Frame from which the dialog is displayed.
     * @param algorithm {@link Algorithm} that will be simulated.
     * @param filePath The path of the XML file representing the algorithm.
     * @param fileName The name of the XML file representing the algorithm.
     */
    public ConfigureSimulationActions(Frame parent, Algorithm algorithm, String filePath, String fileName) {
        super(parent, true);

        this.filePath = filePath;
        this.fileName = fileName;

        initNumberSimulationsSpinner(algorithm);
    }

    private void initNumberSimulationsSpinner(Algorithm algorithm) {
        numberSimulationsSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        boolean enabled = false;

        if (algorithm.getProblem().getDimensionsNumber() == 2) {
            enabled = true;
            numberSimulationsSpinner.addChangeListener(new ChangeListener() {
                private int simulations;

                @Override
                public void stateChanged(ChangeEvent e) {
                    simulations = (Integer) numberSimulationsSpinner.getModel().getValue();
                    realtimeSimulationCheckBox.setEnabled(simulations == 1);
                }
            });
        }

        realtimeSimulationCheckBox.setEnabled(enabled);
    }

    @Override
    protected void startButtonActionPerformed(ActionEvent evt) {
        final int simulationsNumber = (Integer) numberSimulationsSpinner.getModel().getValue();
        final boolean showSimulation = realtimeSimulationCheckBox.isSelected();

        Runner runner = new Runner(filePath, fileName, simulationsNumber, showSimulation);
        if (!showSimulation) {
            runner.addLitener(this);
        }
        Thread thread = new Thread(runner);
        thread.start();

        if (!showSimulation) {
            showProgress();
        } else {
            closeDialog();
        }

    }

    private void showProgress() {
        progressBar.setIndeterminate(true);
        ((CardLayout) defaultPanel.getLayout()).next(defaultPanel);
        repaint();
    }

    @Override
    protected void cancelButtonActionPerformed(ActionEvent evt) {
        closeDialog();
    }

    private void closeDialog() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * {@inheritDoc}
     */
    public void onSimulationFinish() {
        closeDialog();
    }
}
