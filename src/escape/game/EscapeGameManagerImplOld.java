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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import escape.EscapeGameManager;
import escape.required.*;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.util.PieceAttribute;
import escape.util.PieceTypeDescriptor;
import escape.util.RuleDescriptor;
import escape.required.Player;
import escape.required.Coordinate.CoordinateType;
import escape.required.EscapePiece.MovementPattern;
import escape.required.EscapePiece.PieceAttributeID;
import escape.required.EscapePiece.PieceName;

public class EscapeGameManagerImplOld implements EscapeGameManager<EscapeCoordinate> {
	private EscapeSettings settings;

	private Player curPlayer;
	private int curTurn;
	private int[] scores;
	private int[] piecesRemaining;
	
	//TODO: make Map?
	private HashMap<EscapeCoordinate, EscapeLocation> positions; //This could just use Coordinates, but the change isn't necessary
	private HashMap<PieceName, PieceTypeDescriptor> pieceDescriptors; //stores information about pieces

	private List<GameObserver> observers;


	/**
	 * EscapeGameManagerImpl constructor
	 * @param initializer the game initializer to use
	 */
	public EscapeGameManagerImplOld(EscapeGameInitializer initializer) {
		this.settings = new EscapeSettings();
		this.settings.coordinateType = initializer.getCoordinateType();
		this.settings.xMax = initializer.getxMax();
		this.settings.yMax = initializer.getyMax();

		for (RuleDescriptor rule : initializer.getRules())
			switch (rule.id) {
				case POINT_CONFLICT:
					this.settings.pointConflict = true;
					break;
				case REMOVE:
					this.settings.remove = true;
					break;
				case SCORE:
					this.settings.scoreLimit = rule.value;
					break;
				case TURN_LIMIT:
					this.settings.turnLimit = rule.value;
					break;
			}

		this.curPlayer = Player.PLAYER1;
		this.curTurn = 0;
		this.scores = new int[]{0,0};
		this.piecesRemaining = new int[]{0,0};

		this.positions = new HashMap<EscapeCoordinate, EscapeLocation>();

		// This assumes that the initializer filtered out empty and out of bounds
		// spaces. If an initializer is provided with an empty CLEAR
		// LocationInitializer, bugs may occur(?)
		for (LocationInitializer loc : initializer.getLocationInitializers()) {
			EscapeCoordinate coord = makeCoordinate(loc.x, loc.y);
			if (loc.player == null && loc.locationType == LocationType.CLEAR) continue; //This is to skip empty CLEAR spaces (so the code can assume if it isn't in positions it's an empty clear)
			else if (loc.player == null) positions.put(coord, LocationFactory.getLocation(loc));
			else { //piece is present
				positions.put(coord, LocationFactory.getLocation(new EscapePieceImpl(loc.player, loc.pieceName)));
				piecesRemaining[loc.player == Player.PLAYER1 ? 0 : 1] += 1;
			}
		}
			

		this.pieceDescriptors = new HashMap<PieceName, PieceTypeDescriptor>(); 
		for (PieceTypeDescriptor descriptor : initializer.getPieceTypes()) {
			if (descriptor.getAttribute(PieceAttributeID.VALUE) == null) { //TODO: I hate this, refactor into PieceTypeDescriptor by making setAttribute?
				List<PieceAttribute> attributes = new LinkedList<PieceAttribute>(Arrays.asList(descriptor.getAttributes()));
				attributes.add(new PieceAttribute(PieceAttributeID.VALUE, 1));
				descriptor.setAttributes(attributes.toArray(new PieceAttribute[attributes.size()]));
			}
			pieceDescriptors.put(descriptor.getPieceName(), descriptor);
		}		

		this.observers = new LinkedList<GameObserver>();
	}

	
	/**
	 * Checks if the provided coordinate is out of bounds for this board
	 * @param coord coordinate to check
	 * @return true if out of bounds, false if in bounds
	 */
	private boolean outOfBounds(EscapeCoordinate coord) {
		return (settings.xMax > 0 && (coord.getX() > settings.xMax || coord.getX() < 1 ))
			|| (settings.yMax > 0 && (coord.getY() > settings.yMax || coord.getY() < 1 ));
	}


