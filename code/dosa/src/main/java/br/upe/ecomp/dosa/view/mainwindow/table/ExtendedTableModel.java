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
package br.upe.ecomp.dosa.view.mainwindow.table;

import javax.swing.table.AbstractTableModel;

import br.upe.ecomp.doss.core.entity.EntityPropertyManager;

/**
 * Extended table model to handle the configuration of {@link Entity} objects.
 * 
 * @author Rodrigo Castro
 */
public class ExtendedTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private Object entity;
    private String[] columnNames;
    private Object[][] data;

    /**
     * Default constructor.
     * 
     * @param entity The object that will be configured.
     * @param columnNames The name of the columns.
     * @param data The data to be used to fill the table.
     */
    public ExtendedTableModel(Object entity, String[] columnNames, Object[][] data) {
        this.entity = entity;
        this.columnNames = columnNames;
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Returns the name of the columns of the table.
     * 
     * @return The name of the columns of the table.
     */
    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col > 0;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        String newValue = (String) value;
        data[row][col] = newValue;

        String parameterName = (String) data[row][0];

        EntityPropertyManager.setValue(entity, parameterName, String.valueOf(value));

        fireTableCellUpdated(row, col);
    }
}
