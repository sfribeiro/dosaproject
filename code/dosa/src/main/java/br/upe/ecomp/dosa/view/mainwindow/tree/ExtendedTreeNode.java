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
package br.upe.ecomp.dosa.view.mainwindow.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * An extension of the <code>DefaultMutableTreeNode</code> to store the node type.
 * 
 * @author Rodrigo Castro
 */
public class ExtendedTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;

    private final TreeNodeTypeEnum type;

    /**
     * Default constructor.
     * 
     * @param userObject An object to be stored.
     * @param type The node type.
     */
    public ExtendedTreeNode(Object userObject, TreeNodeTypeEnum type) {
        super(userObject);
        this.type = type;
    }

    /**
     * Indicates if this node is of the type Algorithm.
     * 
     * @return <code>true</code> if this node is of the type Algorithm, otherwise returns
     *         <code>false</code>.
     */
    public boolean isAlgorithm() {
        return type.equals(TreeNodeTypeEnum.ALGORITHM);
    }

    /**
     * Indicates if this node is of the type Problem.
     * 
     * @return <code>true</code> if this node is of the type Problem, otherwise returns
     *         <code>false</code>.
     */
    public boolean isProblem() {
        return type.equals(TreeNodeTypeEnum.PROBLEM);
    }

    /**
     * Indicates if this node is of the type Problem Child.
     * 
     * @return <code>true</code> if this node is of the type Problem Child, otherwise returns
     *         <code>false</code>.
     */
    public boolean isProblemChild() {
        return type.equals(TreeNodeTypeEnum.PROBLEM_CHILD);
    }

    /**
     * Indicates if this node is of the type Stop Condition.
     * 
     * @return <code>true</code> if this node is of the type Stop Condition, otherwise returns
     *         <code>false</code>.
     */
    public boolean isStopCondition() {
        return type.equals(TreeNodeTypeEnum.STOP_CONDITION);
    }

    /**
     * Indicates if this node is of the type Stop Condition Child.
     * 
     * @return <code>true</code> if this node is of the type Stop Condition Child, otherwise returns
     *         <code>false</code>.
     */
    public boolean isStopConditionChild() {
        return type.equals(TreeNodeTypeEnum.STOP_CONDITION_CHILD);
    }

    /**
     * Indicates if this node is of the type Measurement.
     * 
     * @return <code>true</code> if this node is of the type Measurement, otherwise returns
     *         <code>false</code>.
     */
    public boolean isMeassurement() {
        return type.equals(TreeNodeTypeEnum.MEASSUREMENT);
    }

    /**
     * Indicates if this node is of the type Measurement Child.
     * 
     * @return <code>true</code> if this node is of the type Measurement Child, otherwise returns
     *         <code>false</code>.
     */
    public boolean isMeassurementChild() {
        return type.equals(TreeNodeTypeEnum.MEASSUREMENT_CHILD);
    }

    /**
     * Returns the type of this node.
     * 
     * @return The type of this node.
     */
    public TreeNodeTypeEnum getType() {
        return type;
    }
}
