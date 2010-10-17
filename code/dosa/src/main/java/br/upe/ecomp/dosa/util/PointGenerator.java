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
package br.upe.ecomp.dosa.util;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * 
 * @author George Moraes
 */
public final class PointGenerator {

    private PointGenerator() {
    }

    public static double[] generatePoints(double begin, double end, double step) {
        List<Double> numbers = new ArrayList<Double>();
        double current = begin;
        while ((current + step) < end) {
            numbers.add(current);
            current = current + step;
        }
        numbers.add(end);
        double[] points = new double[numbers.size()];
        int pos = 0;
        for (Double num : numbers) {
            points[pos] = num;
            pos++;
        }
        return points;
    }
}
