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

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.algorithm.Particle;
import br.upe.ecomp.doss.recorder.FileRecorder;
import br.upe.ecomp.doss.recorder.IRecorder;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public class ChartRecorder implements IRecorder {

    private IRecorder recorder;
    private double[] xAxis;
    private double[] yAxis;
    private int swarmSize;

    /**
     * Creates a new instance of this class.
     */
    public ChartRecorder() {
        recorder = new FileRecorder();
    }

    /**
     * {@inheritDoc}
     */
    public void init(Algorithm algorithm) {
        swarmSize = algorithm.getParticles().length;
        xAxis = new double[swarmSize];
        yAxis = new double[swarmSize];

        recorder.init(algorithm);
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        double[] particleCurrentPosition;
        Particle[] particles = algorithm.getParticles();

        for (int i = 0; i < swarmSize; i++) {
            particleCurrentPosition = particles[i].getCurrentPosition();
            xAxis[i] = particleCurrentPosition[0];
            yAxis[i] = particleCurrentPosition[1];
        }
        recorder.update(algorithm);
    }

    /**
     * {@inheritDoc}
     */
    public void finalise(Algorithm algorithm) {
        recorder.finalise(algorithm);
    }

    public double[] getXAxis() {
        return xAxis;
    }

    public double[] getYAxis() {
        return yAxis;
    }
}
