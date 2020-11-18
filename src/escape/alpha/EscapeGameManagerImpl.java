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
		//TODO: experiment with removing empty locations from positions altogether

		for (LocationInitializer loc : initializer.getLocationInitializers()) 
			positions.put(makeCoordinate(loc.x, loc.y), LocationFactory.getLocation(loc));
		
		//TODO: initialize piece types. Don't need to implement for alpha tho :)
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
			|| coord.getY() < 1; //TODO: this will need to change, but is okay for Alpha because square boards are finite
	}

	
	public boolean move(AlphaCoordinate from, AlphaCoordinate to) {
		AlphaLocation fromLoc, toLoc;

		if (from == null //TODO: are there any ways to short-circuit this for obviously valid moves?
			|| to == null 
			|| outOfBounds(to) //target out of bounds
			|| (fromLoc = positions.get(from)) == null //source has no location (empty)
			|| fromLoc.getPiece() == null  //no piece, implies BLOCK or EXIT
			|| fromLoc.getPiece().getPlayer() != curPlayer //wrong player's piece
			|| ((toLoc = positions.get(to)) != null && toLoc.locationType == LocationType.BLOCK) //target isn't a block
			|| (!from.equals(to) && toLoc != null && toLoc.getPiece() != null && toLoc.getPiece().getPlayer() == curPlayer)) //target isn't source and has current player's piece TODO: this can be simplified after alpha because moving to your own space won't be valid
			return false;


		if (!from.equals(to)) { //do nothing if same space
			positions.remove(from); //no reason to keep the coordinate after moving a piece off it (must be a clear location). Free up some memory
			if (toLoc == null) positions.put(to, toLoc = LocationFactory.getLocation()); //if empty target location, initialize one
			if (toLoc.locationType != LocationType.EXIT) toLoc.setPiece(fromLoc.getPiece()); //not exit (must be enemy or empty), so set piece
		}

		curPlayer = curPlayer == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1; //Would make this its own method but its only one line and not used anywhere else
		return true;
	}


	public EscapePiece getPieceAt(AlphaCoordinate coordinate) {
		if (positions.get(coordinate) == null) return null; //this should catch any bad coordinate.
		return positions.get(coordinate).getPiece();
	}


	public AlphaCoordinate makeCoordinate(int x, int y) {
		return CoordinateFactory.getCoordinate(settings.coordinateType, x, y);	
	}
}
