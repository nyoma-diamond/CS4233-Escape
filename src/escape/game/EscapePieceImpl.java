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

import escape.required.EscapePiece;
import escape.required.Player;

class EscapePieceImpl implements EscapePiece {
	private Player player;
	private PieceName pieceName;
	
	/**
	 * EscapeGamePiece constructor
	 * @param player player that owns piece
	 * @param pieceName name of piece
	 */
	EscapePieceImpl(Player player, PieceName pieceName) {
		this.player = player;
		this.pieceName = pieceName;
	}

	@Override
	public PieceName getName() { return this.pieceName; }

	@Override
	public Player getPlayer() { return this.player; }
}
