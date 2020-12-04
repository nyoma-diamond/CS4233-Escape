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

import java.util.function.ToIntBiFunction;

import escape.required.Coordinate.CoordinateType;

class CoordinateFactory {
	static ToIntBiFunction<EscapeCoordinate, EscapeCoordinate> sqDistance = (EscapeCoordinate c1, EscapeCoordinate c2) -> {
		return Math.max(Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
	};

	static ToIntBiFunction<EscapeCoordinate, EscapeCoordinate> trDistance = (EscapeCoordinate c1, EscapeCoordinate c2) -> {
		int xDif = Math.abs(c1.getX() - c2.getX());
		int yDif = Math.abs(c1.getY() - c2.getY());
		boolean c1Down = (c1.getX() + c1.getY()) % 2 == 0;
		boolean c2Down = (c2.getX() + c2.getY()) % 2 == 0;
		
		if (yDif == 0 || xDif >= yDif) { //same row or move more or equal rows than columns
			if (c1Down == c2Down) return 2 * xDif; //same orientation
			if (c1Down == c1.getX() - c2.getX() > 0) return (2 * xDif) + 1; //moving in direction of orientation
			return (2 * xDif) - 1; //moving against orientation
		}
		return xDif + yDif; 
	};

	/**
	 * Makes a coordinate based on the provided parameters
	 * @param coordinateType type of coordinate
	 * @param x x value of coordinate
	 * @param y y value of coordinate
	 * @return a new coordinate based on provided parameters
	 */
	static EscapeCoordinate getCoordinate(CoordinateType coordinateType, int x, int y) {
		switch (coordinateType) {
			case SQUARE: 
				return new EscapeCoordinate(x, y, coordinateType, sqDistance);
			case TRIANGLE:
				return new EscapeCoordinate(x, y, coordinateType, trDistance);
			default:
				return null; //No way to test, but it's needed so this compiles
		}
	}
}
