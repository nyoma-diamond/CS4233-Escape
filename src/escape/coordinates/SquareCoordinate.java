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

package escape.coordinates;

import escape.required.Coordinate;

public class SquareCoordinate extends AlphaCoordinate {

	public SquareCoordinate(int x, int y) {
		super(x, y);
	}

	@Override
	public int DistanceTo(Coordinate c) { 
		AlphaCoordinate coord = (AlphaCoordinate) c;
		return Math.max((coord.getX() - this.getX()), (coord.getY() - this.getY()));
	}
	
}
