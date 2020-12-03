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
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import escape.EscapeGameManager;
import escape.exception.EscapeException;
import escape.required.*;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.util.PieceAttribute;
import escape.util.PieceTypeDescriptor;
import escape.required.Player;
import escape.required.EscapePiece.MovementPattern;
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
			|| coord.getY() < 1; //TODO: this will need to change for infinite boards
	}


	/**
	 * Get the coordinates neighbouring the provided node (filters out of bounds neighbors)
	 * @param coordinate coordinate to get neighbours around
	 * @param movementPattern movement pattern to designate neighbour limitations
	 * @return list of neighbouring coordinates
	 */
	private List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, MovementPattern movementPattern) { 
		List<EscapeCoordinate> neighbours = new LinkedList<EscapeCoordinate>();
		EscapeCoordinate c;
		if (movementPattern != MovementPattern.DIAGONAL) {
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()+1, coordinate.getY()))) neighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()-1, coordinate.getY()))) neighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX(), coordinate.getY()+1))) neighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX(), coordinate.getY()-1))) neighbours.add(c);
		} 
		if (movementPattern != MovementPattern.ORTHOGONAL) {
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()+1, coordinate.getY()+1))) neighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()+1, coordinate.getY()-1))) neighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()-1, coordinate.getY()+1))) neighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()-1, coordinate.getY()-1))) neighbours.add(c);
		}
		
		return neighbours;
	}

	/**
	 * Get the coordinates that can be jumped to from the provided node (filters out of bounds neighbors)
	 * @param coordinate coordinate to get neighbours around
	 * @param movementPattern movement pattern to designate neighbour limitations
	 * @return list of neighbouring coordinates accessible via jump
	 */
	private List<EscapeCoordinate> getJumpNeighbours(EscapeCoordinate coordinate, MovementPattern movementPattern) {
		List<EscapeCoordinate> jumpNeighbours = new LinkedList<EscapeCoordinate>();
		EscapeCoordinate c;
		if (movementPattern != MovementPattern.DIAGONAL) {
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()+2, coordinate.getY()))) jumpNeighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()-2, coordinate.getY()))) jumpNeighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX(), coordinate.getY()+2))) jumpNeighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX(), coordinate.getY()-2))) jumpNeighbours.add(c);
		} 
		if (movementPattern != MovementPattern.ORTHOGONAL) {
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()+2, coordinate.getY()+2))) jumpNeighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()+2, coordinate.getY()-2))) jumpNeighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()-2, coordinate.getY()+2))) jumpNeighbours.add(c);
			if (!outOfBounds(c = makeCoordinate(coordinate.getX()-2, coordinate.getY()-2))) jumpNeighbours.add(c);
		}
		
		return jumpNeighbours;
	}

	/**
	 * Find a path from the source to the target with OMNI movement pattern (assumes already valid with FLY)
	 * @param source starting coordinate
	 * @param target target coordinate
	 * @param descriptor descriptor for piece (movement patterns, max distance, etc.)
	 * @return if there exists a valid path
	 */
	private boolean omniPath(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor) {
		LinkedList<EscapeCoordinate> curLayer = new LinkedList<EscapeCoordinate>(); //current layer to check
		List<EscapeCoordinate> visited = new LinkedList<EscapeCoordinate>(); //visited nodes
		List<EscapeCoordinate> nextLayer = new LinkedList<EscapeCoordinate>(); //next layer to check. Becomes curLayer
		List<EscapeCoordinate> jumpLayer = new LinkedList<EscapeCoordinate>(); //layer of nodes to jump to. Becomes nextLayer
		
		Predicate<EscapeCoordinate> validNeighbour = coord -> { //Predicate indicating whether the neighbour is a valid space to move to and not already queued
			EscapeLocation loc = positions.get(coord);
			return visited.indexOf(coord) == -1 //haven't already visited this neighbour
				&& curLayer.indexOf(coord) == -1 //isn't already queued in current layer
				&& nextLayer.indexOf(coord) == -1 //isn't already queued in next layer
				&& (loc == null //empty spaces are good
					|| loc.locationType == LocationType.EXIT //exits are good
					|| (loc.getPiece() != null 
						&& loc.getPiece().getPlayer() != positions.get(source).getPiece().getPlayer())); //enemy piece spaces are good
		};

		int maxDistance = descriptor.getAttribute(PieceAttributeID.DISTANCE).getValue(); //set maximum distance
		boolean canJump = descriptor.getAttribute(PieceAttributeID.JUMP) != null; //set whether we can jump

		curLayer.add(source);

		//AAAAAAAH THIS ALGORITHM IS SMARTER THAN I AM
		int distance = 0; //start at source
		EscapeCoordinate curNode;
		while (curLayer.size() > 0 && distance <= maxDistance) { //while valid distance and there are nodes to check
			curNode = curLayer.pop(); //get next node
			//if (curNode.equals(target)) return true; //return true if target

			// DEBUG PRINTING
			System.out.print(distance+": <"+curNode.getX()+","+curNode.getY()+">");
			if (curNode.equals(target)) { 
				System.out.println("TARGET FOUND\n");
				return true;
			}
			
			visited.add(curNode);

			if (positions.get(curNode) == null || curNode == source) { // empty space (cannot be BLOCK or EXIT) or starting node (need this to avoid repeating code). This means we can move forward from here
				//nextLayer.addAll(
				//	getNeighbours(curNode, descriptor.getMovementPattern()).stream()
				//														   .filter(validNeighbour)
				//														   .collect(Collectors.toList()));
				//if (canJump) jumpLayer.addAll(
				//	getJumpNeighbours(curNode, descriptor.getMovementPattern()).stream()
				//															   .filter(validNeighbour)
				//															   .collect(Collectors.toList()));

				// DEBUG PRINTING
				System.out.print(" | ");
				List<EscapeCoordinate> neighbours = getNeighbours(curNode, descriptor.getMovementPattern()).stream()
																										   .filter(validNeighbour)
				  																						   .collect(Collectors.toList());
				nextLayer.addAll(neighbours);
				for (EscapeCoordinate c : neighbours) System.out.print(" <"+c.getX()+","+c.getY()+"> ");
				if (canJump) {
					System.out.print(" | ");
					List<EscapeCoordinate> jumpNeighbours = getJumpNeighbours(curNode, descriptor.getMovementPattern()).stream()
																													   .filter(validNeighbour)
																													   .collect(Collectors.toList());
					jumpLayer.addAll(jumpNeighbours);													
					for (EscapeCoordinate c : jumpNeighbours) System.out.print(" <"+c.getX()+","+c.getY()+"> ");											   
				}
				
			}
			System.out.println();
			
			if (curLayer.size() == 0) {
				if (nextLayer.size() != 0) {
					curLayer.addAll(nextLayer); //move to next layer
					nextLayer.clear();
					distance++; //increment distance from source
					
					if (canJump) {
						nextLayer.addAll(jumpLayer.stream() //move forward jump layer
										          .filter(coord -> curLayer.indexOf(coord) == -1) //make sure we're not moving forward a node we can access earlier
										          .distinct()
												  .collect(Collectors.toList()));
					}
					jumpLayer.clear(); //clear jump layer (already empty if cannot jump)
				} else if (canJump) { // this is a hack for the case where the only possible moves are jumps
					curLayer.addAll(jumpLayer);
					jumpLayer.clear();
					distance += 2;
				}
			}
		} 

		System.out.println("NO PATH\n"); // DEBUG PRINTING
		return false;
	}

	/**
	 * Find a path from the source to the target with LINEAR movement pattern (assumes path is already valid with FLY)
	 * @param source starting coordinate
	 * @param target target coordinate
	 * @param descriptor descriptor for piece (movement patterns, max distance, etc.)
	 * @return if there exists a valid path
	 */
	private boolean linearPath(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor) { 
		int dx = target.getX() - source.getX();
		int dy = target.getY() - source.getY();

		int xStep = dx == 0 ? 0 : (dx) / Math.abs(dx);
		int yStep = dy == 0 ? 0 : (dy) / Math.abs(dy);

		boolean jumping = false;
		EscapeCoordinate curCoord = makeCoordinate(source.getX() + xStep, source.getY() + yStep); //set curCoord to next coord

		while (!curCoord.equals(target)) { //while current coordinate isn't the target
			if (positions.get(curCoord) != null) { //current coordinate is not empty
				if (descriptor.getAttribute(PieceAttributeID.JUMP) == null //no jumping
					|| (descriptor.getAttribute(PieceAttributeID.JUMP) != null && jumping)) //jumping but we just jumped
					return false;
				else jumping = true;
			} else jumping = false;

			curCoord = makeCoordinate(curCoord.getX() + xStep, curCoord.getY() + yStep); //next coordinate
		}

		EscapeLocation targetLoc = positions.get(target);
		return targetLoc == null //target is empty space
			|| (jumping //jumping over something onto target
				&& (targetLoc.locationType == LocationType.EXIT //target is exit
					|| (targetLoc.getPiece() != null //target has piece
						&& targetLoc.getPiece().getPlayer() != positions.get(source).getPiece().getPlayer()))); //target piece is enemy's
	}

	/**
	 * Find a path from the source to the target (assumes source and target are valid)
	 * @param source starting coordinate
	 * @param target target coordinate
	 * @param descriptor descriptor for piece (movement patterns, max distance, etc.)
	 * @return if there exists a valid path
	 */
	private boolean pathExists(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor) {
		switch (descriptor.getMovementPattern()) { //TODO: REFACTOR THESE FOR GODS SAKE
			case ORTHOGONAL:
			case DIAGONAL:
			case OMNI:
				return omniPath(source, target, descriptor);
			case LINEAR:
				return linearPath(source, target, descriptor);
		}
		return false;
	}


	/**
	 * Check if moving a piece from source to target is valid
	 * 
	 * @param source starting coordinate
	 * @param target ending coordinate
	 * @return validity of move
	 */
	private boolean validMove(EscapeCoordinate source, EscapeCoordinate target) { //this code is awful and I hate it
		EscapeLocation sourceLoc, targetLoc;
		
		if (source == null
			|| target == null 
			|| outOfBounds(target) //target out of bounds
			|| (sourceLoc = positions.get(source)) == null //source has no location (empty)
			|| sourceLoc.getPiece() == null  //no piece, implies BLOCK or EXIT
			|| sourceLoc.getPiece().getPlayer() != curPlayer //wrong player's piece
			|| ((targetLoc = positions.get(target)) != null && targetLoc.getPiece() != null && targetLoc.getPiece().getPlayer() == curPlayer) //target has current player's piece
			|| (targetLoc != null && targetLoc.locationType == LocationType.BLOCK) //target a BLOCK
		) return false;


		PieceTypeDescriptor descriptor = pieceDescriptors.get(sourceLoc.getPiece().getName()); 

		int maxDistance = descriptor.getAttribute(PieceAttributeID.FLY) != null
			? descriptor.getAttribute(PieceAttributeID.FLY).getValue()
			: descriptor.getAttribute(PieceAttributeID.DISTANCE).getValue();

		switch (descriptor.getMovementPattern()) { 
			case OMNI:
				if (source.DistanceTo(target) > maxDistance) 
					return false;
				break;
			case LINEAR:
				if (source.DistanceTo(target) > maxDistance
					|| (Math.abs(target.getX() - source.getX()) != Math.abs(target.getY() - source.getY())
						&& target.getX() - source.getX() != 0
						&& target.getY() - source.getY() != 0))
						return false;
				break;
			case ORTHOGONAL:
				if (Math.abs(target.getX() - source.getX()) + Math.abs(target.getY() - source.getY()) > maxDistance) 
					return false;
				break;
			case DIAGONAL:
				if ((target.getX() + target.getY()) % 2 != (source.getX() + source.getY()) % 2
					|| Math.abs(target.getX() - source.getX()) > maxDistance
					|| Math.abs(target.getY() - source.getY()) > maxDistance) 
					return false;
				break;
		}

		if (descriptor.getAttribute(PieceAttributeID.FLY) == null) //if FLY is null then DISTANCE must not be and we need to search
			return pathExists(source, target, descriptor); 

		return true;
		
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
