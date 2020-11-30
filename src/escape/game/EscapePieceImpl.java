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
import escape.util.PieceAttribute;
import escape.util.PieceTypeDescriptor;

class EscapePieceImpl implements EscapePiece {
	private Player player;
	private PieceTypeDescriptor descriptor;
	
	/**
	 * EscapeGamePiece constructor
	 * @param player player that owns piece
	 * @param descriptor descriptor for piece
	 */
	EscapePieceImpl(Player player, PieceTypeDescriptor descriptor) {
		this.player = player;
		this.descriptor = descriptor;
	}

	@Override
	public PieceName getName() { return this.descriptor.getPieceName(); }

	@Override
	public Player getPlayer() { return this.player; }

	/**
	 * Gets the movement pattern of this piece
	 * @return the movement pattern of this piece
	 */
	public MovementPattern getMovementPattern() { return this.descriptor.getMovementPattern(); }

	public PieceAttribute getAttribute(PieceAttributeID id) { return descriptor.getAttribute(id); }
}
