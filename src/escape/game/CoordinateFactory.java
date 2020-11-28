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
import escape.required.Coordinate.CoordinateType;

@FunctionalInterface
interface TwoAndOneFunction<A,B,R> {
	/**
	 * Takes two inputs of the same type, one of a different type, and returns a third type
	 * @param a1 first A-type input
	 * @param a2 second A-type input
	 * @param b b-type input
	 * @return an R-type output
	 */
	R apply(A a1, A a2, B b);
}

class CoordinateFactory {
	static TwoAndOneFunction<Integer, Coordinate, Integer> sqDistance = (Integer x, Integer y, Coordinate c) -> {
		AlphaCoordinate coord = (AlphaCoordinate) c;
		return Math.max(Math.abs(coord.getX() - x), Math.abs(coord.getY() - y));
	};

	/**
	 * Makes a coordinate based on the provided parameters
	 * @param coordinateType type of coordinate
	 * @param x x value of coordinate
	 * @param y y value of coordinate
	 * @return a new coordinate based on provided parameters
	 */
	static AlphaCoordinate getCoordinate(CoordinateType coordinateType, int x, int y) {
		if(coordinateType == CoordinateType.SQUARE)	return new AlphaCoordinate(x, y, coordinateType, sqDistance);
		return null; //No way to test, but it's needed so this compiles
	}
}
