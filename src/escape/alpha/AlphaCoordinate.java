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

package escape.alpha;

import escape.required.Coordinate;

abstract class AlphaCoordinate implements Coordinate {
	int x, y; //default is less visible than protected

	AlphaCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	int getX() { return this.x; }

	int getY() { return this.y; }

	public abstract int DistanceTo(Coordinate c);
	
}
