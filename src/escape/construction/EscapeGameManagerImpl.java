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

package escape.construction;

import escape.EscapeGameManager;
import escape.required.*;
import escape.util.EscapeGameInitializer;

public class EscapeGameManagerImpl<C extends Coordinate> implements EscapeGameManager<C> {
	private GameSettings settings;

	public EscapeGameManagerImpl(EscapeGameInitializer initializer) {
		this.settings = new GameSettings();
		this.settings.xMax = initializer.getxMax();
		this.settings.yMax = initializer.getyMax();
		this.settings.rules = initializer.getRules();
		//TODO: initialize locations
		//TODO: initialize pieces
	}

	public boolean move(C from, C to) {
		//TODO: implement this
		return false;
	}

	public EscapePiece getPieceAt(C coordinate) {
		return null;
		//TODO: implement this
	}

	public C makeCoordinate(int x, int y) {
		return null;
		//TODO: implement this
	}
}
