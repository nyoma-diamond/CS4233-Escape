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

package escape;

import escape.exception.EscapeException;
import escape.required.*;

/**
 * This interface describes the behavior of the Escape game manager. Every instance
 * of an Escape game manager must implement this interface. The methods described here are
 * the only methods that a client can use when interacting with the game.
 * 
 * MODIFIABLE: NO
 * MOVEABLE: NO
 * REQUIRED: YES
 * 
 * You may extend this interface for your internal use, but this is the 
 * public interface that all clients will use.
 */
public interface EscapeGameManager<C extends Coordinate> {
	/**
	 * Make the move in the current game.
	 * @param from starting location
	 * @param to ending location
	 * @return true if the move was legal, false otherwise
	 */
	boolean move(C from, C to);

	/**
	 * Return the piece located at the specified coordinate. If executing
	 * this method in the game instance causes an exception, then this method
	 * returns null and sets the status message appropriately. The status message
	 * is not used in the initial release(s) of the game.
	 * @param coordinate the location to probe
	 * @return the piece at the specified location or null if there is none
	 */
	EscapePiece getPieceAt(C coordinate);

	/**
	 * Returns a coordinate of the appropriate type. If the coordinate cannot be
	 * created, then null is returned and the status message is set appropriately.
	 * @param x the x component
	 * @param y the y component
	 * @return the coordinate or null if the coordinate cannot be implemented
	 */
	C makeCoordinate(int x, int y);

	/**
	 * Add an observer to this manager. Whever the move() method returns
	 * false, the observer will be notified with a message indication the
	 * problem.
	 * @param observer
	 * @return the observer
	 */
	default GameObserver addObserver(GameObserver observer) {
		throw new EscapeException("Not implemented");
	}

	/**
	 * Remove an observer from this manager. The observer will no longer
	 * receive notifications from this game manager.
	 * @param observer
	 * @return the observer that was removed or null if it had not previously
	 *     been registered
	 */
	default GameObserver removeObserver(GameObserver observer) {
		throw new EscapeException("Not implemented");
	}
}
