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

import escape.required.EscapePiece;
import escape.required.Player;

class AlphaPiece implements EscapePiece {
	private Player player;
	private PieceName pieceName;
	
	AlphaPiece(Player player, PieceName pieceName) {
		this.player = player;
		this.pieceName = pieceName;
	}

	public PieceName getName() { return this.pieceName; }

	public Player getPlayer() { return this.player; }
}