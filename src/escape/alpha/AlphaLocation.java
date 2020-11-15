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
import escape.required.LocationType;
import escape.util.LocationInitializer;

class AlphaLocation {
	private Coordinate coordinate;
	private LocationType locationType;
	private AlphaPiece piece;

	//private Player player;
	//private PieceName pieceName;

	AlphaLocation(LocationInitializer initializer) { 
		this.coordinate = new SquareCoordinate(initializer.x, initializer.y); //works for Alpha
		this.locationType = initializer.locationType;

		this.piece = new AlphaPiece();
		this.piece.player = initializer.player;
		this.piece.pieceName = initializer.pieceName;
	}

	AlphaPiece getPiece() { return this.piece; }
	AlphaPiece setPiece(AlphaPiece piece) { return this.piece = piece; }
	
	Coordinate getCoordinate() { return this.coordinate; }
	Coordinate setCoordinate(Coordinate coordinate) {
		Coordinate c = this.coordinate;
		this.coordinate = coordinate;
		return c;
	}
	
	LocationType getLocationType() { return this.locationType; }
}
