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
import java.util.List;

import escape.EscapeGameManager;
import escape.required.*;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;

public class EscapeGameManagerImpl implements EscapeGameManager<AlphaCoordinate> {
	private GameSettings settings;
	private List<Coordinate> coordinates;
	
	private List<AlphaLocation> locations;
	// Hashmap<AlphaCoordinate,Location>

	public EscapeGameManagerImpl(EscapeGameInitializer initializer) {
		this.settings = new GameSettings();
		this.settings.coordinateType = initializer.getCoordinateType();
		this.settings.xMax = initializer.getxMax();
		this.settings.yMax = initializer.getyMax();
		this.settings.rules = initializer.getRules();

		this.coordinates = new ArrayList<Coordinate>();

		this.locations = new ArrayList<AlphaLocation>();
		for (LocationInitializer loc : initializer.getLocationInitializers()) locations.add(new AlphaLocation(loc));

		//TODO: initialize locations (depends on makeCoordinate)
		//TODO: initialize pieces (depends on makeCoordinate)
	}

	public boolean move(AlphaCoordinate from, AlphaCoordinate to) {
		//TODO: implement this
		return false;
	}

	public EscapePiece getPieceAt(AlphaCoordinate coordinate) {
		return null;
		//TODO: implement this
	}

	public AlphaCoordinate makeCoordinate(int x, int y) {
		AlphaCoordinate coord = new SquareCoordinate(x, y);
		
		if (x > settings.xMax || y > settings.yMax) return null;

		for (Coordinate c : coordinates)
		 	if (coord.DistanceTo(c) == 0) return null;

		coordinates.add(coord);
		return new SquareCoordinate(x, y);
	}
}