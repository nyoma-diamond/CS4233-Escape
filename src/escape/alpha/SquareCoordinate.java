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

import escape.exception.EscapeException;
import escape.required.Coordinate;

class SquareCoordinate extends AlphaCoordinate {

	SquareCoordinate(int x, int y) {
		super(x, y);
	}

	@Override
	public int DistanceTo(Coordinate c) { 
		if (c.getClass() != this.getClass()) 
			throw new EscapeException("Mismatched coordinate type. Cannot distance between different coordinate types.");
		
		AlphaCoordinate coord = (AlphaCoordinate) c;
		return Math.max(Math.abs(coord.getX() - this.getX()), Math.abs(coord.getY() - this.getY()));
	}
	
}
