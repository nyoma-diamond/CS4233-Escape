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

import java.util.Objects;
import java.util.function.ToIntBiFunction;

import escape.exception.EscapeException;
import escape.required.Coordinate;

class EscapeCoordinate implements Coordinate {
	private int x, y;
	CoordinateType coordinateType;
	private ToIntBiFunction<EscapeCoordinate, EscapeCoordinate> distanceToFunc;
	//private TwoAndOneFunction<Integer, Coordinate> distanceToFunc;
	
	/**
	 * Constructor for EscapeCoordinate
	 * @param x x position of coordinate
	 * @param y y position of coordinate
	 * @param coordinateType associated coordinate type (used for same type validation in DistanceTo)
	 * @param distanceToFunc how to calculate DistanceTo
	 */
	EscapeCoordinate(int x, int y, CoordinateType coordinateType, ToIntBiFunction<EscapeCoordinate, EscapeCoordinate> distanceToFunc) {
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

	@Override
	public int DistanceTo(Coordinate c) {
		if (!(c instanceof EscapeCoordinate) || ((EscapeCoordinate)c).coordinateType != coordinateType) 
				throw new EscapeException("Mismatched coordinate type. Cannot get distance between different coordinate types.");
			
		return distanceToFunc.applyAsInt(this, (EscapeCoordinate)c);
	}	

	@Override
	public int hashCode() {
		return Objects.hash(x, y, coordinateType);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof EscapeCoordinate)) return false;
		EscapeCoordinate c = (EscapeCoordinate)o;
		return c.getX() == x && c.getY() == y && c.coordinateType == coordinateType;
	}
}
