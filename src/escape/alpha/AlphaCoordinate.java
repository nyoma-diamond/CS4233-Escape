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

import java.util.Objects;

import escape.exception.EscapeException;
import escape.required.Coordinate;

class AlphaCoordinate implements Coordinate {
	private int x, y;
	private CoordinateType coordinateType;
	private TwoAndOneFunction<Integer, Coordinate, Integer> distanceToFunc;
	
	/**
	 * Constructor for AlphaCoordinate
	 * @param x x position of coordinate
	 * @param y y position of coordinate
	 * @param coordinateType associated coordinate type (used for same type validation in DistanceTo)
	 * @param distanceToFunc how to calculate DistanceTo
	 */
	AlphaCoordinate(int x, int y, CoordinateType coordinateType, TwoAndOneFunction<Integer, Coordinate, Integer> distanceToFunc) {
		this.x = x;
		this.y = y;
		this.coordinateType = coordinateType;
		this.distanceToFunc = distanceToFunc;
	}

	/**
	 * x getter
	 * @return x of this coordinate
	 */
	int getX() { return this.x; }

	/**
	 * y getter
	 * @return y of this coordinate
	 */
	int getY() { return this.y; }

	/**
	 * coordinate type getter
	 * @return coordinate type of this coordinate
	 */
	CoordinateType getCoordinateType() { return this.coordinateType; }

	@Override
	public int DistanceTo(Coordinate c) {
		if (!(c instanceof AlphaCoordinate) || ((AlphaCoordinate)c).getCoordinateType() != coordinateType) 
				throw new EscapeException("Mismatched coordinate type. Cannot get distance between different coordinate types.");
			
		return distanceToFunc.apply(x, y, c);
	}	

	@Override
	public int hashCode() {
		return Objects.hash(x, y, coordinateType);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AlphaCoordinate)) return false;
		AlphaCoordinate c = (AlphaCoordinate)o;
		return c.getX() == x && c.getY() == y && c.getCoordinateType() == coordinateType;
	}
}
