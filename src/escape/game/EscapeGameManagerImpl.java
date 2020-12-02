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
import escape.exception.EscapeException;
import escape.required.*;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.util.PieceTypeDescriptor;
import escape.required.Player;
import escape.required.EscapePiece.PieceAttributeID;
import escape.required.EscapePiece.PieceName;

public class EscapeGameManagerImpl implements EscapeGameManager<EscapeCoordinate> {
	private EscapeSettings settings;

	private Player curPlayer;
	
	private HashMap<EscapeCoordinate, EscapeLocation> positions; //This could just use Coordinates, but the change isn't necessary
	private HashMap<PieceName, PieceTypeDescriptor> pieceDescriptors; //stores information about pieces

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

		// This assumes that the initializer filtered out empty and out of bounds
		// spaces. If an initializer is provided with an empty CLEAR
		// LocationInitializer, bugs may occur(?)
		for(LocationInitializer loc : initializer.getLocationInitializers())
			positions.put(
				makeCoordinate(loc.x, loc.y), 
				loc.player == null 
					? LocationFactory.getLocation(loc)
					: LocationFactory.getLocation(new EscapePieceImpl(loc.player, loc.pieceName))
			);

		this.pieceDescriptors = new HashMap<PieceName, PieceTypeDescriptor>(); 
		for(PieceTypeDescriptor pieceDescriptor : initializer.getPieceTypes())
			pieceDescriptors.put(pieceDescriptor.getPieceName(), pieceDescriptor);
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


	/**
	 * Check if moving a piece from source to target is valid
	 * @param source starting coordinate
	 * @param target ending coordinate
	 * @return validity of move
	 */
	private boolean validMove(EscapeCoordinate source, EscapeCoordinate target) { //this code is awful and I hate it
		EscapeLocation sourceLoc, targetLoc;
		
		if (source == null //TODO: are there any ways to short-circuit this for obviously valid moves?
			|| target == null 
			|| outOfBounds(target) //target out of bounds
			|| (sourceLoc = positions.get(source)) == null //source has no location (empty)
			|| sourceLoc.getPiece() == null  //no piece, implies BLOCK or EXIT
			|| sourceLoc.getPiece().getPlayer() != curPlayer //wrong player's piece
			|| ((targetLoc = positions.get(target)) != null && targetLoc.getPiece() != null && targetLoc.getPiece().getPlayer() == curPlayer) //target has current player's piece
			|| (targetLoc != null && targetLoc.locationType == LocationType.BLOCK) //target a BLOCK
		) return false;

		int maxDistance = pieceDescriptors.get(sourceLoc.getPiece().getName()).getAttribute(PieceAttributeID.FLY).getValue(); //TODO: make this work for DISTANCE instead of just FLY (will require pathfinding)
		switch (pieceDescriptors.get(sourceLoc.getPiece().getName()).getMovementPattern()) { //TODO: split this into another method?
			case OMNI:
				return source.DistanceTo(target) <= maxDistance;
			case LINEAR:
				return source.DistanceTo(target) <= maxDistance
					&& (Math.abs(target.getX() - source.getX()) == Math.abs(target.getY() - source.getY())
						|| target.getX() - source.getX() == 0
						|| target.getY() - source.getY() == 0);
			case ORTHOGONAL:
				return Math.abs(target.getX() - source.getX()) + Math.abs(target.getY() - source.getY()) <= maxDistance;
			case DIAGONAL:
				return (target.getX() + target.getY()) % 2 == (source.getX() + source.getY()) % 2
					&& Math.abs(target.getX() - source.getX()) <= maxDistance
					&& Math.abs(target.getY() - source.getY()) <= maxDistance;
		}
		throw new EscapeException("Something went wrong: No valid movement pattern"); //needed to compile.
	}

	@Override
	public boolean move(EscapeCoordinate from, EscapeCoordinate to) {
		if (!validMove(from, to)) return false;

		EscapeLocation fromLoc = positions.get(from);
		EscapeLocation toLoc = positions.get(to);

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
