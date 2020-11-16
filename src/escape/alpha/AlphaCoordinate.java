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

class AlphaCoordinate implements Coordinate {
	private int x, y; //default is less visible than protected
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

	int getX() { return this.x; }
	int getY() { return this.y; }
	CoordinateType getCoordinateType() { return this.coordinateType; }

	public int DistanceTo(Coordinate c) {
		try { // This is jank as hell, but it works.
			if (((AlphaCoordinate)c).getCoordinateType() != coordinateType) 
				throw new EscapeException("Mismatched coordinate type. Cannot get distance between different coordinate types.");
		} catch (Exception e) {
			throw new EscapeException("Mismatched coordinate type. Cannot get distance between different coordinate types.");
		}
			
		return distanceToFunc.apply(x, y, c);
	}	
}
