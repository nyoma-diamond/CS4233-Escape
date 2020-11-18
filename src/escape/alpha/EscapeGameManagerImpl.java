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

import java.util.HashMap;

import escape.EscapeGameManager;
import escape.required.*;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.required.Player;

public class EscapeGameManagerImpl implements EscapeGameManager<AlphaCoordinate> {
	private AlphaSettings settings;

	private Player curPlayer;
	
	private HashMap<AlphaCoordinate, AlphaLocation> positions; //This could just use Coordinates, but the change isn't necessary

	public EscapeGameManagerImpl(EscapeGameInitializer initializer) {
		this.settings = new AlphaSettings();
		this.settings.coordinateType = initializer.getCoordinateType();
		this.settings.xMax = initializer.getxMax();
		this.settings.yMax = initializer.getyMax();
		this.settings.rules = initializer.getRules();

		this.curPlayer = Player.PLAYER1;

		this.positions = new HashMap<AlphaCoordinate, AlphaLocation>();

		for (LocationInitializer loc : initializer.getLocationInitializers()) 
			positions.put(makeCoordinate(loc.x, loc.y), LocationFactory.getLocation(loc));
		
		//TODO: initialize pieces. Don't need to implement for alpha tho :)
	}

	
	/**
	 * Return whether moving a piece between the provided locations is valid
	 * @param from location to move piece from
	 * @param to location to move piece to
	 * @return whether moving a piece between the provided locations is valid
	 */
	private boolean validMove(AlphaLocation from, AlphaLocation to) {
		return !(from == null
				|| to == null
				|| from.getPiece() == null
				|| (to.getPiece() != null && from.getPiece().getPlayer() == to.getPiece().getPlayer())
				|| from.getPiece().getPlayer() != curPlayer
				|| to.locationType == LocationType.BLOCK) 
			|| from == to;
	}


	public boolean move(AlphaCoordinate from, AlphaCoordinate to) {
		if (from == null || to == null) return false;
		//if (from == to) return true; //see lower todo

		AlphaLocation fromLoc = positions.get(from);
		AlphaLocation toLoc = positions.get(to);

		if (!validMove(fromLoc, toLoc)) return false;
	
		if (toLoc.locationType != LocationType.EXIT) toLoc.setPiece(fromLoc.getPiece());
		if (fromLoc != toLoc) fromLoc.setPiece(null); //this is kind of redundant because its already checked for in validMove. Is there a better way to do this?

		//TODO: Will need to not change turns on movement to same spot disabled after Alpha
		curPlayer = curPlayer == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1; //Would make this its own method but its only one line and not used anywhere else
		return true;
	}


	public EscapePiece getPieceAt(AlphaCoordinate coordinate) {
		if (positions.get(coordinate) == null) return null; //this should catch any bad coordinate.
		return positions.get(coordinate).getPiece();
	}


	/**
	 * Checks if the provided coordinate is out of bounds for this board
	 * @param coord coordinate to check
	 * @return true if out of bounds, false if in bounds
	 */
	private boolean outOfBounds(AlphaCoordinate coord) {
		return coord.getX() > settings.xMax 
			|| coord.getY() > settings.yMax 
			|| coord.getX() < 1 
			|| coord.getY() < 1; //this will need to change, but is okay for Alpha because square boards are finite
	}


	public AlphaCoordinate makeCoordinate(int x, int y) { //this code is bad and I don't like it
		AlphaCoordinate coord = CoordinateFactory.getCoordinate(settings.coordinateType, x, y);	
		
		if (!outOfBounds(coord) && !positions.containsKey(coord)) 
			positions.put(coord, LocationFactory.getLocation(coord.getX(), coord.getY()));
		return coord;
	}
}
