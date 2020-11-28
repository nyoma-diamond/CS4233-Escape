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
	LocationType locationType;
	private AlphaPiece piece;

	/**
	 * AlphaLocation constructor
	 * @param locationType locationType of location
	 * @param piece piece to put in location
	 */
	AlphaLocation(LocationType locationType, AlphaPiece piece) {
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
	 */
	void setPiece(AlphaPiece newPiece) { this.piece = newPiece; }
}
