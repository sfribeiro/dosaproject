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

import br.upe.ecomp.dosa.controller.chart.ChartRecorder;
import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.Runner;
import br.upe.ecomp.doss.recorder.IRecorder;

/**
 * 
 * @author Rodrigo Castro
 */
public class ConfigureSimulationActions extends ConfigureSimulation {

    private static final long serialVersionUID = 1L;

    private String filePath;
    private String fileName;

    /**
     * Default constructor.
     * 
     * @param parent the Frame from which the dialog is displayed.
     * @param algorithm {@link Algorithm} that will be simulated.
     */
    public ConfigureSimulationActions(Frame parent, String filePath, String fileName) {
        super(parent, true);

        this.filePath = filePath;
        this.fileName = fileName;

        numberSimulationsSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        realtimeSimulationCheckBox.setEnabled(false);
    }

    @Override
    protected void startButtonActionPerformed(ActionEvent evt) {
        change();
        final int simulationsNumber = (Integer) numberSimulationsSpinner.getModel().getValue();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IRecorder recorder = new ChartRecorder();
                Runner runner = new Runner(filePath, fileName, simulationsNumber, recorder);
                // new ChartView().runChart((ChartRecorder) recorder);
                runner.run();
            }
        });
        // closeDialog();
    }

    private void change() {
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
}
