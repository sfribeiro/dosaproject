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

/**
 * Enumerates the types supported by the Update Algorithm Dialog.
 * 
 * @author Rodrigo Castro
 */
public enum UpdateAlgorithmDialogEnum {

    ALGORITHM("Algorithm"),

    PROBLEM("Problem"),

    STOP_CONDITIONS("Stop Conditions"),

    MEASUREMENTS("Measurements");

    private String description;

    private UpdateAlgorithmDialogEnum(String description) {
        this.description = description;
    }

    /**
     * Returns the description of the selected update algorithm dialog type.
     * 
     * @return The description of the selected update algorithm dialog type.
     */
    public String getDescription() {
        return description;
    }
}
