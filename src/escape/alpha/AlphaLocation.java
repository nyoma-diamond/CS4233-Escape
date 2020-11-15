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
import escape.required.Player;
import escape.required.EscapePiece.PieceName;
import escape.util.LocationInitializer;

class AlphaLocation {
	private LocationType locationType;
	private Player player;
	private PieceName pieceName;

	AlphaLocation(LocationInitializer initializer) {
		this.locationType = initializer.locationType;
		this.player = initializer.player;
		this.pieceName = initializer.pieceName;
	}
}
