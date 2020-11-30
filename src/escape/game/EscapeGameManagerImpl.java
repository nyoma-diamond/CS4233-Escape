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

import java.util.HashMap;

import escape.EscapeGameManager;
import escape.required.*;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.util.PieceTypeDescriptor;
import escape.required.Player;
import escape.required.EscapePiece.MovementPattern;
import escape.required.EscapePiece.PieceAttributeID;
import escape.required.EscapePiece.PieceName;

public class EscapeGameManagerImpl implements EscapeGameManager<EscapeCoordinate> {
	private EscapeSettings settings;

	private Player curPlayer;
	
	private HashMap<EscapeCoordinate, EscapeLocation> positions; //This could just use Coordinates, but the change isn't necessary
	
	/**
	 * EscapeGameManagerImpl constructor
	 * @param initializer the game initializer to use
	 */
	public EscapeGameManagerImpl(EscapeGameInitializer initializer) {
		this.settings = new EscapeSettings();
		this.settings.coordinateType = initializer.getCoordinateType();
		this.settings.xMax = initializer.getxMax();
		this.settings.yMax = initializer.getyMax();
		this.settings.rules = initializer.getRules();

		this.curPlayer = Player.PLAYER1;

		this.positions = new HashMap<EscapeCoordinate, EscapeLocation>();

		HashMap<PieceName, PieceTypeDescriptor> pieceTypes = new HashMap<PieceName, PieceTypeDescriptor>(); //this is used to make piece descriptors easily accessible during construction
		for (PieceTypeDescriptor pieceType : initializer.getPieceTypes()) 
			pieceTypes.put(pieceType.getPieceName(), pieceType);

		//This assumes that the initializer filtered out empty and out of bounds spaces. If an initializer is provided with an empty CLEAR LocationInitializer, bugs may occur(?)
		for (LocationInitializer loc : initializer.getLocationInitializers()) 
			positions.put(
				makeCoordinate(loc.x, loc.y), 
				loc.player == null 
					? LocationFactory.getLocation(loc) 
					: LocationFactory.getLocation(new EscapePieceImpl(loc.player, pieceTypes.get(loc.pieceName)))
			);
	}

	
	/**
	 * Checks if the provided coordinate is out of bounds for this board
	 * @param coord coordinate to check
	 * @return true if out of bounds, false if in bounds
	 */
	private boolean outOfBounds(EscapeCoordinate coord) {
		return coord.getX() > settings.xMax 
			|| coord.getY() > settings.yMax 
			|| coord.getX() < 1 
			|| coord.getY() < 1; //TODO: this will need to change, but is okay for Alpha because square boards are finite
	}

	@Override
	public boolean move(EscapeCoordinate from, EscapeCoordinate to) {
		EscapeLocation fromLoc, toLoc;

		if (from == null //TODO: are there any ways to short-circuit this for obviously valid moves?
			|| to == null 
			|| from.equals(to) //target and source are same tile
			|| outOfBounds(to) //target out of bounds
			|| (fromLoc = positions.get(from)) == null //source has no location (empty)
			|| fromLoc.getPiece() == null  //no piece, implies BLOCK or EXIT
			|| fromLoc.getPiece().getPlayer() != curPlayer //wrong player's piece
			|| ((toLoc = positions.get(to)) != null && toLoc.locationType == LocationType.BLOCK) //target isn't a block
			|| (toLoc != null && toLoc.getPiece() != null && toLoc.getPiece().getPlayer() == curPlayer) //target isn't source and has current player's piece TODO: this can be simplified after alpha because moving to your own space won't be valid
			|| fromLoc.getPiece().getAttribute(PieceAttributeID.DISTANCE).getValue() < from.DistanceTo(to)
			) return false;


		if (toLoc == null) positions.put(to, toLoc = LocationFactory.getLocation(fromLoc.getPiece())); //if null target location (empty), initialize new location with sourcepiece
		else if (toLoc.locationType != LocationType.EXIT) toLoc.setPiece(fromLoc.getPiece()); //not exit (must be enemy or empty, already checked for blocks), so set piece
		positions.remove(from); //no reason to keep the coordinate after moving a piece off it (must be a clear location). Free up some memory (This line can actually be removed by replacing fromLoc in previous two lines with positions.remove(from), but that hurts readability)
		
		curPlayer = curPlayer == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1; //Would make this its own method but its only one line and not used anywhere else
		return true;
	}

	@Override
	public EscapePiece getPieceAt(EscapeCoordinate coordinate) {
		if (positions.get(coordinate) == null) return null; //this should catch any bad coordinate.
		return positions.get(coordinate).getPiece();
	}

	@Override
	public EscapeCoordinate makeCoordinate(int x, int y) {
		return CoordinateFactory.getCoordinate(settings.coordinateType, x, y);	
	}
}
