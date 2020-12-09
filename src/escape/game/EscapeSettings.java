/*******************************************************************************
 * This file was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 N'yoma Diamond
 *******************************************************************************/

package escape.game;

import escape.required.Coordinate.CoordinateType;

class EscapeSettings {
	CoordinateType coordinateType;
	int xMax, yMax;
	boolean remove;
	boolean pointConflict;
	Integer scoreLimit;
	Integer turnLimit;

	/**
	 * EscapeSettings constructor
	 */
	EscapeSettings() { 
		this.coordinateType = null;
		this.remove = false;
		this.pointConflict = false;
		this.scoreLimit = null;
		this.turnLimit = null;
	}
}
