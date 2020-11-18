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
import escape.util.LocationInitializer;

class LocationFactory {
	static AlphaLocation getLocation(LocationInitializer initializer) {
		return new AlphaLocation(
			initializer.locationType, 
			initializer.player == null ? null : new AlphaPiece(initializer.player, initializer.pieceName));
	}

	static AlphaLocation getLocation() {
		return new AlphaLocation(LocationType.CLEAR, null);
	}
}
