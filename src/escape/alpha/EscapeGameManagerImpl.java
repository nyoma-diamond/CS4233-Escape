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
		
		//TODO: initialize pieces (depends on makeCoordinate)
	}

	public boolean move(AlphaCoordinate from, AlphaCoordinate to) {
		//TODO: implement this
		return false;
	}

	public EscapePiece getPieceAt(AlphaCoordinate coordinate) {
		if (positions.get(coordinate) == null) return null; //this should catch any bad coordinate.
		return positions.get(coordinate).getPiece();
	}

	public AlphaCoordinate makeCoordinate(int x, int y) { //this code is bad and I don't like it
		AlphaCoordinate coord = AlphaCoordinateFactory.getCoordinate(settings.coordinateType, x, y);	
		
		if (x > settings.xMax || y > settings.yMax || x < 1 || y < 1) return null; //this will need to change, but is okay for Alpha

		for (AlphaCoordinate c : positions.keySet()) //TODO: make this into its own function
		 	if (coord.DistanceTo(c) == 0) return null;

		for (int i = 0; i < unassignedLocations.size(); i++) { //TODO: make this into its own function
			AlphaLocation loc = unassignedLocations.get(i);
			if (loc.x == x && loc.y == y) {
				positions.put(coord, loc);
				unassignedLocations.remove(i);
				return coord;
			}
		}

		positions.put(coord, LocationFactory.getLocation(x, y));
		return coord;
	}
}
