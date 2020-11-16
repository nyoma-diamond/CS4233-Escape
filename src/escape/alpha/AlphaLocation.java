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

import escape.required.LocationType;

class AlphaLocation {
	int x, y;
	LocationType locationType;
	private AlphaPiece piece;

	AlphaLocation(int x, int y, LocationType locationType, AlphaPiece piece) {
		this.x = x;
		this.y = y;
		this.locationType = locationType;
		this.piece = piece;
	}

	/**
	 * Gets the piece in this location
	 * @return the piece in this location
	 */
	AlphaPiece getPiece() { return this.piece; }

	/**
	 * Sets the piece in this location
	 * @param newPiece piece to put in this location
	 * @return previous piece
	 */
	AlphaPiece setPiece(AlphaPiece newPiece) {
		AlphaPiece p = this.piece;
		this.piece = newPiece;
		return p;
	}
}
