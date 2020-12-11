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

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import escape.exception.EscapeException;
import escape.required.Coordinate.CoordinateType;
import escape.required.EscapePiece.MovementPattern;
import escape.util.PieceTypeDescriptor;


abstract class Movement { 
	/**
	 * Get's the neigbours of the provided coordinate
	 * @param coordinate Coordinate to get neighbours of
	 * @param jump whether or not to get jump neighbours
	 * @param manager the manager to use (this is needed so we can call makeCoordinate since makeCoordinate isn't static)
	 * @return list of neighbours
	 */
	abstract List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager);

	/**
	 * Validate if moving from source to target is valid assuming no bounds other than maximum distance
	 * This assumes source and target are already valid on their own
	 * @param source source coordinate
	 * @param target target coordinate
	 * @param maxDistance maximum moveable distance
	 * @return a string with the reason why the move is not valid or null if it is valid
	 */
	abstract String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance);

	/**
	 * Verify if a path exists from the source coordinate to the target coordinate with the provided piece characteristics
	 * @param source source coordinate
	 * @param target target coordinate
	 * @param descriptor descriptor for the characteristics of the piece being moved
	 * @param manager manager to look for a path on
	 * @return if a path exists
	 */
	boolean pathExists(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor, EscapeGameManagerImpl manager) {
		return manager.omniPath(source, target, descriptor);
	}


	private static final Movement ORTHOGONAL = new Movement() {
		List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager) {
			List<EscapeCoordinate> neighbours = new LinkedList<EscapeCoordinate>();
			int d = jump ? 2 : 1;
			int x = coordinate.getX();
			int y = coordinate.getY();
			
			neighbours.add(manager.makeCoordinate(x + d, y));
			neighbours.add(manager.makeCoordinate(x - d, y));
			neighbours.add(manager.makeCoordinate(x, y + d));
			neighbours.add(manager.makeCoordinate(x, y - d)); 
			
			return neighbours;
		}

		String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance) {
			return Math.abs(target.getX() - source.getX()) + Math.abs(target.getY() - source.getY()) > maxDistance 
				? "Invalid move: Target is too far away" 
				: null;
		}
	};


	private static final Movement DIAGONAL = new Movement() {
		List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager) {
			List<EscapeCoordinate> neighbours = new LinkedList<EscapeCoordinate>();
			int d = jump ? 2 : 1;
			int x = coordinate.getX();
			int y = coordinate.getY();

			neighbours.add(manager.makeCoordinate(x + d, y + d));
			neighbours.add(manager.makeCoordinate(x + d, y - d));
			neighbours.add(manager.makeCoordinate(x - d, y + d));
			neighbours.add(manager.makeCoordinate(x - d, y - d));

			return neighbours;
		}

		String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance) {
			return (target.getX() + target.getY()) % 2 != (source.getX() + source.getY()) % 2
				? "Invalid move: DIAGONAL pieces can't move that way" 
				: ((Math.abs(target.getX() - source.getX()) > maxDistance || Math.abs(target.getY() - source.getY()) > maxDistance) 
					? "Invalid move: Target is too far away" 
					: null);
		}
	};


	private static final Movement OMNI = new Movement() {
		List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager) {
			List<EscapeCoordinate> neighbours = new LinkedList<EscapeCoordinate>();

			if (coordinate.coordinateType == CoordinateType.SQUARE) {
				neighbours.addAll(ORTHOGONAL.getNeighbours(coordinate, jump, manager));
				neighbours.addAll(DIAGONAL.getNeighbours(coordinate, jump, manager));
			} else if (coordinate.coordinateType == CoordinateType.TRIANGLE) {
				int d = jump ? 2 : 1;
				int x = coordinate.getX();
				int y = coordinate.getY();
				if (jump) { //we're getting jump neighbours
					neighbours.add(manager.makeCoordinate(x + 1, y + 1));
					neighbours.add(manager.makeCoordinate(x - 1, y + 1));
					neighbours.add(manager.makeCoordinate(x + 1, y - 1));
					neighbours.add(manager.makeCoordinate(x - 1, y - 1));
				} else if ((x + y) % 2 == 0) { //points down
					neighbours.add(manager.makeCoordinate(x + 1, y));
				} else {
					neighbours.add(manager.makeCoordinate(x - 1, y)); //points up
				}
	
				neighbours.add(manager.makeCoordinate(x, y - d));
				neighbours.add(manager.makeCoordinate(x, y + d));
			} else {
				throw new EscapeException("NOT IMPLEMENTED");
			}

			return neighbours;
		}

		String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance) {
			return source.DistanceTo(target) > maxDistance 
				? "Invalid move: Target is too far away"
				: null;
		}
	};


	private static final Movement LINEAR = new Movement() {
		List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager) {
			throw new EscapeException("Linear doesn't have neighbours");
		}

		String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance) {
			return source.DistanceTo(target) > maxDistance 
				? "Invalid move: Target is too far away"
				: ((Math.abs(target.getX() - source.getX()) != Math.abs(target.getY() - source.getY()) 
					&& target.getX() - source.getX() != 0 && target.getY() - source.getY() != 0) 
					? "Invalid move: LINEAR pieces can't move that way"
					: null);
		}

		@Override
		boolean pathExists(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor, EscapeGameManagerImpl manager) {
			return manager.linearPath(source, target, descriptor);
		}
	};



	//Map that allows us to effectively convert from the MovementPattern enum to the movement classes
	static final ImmutableMap<MovementPattern, Movement> movementTypes = ImmutableMap.<MovementPattern, Movement>builder()
		.put(MovementPattern.ORTHOGONAL, ORTHOGONAL)
		.put(MovementPattern.DIAGONAL, DIAGONAL)
		.put(MovementPattern.OMNI, OMNI)
		.put(MovementPattern.LINEAR, LINEAR)
		.build();
}