	/**
	 * Get the coordinates neighbouring the provided node
	 * Note: does not do any checks on coordinates
	 * @param coordinate coordinate to get neighbours around
	 * @param movementPattern movement pattern to designate neighbour limitations
	 * @param jump whether to get jump neighbours intead of adjacent neighbours
	 * @return list of neighbouring coordinates
	 */
	private List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, MovementPattern movementPattern, boolean jump) { 
		List<EscapeCoordinate> neighbours = new LinkedList<EscapeCoordinate>();
		int d = jump ? 2 : 1;
		int x = coordinate.getX();
		int y = coordinate.getY();

		if (coordinate.coordinateType == CoordinateType.SQUARE) { //I cannot figure out how the hell to refactor this in the slightest. The best thing to do would be to rewrite everything but I don't have the time or sanity for that
			if (movementPattern != MovementPattern.DIAGONAL) {
				neighbours.add(makeCoordinate(x + d, y));
				neighbours.add(makeCoordinate(x - d, y));
				neighbours.add(makeCoordinate(x, y + d));
				neighbours.add(makeCoordinate(x, y - d));
			} 
			if (movementPattern != MovementPattern.ORTHOGONAL) {
				neighbours.add(makeCoordinate(x + d, y + d));
				neighbours.add(makeCoordinate(x + d, y - d));
				neighbours.add(makeCoordinate(x - d, y + d));
				neighbours.add(makeCoordinate(x - d, y - d));
			}
		} else { //Currently only works for triangle
			if (jump) { //we're getting jump neighbours
				neighbours.add(makeCoordinate(x + 1, y + 1));
				neighbours.add(makeCoordinate(x - 1, y + 1));
				neighbours.add(makeCoordinate(x + 1, y - 1));
				neighbours.add(makeCoordinate(x - 1, y - 1));
			} else if ((x + y) % 2 == 0) { //points down
				neighbours.add(makeCoordinate(x + 1, y));
			} else {
				neighbours.add(makeCoordinate(x - 1, y)); //points up
			}

			neighbours.add(makeCoordinate(x, y - d));
			neighbours.add(makeCoordinate(x, y + d));
		}
		
		return neighbours;
	}


	/**
	 * Find a path from the source to the target with OMNI movement pattern (assumes already valid with FLY)
	 * @param source starting coordinate
	 * @param target target coordinate
	 * @param descriptor descriptor for piece (movement patterns, max distance, etc.)
	 * @return if there exists a valid path
	 */
	boolean omniPath(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor) {
		LinkedList<EscapeCoordinate> curLayer = new LinkedList<EscapeCoordinate>(); //current layer to check
		List<EscapeCoordinate> visited = new LinkedList<EscapeCoordinate>(); //visited nodes
		List<EscapeCoordinate> nextLayer = new LinkedList<EscapeCoordinate>(); //next layer to check. Becomes curLayer
		List<EscapeCoordinate> jumpLayer = new LinkedList<EscapeCoordinate>(); //layer of nodes to jump to. Becomes nextLayer
		
		Predicate<EscapeCoordinate> validNeighbour = coord -> { //Predicate indicating whether the neighbour is a valid space to move to and not already queued
			EscapeLocation loc = positions.get(coord);
			return !outOfBounds(coord) //not out of bounds
				&& visited.indexOf(coord) == -1 //haven't already visited this neighbour
				&& curLayer.indexOf(coord) == -1 //isn't already queued in current layer
				&& nextLayer.indexOf(coord) == -1 //isn't already queued in next layer
				&& (loc == null //empty spaces are good
					|| loc.locationType == LocationType.EXIT //exits are good
					|| (descriptor.getAttribute(PieceAttributeID.UNBLOCK) != null && loc.locationType == LocationType.BLOCK) // blocks are good when unblock is present
					|| (settings.remove 
						&& loc.getPiece() != null 
						&& loc.getPiece().getPlayer() != positions.get(source).getPiece().getPlayer())); //enemy piece spaces are good when REMOVE
		};

		int maxDistance = descriptor.getAttribute(PieceAttributeID.DISTANCE).getValue(); //set maximum distance
		boolean canJump = descriptor.getAttribute(PieceAttributeID.JUMP) != null; //set whether we can jump

		curLayer.add(source);

		int distance = 0; //start at source
		while (curLayer.size() > 0 && distance <= maxDistance) { //while valid distance and there are nodes to check
			EscapeCoordinate curNode = curLayer.pop(); //get next node 
			
			if (curNode.equals(target)) return true; //return true if target

			visited.add(curNode);

			Predicate<EscapeCoordinate> validJumpNeighbour = to -> {
				int xDif = to.getX() - curNode.getX();
				int yDif = to.getY() - curNode.getY();

				if (source.coordinateType != CoordinateType.SQUARE) return true; //not coded for other cases right now, this is to catch for them
				
				EscapeLocation jumpOver = positions.get(makeCoordinate(curNode.getX() + (xDif / 2), curNode.getY() + (yDif / 2))); //get the location being jumped over
				if (jumpOver == null || jumpOver.locationType == LocationType.CLEAR) return true; //the location being jumped over is a piece (can only jump over pieces)
				return false;
			};

			// current node is empty, source, or a block when we have UNBLOCK
			if (positions.get(curNode) == null || curNode == source || (descriptor.getAttribute(PieceAttributeID.UNBLOCK) != null && positions.get(curNode).locationType == LocationType.BLOCK)) { // empty space (cannot be BLOCK or EXIT) or starting node (need this to avoid repeating code). This means we can move forward from here
				nextLayer.addAll(getNeighbours(curNode, descriptor.getMovementPattern(), false).stream().filter(validNeighbour).collect(Collectors.toList())); //add neighbours to next layer
				if (canJump) jumpLayer.addAll(getNeighbours(curNode, descriptor.getMovementPattern(), true).stream().filter(validNeighbour).filter(validJumpNeighbour).collect(Collectors.toList())); //add jumping neighbours to jump layer
			}
			
			if (curLayer.size() == 0) { //done with layer
				if (nextLayer.size() != 0) { //next layer isn't empty
					curLayer.addAll(nextLayer); //move to next layer
					nextLayer.clear();
					distance++; //increment distance from source
					
					//move forward jump layer  //filter to make sure we're not moving forward a node we can access earlier
					if (canJump) nextLayer.addAll(jumpLayer.stream().filter(coord -> curLayer.indexOf(coord) == -1).distinct().collect(Collectors.toList()));
															 
					jumpLayer.clear(); //clear jump layer (already empty if cannot jump)
				} else if (canJump) { //next layer is empty (only possible moves are jumps)
					curLayer.addAll(jumpLayer); //move to jump layer
					jumpLayer.clear();
					distance += 2; //increment distance from source
				}
			}
		} 

		return false;
	}


	/**
	 * Find a path from the source to the target with LINEAR movement pattern (assumes path is already valid with FLY)
	 * @param source starting coordinate
	 * @param target target coordinate
	 * @param descriptor descriptor for piece (movement patterns, max distance, etc.)
	 * @return if there exists a valid path
	 */
	boolean linearPath(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor) { 
		int dx, dy;
		int xStep = (dx = target.getX() - source.getX()) == 0 ? 0 : (dx) / Math.abs(dx);
		int yStep = (dy = target.getY() - source.getY()) == 0 ? 0 : (dy) / Math.abs(dy);

		boolean jumping = false; //whether we just jumped
		EscapeCoordinate curCoord = makeCoordinate(source.getX() + xStep, source.getY() + yStep); //set curCoord to next coord in line

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
					|| targetLoc.getPiece() != null)); //target has piece (don't need to check piece owner because we assume valid FLY)
	}


	/**
	 * Find a path from the source to the target (assumes source and target are valid)
	 * @param source starting coordinate
	 * @param target target coordinate
	 * @param descriptor descriptor for piece (movement patterns, max distance, etc.)
	 * @return if there exists a valid path
	 */
	private boolean pathExists(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor) {
		switch (descriptor.getMovementPattern()) {
			case ORTHOGONAL:
			case DIAGONAL:
			case OMNI:
				return omniPath(source, target, descriptor);
			case LINEAR:
				return linearPath(source, target, descriptor);
			default: 
				return false; //This isn't possible to get but is needed to compile for some reason
		}
	}

	/**
	 * Notifies all observers with the provided message
	 * @param message message to notify with
	 */
	private void notifyObservers(String message) {
		for (GameObserver observer : observers) 
			observer.notify(message);
	}


	/**
	 * Validate if the provided coordinates are valid for a move
	 * @param source source coordinate
	 * @param target target coordinate
	 * @return if the coordinates are valid
	 */
	private boolean validateCoordinates(EscapeCoordinate source, EscapeCoordinate target) {
		EscapeLocation sourceLoc, targetLoc;
		if (source == null) {
			notifyObservers("Invalid move: Source is null");
			return false;
		} else if (target == null) {
			notifyObservers("Invalid move: Target is null");
			return false;
		} else if (outOfBounds(target)) {
			notifyObservers("Invalid move: Target is out of bounds");
			return false;
		} else if ((sourceLoc = positions.get(source)) == null) {
			notifyObservers("Invalid move: Source is empty or out of bounds");
			return false;
		} else if (sourceLoc.getPiece() == null) {
			notifyObservers("Invalid move: Source is a BLOCK or EXIT");
			return false;
		} else if (sourceLoc.getPiece().getPlayer() != curPlayer) {
			notifyObservers("Invalid move: Not allowed to move enemy player's piece");
			return false;
		} else if (
			(targetLoc = positions.get(target)) != null
			&& targetLoc.getPiece() != null 
			&& (targetLoc.getPiece().getPlayer() == curPlayer 
				|| (targetLoc.getPiece().getPlayer() != curPlayer 
					&& !settings.remove))) {
			notifyObservers("Invalid move: Cannot land on enemy piece without the REMOVE rule");
			return false;
		} else if (targetLoc != null && targetLoc.locationType == LocationType.BLOCK) {
			notifyObservers("Invalid move: Cannot land on a BLOCK");
			return false;
		}
		return true;
	}


	/**
	 * Validate if moving from source to target is valid for the provided movement pattern and maximum distance unbounded by the board state
	 * Note: This assumes that the source and target are already valid on their own
	 * @param source source coordinate
	 * @param target target coordinate
	 * @param movementPattern movement pattern to use
	 * @param maxDistance maximum move distance
	 * @return if the move is valid ignoring the board state
	 */
	private boolean validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, MovementPattern movementPattern, int maxDistance) {
		switch (movementPattern) { 
			case OMNI:
				if (source.DistanceTo(target) > maxDistance) {
					notifyObservers("Invalid move: Target is too far away");
					return false;
				}
				break;
			case LINEAR:
				if (source.DistanceTo(target) > maxDistance) {
					notifyObservers("Invalid move: Target is too far away");
					return false;
				} else if ((Math.abs(target.getX() - source.getX()) != Math.abs(target.getY() - source.getY())
					&& target.getX() - source.getX() != 0
					&& target.getY() - source.getY() != 0)) {
						notifyObservers("Invalid move: LINEAR pieces can't move that way");
						return false;
				}
				break;
			case ORTHOGONAL:
				if (Math.abs(target.getX() - source.getX()) + Math.abs(target.getY() - source.getY()) > maxDistance) {
					notifyObservers("Invalid move: Target is too far away");
					return false;
				}
				break;
			case DIAGONAL:
				if ((target.getX() + target.getY()) % 2 != (source.getX() + source.getY()) % 2) {
					notifyObservers("Invalid move: DIAGONAL pieces can't move that way");
					return false;
				} else if (Math.abs(target.getX() - source.getX()) > maxDistance
					|| Math.abs(target.getY() - source.getY()) > maxDistance) {
					notifyObservers("Invalid move: Target is too far away");
					return false;
				}
				break;
		}

		return true;
	}


	/**
	 * Check if moving a piece from source to target is valid
	 * @param source starting coordinate
	 * @param target ending coordinate
	 * @return validity of move
	 */
	private boolean validMove(EscapeCoordinate source, EscapeCoordinate target) {
		if (!validateCoordinates(source, target)) return false;

		PieceTypeDescriptor descriptor = pieceDescriptors.get(positions.get(source).getPiece().getName()); 

		int maxDistance = descriptor.getAttribute(PieceAttributeID.FLY) != null
			? descriptor.getAttribute(PieceAttributeID.FLY).getValue()
			: descriptor.getAttribute(PieceAttributeID.DISTANCE).getValue();

		if (!validateUnbounded(source, target, descriptor.getMovementPattern(), maxDistance)) return false;

		if (descriptor.getAttribute(PieceAttributeID.FLY) == null) {//if FLY is null then DISTANCE must not be and we need to search
			if (!pathExists(source, target, descriptor)) {
				notifyObservers("Invalid move: No path to target");
				return false;
			}
		}

		return true;
		
	}


	/**
	 * Return if the game is over (also notifies observers)
	 * @param beforeMove if this is being run at the start of a move (checking if the game is already over vs if the move caused an end state)
	 * @return if the game is over
	 */ 
	private boolean gameIsOver(boolean beforeMove) { //This is awful but it works
		String[] messages = beforeMove 
			? new String[]{"Game is already won by PLAYER1", "Game is already won by PLAYER2", "Game is already over: Draw"}
			: new String[]{"PLAYER1 wins", "PLAYER2 wins", "Game is over and results in a draw"};

		if (settings.scoreLimit != null && (scores[0] >= settings.scoreLimit || scores[1] >= settings.scoreLimit)) {
			notifyObservers(scores[0] > scores[1] ? messages[0] : messages[1]); //if player1 has more points they must have been the one to win
			return true;
		} else if (settings.turnLimit != null && curTurn >= settings.turnLimit) {
			notifyObservers(
				scores[0] > scores[1] //player 1 has more score
				? messages[0] //player 1 won
				: (scores[1] > scores[0] //player 2 has more score
					? messages[1] //player 2 won
					: messages[2]) //draw
			); 
			return true;
		} else if (beforeMove && piecesRemaining[curPlayer == Player.PLAYER1 ? 0 : 1] == 0) { //for some reason Pollice wants this checked at the start of moves. I disagree but whatever
			if (curPlayer == Player.PLAYER1) notifyObservers("PLAYER2 wins"); //because these disagree with the normal convention, these have to be hardcoded
			else notifyObservers("PLAYER1 wins");
			return true;
		}
		
		return false; // none of the game over states passed, so the game is not over
	}


	@Override
	public boolean move(EscapeCoordinate from, EscapeCoordinate to) {
		if (gameIsOver(true)) return false;
		if (!validMove(from, to)) return false;

		EscapeLocation fromLoc = positions.get(from);
		EscapeLocation toLoc = positions.get(to);

		if (toLoc == null) positions.put(to, toLoc = LocationFactory.getLocation(fromLoc.getPiece())); //if null target location (empty), initialize new location with sourcepiece
		else if (toLoc.locationType != LocationType.EXIT) toLoc.setPiece(fromLoc.getPiece()); //not exit (must be enemy or empty, already checked for blocks), so set piece
		else if (toLoc.locationType == LocationType.EXIT) {
			scores[curPlayer == Player.PLAYER1 ? 0 : 1] += pieceDescriptors.get(fromLoc.getPiece().getName()).getAttribute(PieceAttributeID.VALUE).getValue();
			piecesRemaining[curPlayer == Player.PLAYER1 ? 0 : 1] -= 1;
		}
		positions.remove(from); //no reason to keep the coordinate after moving a piece off it (must be a clear location). Free up some memory (This line can actually be removed by replacing fromLoc with positions.remove(from), but that hurts readability)
		
		if (curPlayer == Player.PLAYER2) {
			curPlayer = Player.PLAYER1;
			curTurn++;
		} else curPlayer = Player.PLAYER2;

		gameIsOver(false); //Need to do this to check if the move ended the game and notify observers
		
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
	
	@Override
	public GameObserver addObserver(GameObserver observer) {
		observers.add(observer);
		return observer;
	}

	@Override
	public GameObserver removeObserver(GameObserver observer) {
		return observers.remove(observer) ? observer : null;
	}
}
