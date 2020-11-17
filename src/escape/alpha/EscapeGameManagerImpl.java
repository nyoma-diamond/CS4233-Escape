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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import escape.EscapeGameManager;
import escape.required.*;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;

public class EscapeGameManagerImpl implements EscapeGameManager<AlphaCoordinate> {
	private AlphaSettings settings;
	
	private List<AlphaLocation> unassignedLocations;
	private HashMap<AlphaCoordinate, AlphaLocation> positions;

	public EscapeGameManagerImpl(EscapeGameInitializer initializer) {
		this.settings = new AlphaSettings();
		this.settings.coordinateType = initializer.getCoordinateType();
		this.settings.xMax = initializer.getxMax();
		this.settings.yMax = initializer.getyMax();
		this.settings.rules = initializer.getRules();

		this.positions = new HashMap<AlphaCoordinate, AlphaLocation>();

		this.unassignedLocations = new ArrayList<AlphaLocation>();
		for (LocationInitializer loc : initializer.getLocationInitializers()) 
			unassignedLocations.add(LocationFactory.getLocation(loc));
		
		//TODO: initialize pieces. Don't need to implement for alpha tho :)
	}

	public boolean move(AlphaCoordinate from, AlphaCoordinate to) {
		//TODO: implement this
		if (from == null 
			|| to == null) return false;
		
		AlphaLocation fromLoc = positions.get(from);
		AlphaLocation toLoc = positions.get(to);

		if (toLoc.getPiece() != null 
			|| fromLoc.getPiece() == null 
			|| toLoc.locationType == LocationType.BLOCK) return false;
	
		if (toLoc.locationType != LocationType.EXIT) toLoc.setPiece(fromLoc.getPiece());
		fromLoc.setPiece(null);
		return true;
	}

	public EscapePiece getPieceAt(AlphaCoordinate coordinate) {
		if (positions.get(coordinate) == null) return null; //this should catch any bad coordinate.
		return positions.get(coordinate).getPiece();
	}

	/**
	 * Checks if the provided coordinate is a valid coordinate given existing coordinates
	 * @param coord coordinate to check
	 * @return true if valid, false if not
	 */
	private boolean validCoordinate(AlphaCoordinate coord) {
		if (coord.getX() > settings.xMax 
			|| coord.getY() > settings.yMax 
			|| coord.getX() < 1 
			|| coord.getY() < 1) return false; //this will need to change, but is okay for Alpha

		for (AlphaCoordinate c : positions.keySet())
			if (coord.DistanceTo(c) == 0) return false;
			 
		return true;
	}

	/**
	 * Adds the provided coordinate to the game
	 * @param coord coordinate to add
	 */
	private void putCoordinate(AlphaCoordinate coord) {
		for (int i = 0; i < unassignedLocations.size(); i++) { 
			AlphaLocation loc = unassignedLocations.get(i); //For each unassigned location
			if (loc.x == coord.getX() && loc.y == coord.getY()) { //If same position as provided coordinate
				positions.put(coord, loc); //Add coordinate-location pair to positions
				unassignedLocations.remove(i); //remove location from unassigned locations list
				return; //exit (there shouldn't be more than one unassigned location at the same spot)
			}
		}

		//No unassigned location in same spot. Create new clear location and add coordinate-location pair to positions
		positions.put(coord, LocationFactory.getLocation(coord.getX(), coord.getY()));
	}

	public AlphaCoordinate makeCoordinate(int x, int y) { //this code is bad and I don't like it
		AlphaCoordinate coord = AlphaCoordinateFactory.getCoordinate(settings.coordinateType, x, y);	
		
		if (!validCoordinate(coord)) return null;

		putCoordinate(coord);
		return coord;
	}
}
