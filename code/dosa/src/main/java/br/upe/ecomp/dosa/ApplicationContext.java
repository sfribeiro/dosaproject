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
import br.upe.ecomp.doss.algorithm.apso.GlobalBestAPSO;
import br.upe.ecomp.doss.algorithm.apso.LocalBestAPSO;
import br.upe.ecomp.doss.algorithm.chargedpso.ChargedClanPSO;
import br.upe.ecomp.doss.algorithm.chargedpso.ChargedGlobalBestPSO;
import br.upe.ecomp.doss.algorithm.chargedpso.ChargedLocalBestPSO;
import br.upe.ecomp.doss.algorithm.clanpso.ClanPSO;
import br.upe.ecomp.doss.algorithm.fss.FSS;
import br.upe.ecomp.doss.algorithm.pso.LocalBestPSO;
import br.upe.ecomp.doss.algorithm.volitiveapso.GlobalVolitiveAPSO;
import br.upe.ecomp.doss.algorithm.volitiveapso.LocalVolitiveAPSO;
import br.upe.ecomp.doss.algorithm.volitivepso.ClanVolitivePSO;
import br.upe.ecomp.doss.algorithm.volitivepso.GlobalVolitivePSO;
import br.upe.ecomp.doss.algorithm.volitivepso.LocalVolitivePSO;
import br.upe.ecomp.doss.measurement.BestFitness;
import br.upe.ecomp.doss.measurement.MeanFitness;
import br.upe.ecomp.doss.measurement.Measurement;
import br.upe.ecomp.doss.problem.Problem;
import br.upe.ecomp.doss.problem.RandomPeaks;
import br.upe.ecomp.doss.problem.ackley.Ackley;
import br.upe.ecomp.doss.problem.cec.F1;
import br.upe.ecomp.doss.problem.cec.F10;
import br.upe.ecomp.doss.problem.cec.F11;
import br.upe.ecomp.doss.problem.cec.F2;
import br.upe.ecomp.doss.problem.cec.F20;
import br.upe.ecomp.doss.problem.cec.F3;
import br.upe.ecomp.doss.problem.cec.F9;
import br.upe.ecomp.doss.problem.df1.DF1;
import br.upe.ecomp.doss.problem.griewank.Griewank;
import br.upe.ecomp.doss.problem.movingpeaks.MovingPeaks;
import br.upe.ecomp.doss.problem.penalized.PenalizedP16;
import br.upe.ecomp.doss.problem.penalized.PenalizedP8;
import br.upe.ecomp.doss.problem.rastrigin.Rastrigin;
import br.upe.ecomp.doss.problem.rosenbrock.Rosenbrock;
import br.upe.ecomp.doss.problem.schwefel.Schwefel12;
import br.upe.ecomp.doss.problem.schwefel.Schwefel226;
import br.upe.ecomp.doss.stopCondition.MaximumIterationsStopCondition;
import br.upe.ecomp.doss.stopCondition.StopCondition;

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
        addAlgorithm(ClanPSO.class);

        addAlgorithm(ChargedGlobalBestPSO.class);
        addAlgorithm(ChargedLocalBestPSO.class);
        addAlgorithm(ChargedClanPSO.class);

        addAlgorithm(GlobalBestAPSO.class);
        addAlgorithm(LocalBestAPSO.class);

        addAlgorithm(GlobalVolitivePSO.class);
        addAlgorithm(LocalVolitivePSO.class);
        addAlgorithm(ClanVolitivePSO.class);

        addAlgorithm(GlobalVolitiveAPSO.class);
        addAlgorithm(LocalVolitiveAPSO.class);

        addAlgorithm(FSS.class);
        
        //addAlgorithm(ABC.class);

        /* Problems */
        addProblem(MovingPeaks.class);
        addProblem(DF1.class);
        addProblem(RandomPeaks.class);
        
        addProblem(Ackley.class);
        addProblem(Griewank.class);
        addProblem(PenalizedP8.class);
        addProblem(PenalizedP16.class);
        addProblem(Rastrigin.class);
        addProblem(Rosenbrock.class);
        addProblem(Schwefel12.class);
        addProblem(Schwefel226.class);

        addProblem(F1.class);
        addProblem(F2.class);
        addProblem(F3.class);
        //addProblem(F4.class);
        //addProblem(F5.class);
        //addProblem(F6.class);
        //addProblem(F7.class);
        //addProblem(F8.class);
        addProblem(F9.class);
        addProblem(F10.class);
        addProblem(F11.class);
        //addProblem(F12.class);
        //addProblem(F13.class);
        //addProblem(F14.class);
        //addProblem(F15.class);
        //addProblem(F16.class);
        //addProblem(F17.class);
        //addProblem(F18.class);
        //addProblem(F19.class);
        addProblem(F20.class);
        
        /* Stop Conditions */
        addStopCondition(MaximumIterationsStopCondition.class);

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
