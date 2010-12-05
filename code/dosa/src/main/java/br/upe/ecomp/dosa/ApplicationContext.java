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
package br.upe.ecomp.dosa;

import java.util.ArrayList;
import java.util.List;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.algorithm.chargedpso.ChargedClanPSO;
import br.upe.ecomp.doss.algorithm.chargedpso.ChargedGlobalBestPSO;
import br.upe.ecomp.doss.algorithm.chargedpso.ChargedLocalBestPSO;
import br.upe.ecomp.doss.algorithm.clanpso.ClanPSO;
import br.upe.ecomp.doss.algorithm.fss.FSS;
import br.upe.ecomp.doss.algorithm.pso.LocalBestPSO;
import br.upe.ecomp.doss.measurement.BestFitness;
import br.upe.ecomp.doss.measurement.MeanFitness;
import br.upe.ecomp.doss.measurement.Measurement;
import br.upe.ecomp.doss.problem.Problem;
import br.upe.ecomp.doss.problem.RandomPeaks;
import br.upe.ecomp.doss.problem.df1.DF1;
import br.upe.ecomp.doss.problem.movingpeaks.MovingPeaks;
import br.upe.ecomp.doss.stopCondition.MaximumIterationsStopCondition;
import br.upe.ecomp.doss.stopCondition.StopCondition;
import br.upe.ecomp.doss.stopCondition.StopCondition1;

/**
 * Configures the application.
 * 
 * @author Rodrigo Castro
 */
public final class ApplicationContext {

    private static ApplicationContext applicationContext;
    private List<Class<? extends Algorithm>> algorithmList;
    private List<Class<? extends Problem>> problemList;
    private List<Class<? extends StopCondition>> stopConditionList;
    private List<Class<? extends Measurement>> measurementList;

    /**
     * Default constructor.
     */
    private ApplicationContext() {
        algorithmList = new ArrayList<Class<? extends Algorithm>>();
        problemList = new ArrayList<Class<? extends Problem>>();
        stopConditionList = new ArrayList<Class<? extends StopCondition>>();
        measurementList = new ArrayList<Class<? extends Measurement>>();

        /* Algorithms */
        addAlgorithm(LocalBestPSO.class);
        addAlgorithm(ChargedGlobalBestPSO.class);
        addAlgorithm(ChargedLocalBestPSO.class);
        addAlgorithm(ClanPSO.class);
        addAlgorithm(FSS.class);
        addAlgorithm(ChargedClanPSO.class);

        /* Problems */
        addProblem(MovingPeaks.class);
        addProblem(DF1.class);
        addProblem(RandomPeaks.class);

        /* Stop Conditions */
        addStopCondition(MaximumIterationsStopCondition.class);
        addStopCondition(StopCondition1.class);

        /* Measurements */
        addMeasurement(BestFitness.class);
        addMeasurement(MeanFitness.class);
    }

    /**
     * Returns the instance of this class.
     * 
     * @return The instance of this class.
     */
    public static ApplicationContext getInstance() {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext();
        }
        return applicationContext;
    }

    /**
     * Adds a new algorithm.
     * 
     * @param algorithm An instance of {@link Algorithm}.
     */
    public void addAlgorithm(Class<? extends Algorithm> algorithm) {
        algorithmList.add(algorithm);
    }

    /**
     * Adds a new problem.
     * 
     * @param problem An instance of {@link Problem}.
     */
    public void addProblem(Class<? extends Problem> problem) {
        problemList.add(problem);
    }

    /**
     * Adds a new stop condition.
     * 
     * @param stopCondition An instance of {@link StopCondition}
     */
    public void addStopCondition(Class<? extends StopCondition> stopCondition) {
        stopConditionList.add(stopCondition);
    }

    /**
     * Adds a new measurement.
     * 
     * @param measurement An instance of {@link Measurement}.
     */
    public void addMeasurement(Class<? extends Measurement> measurement) {
        measurementList.add(measurement);
    }

    /**
     * Returns the list of algorithms registered in the application.
     * 
     * @return The list of algorithms registered in the application.
     */
    public List<Class<? extends Algorithm>> getAlgorithmList() {
        return algorithmList;
    }

    /**
     * Returns the list of problems registered in the application.
     * 
     * @return The list of problems registered in the application.
     */
    public List<Class<? extends Problem>> getProblemList() {
        return problemList;
    }

    /**
     * Returns the list of stop conditions registered in the application.
     * 
     * @return The list of stop conditions registered in the application.
     */
    public List<Class<? extends StopCondition>> getStopConditionList() {
        return stopConditionList;
    }

    /**
     * Returns the list of measurements registered in the application.
     * 
     * @return The list of measurements registered in the application.
     */
    public List<Class<? extends Measurement>> getMeasurementList() {
        return measurementList;
    }
}
