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

import escape.required.LocationType;
import escape.util.LocationInitializer;

class LocationFactory {
	/**
	 * Build a location
	 * This only builds the location and does not pull information about the piece!
	 * @param initializer initializer to make location with
	 * @return new loacation based on initializer
	 */
	static EscapeLocation getLocation(LocationInitializer initializer) {
		return new EscapeLocation(initializer.locationType, null);
	}

	/**
	 * build an empty location with provided piece
	 * @param piece piece to put in location
	 * @return the location
	 */
	static EscapeLocation getLocation(EscapePieceImpl piece) {
		return new EscapeLocation(LocationType.CLEAR, piece);
	}
}
