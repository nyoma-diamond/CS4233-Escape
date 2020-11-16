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

import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import escape.exception.EscapeException;
import escape.required.*;
import escape.required.EscapePiece.PieceName;

/**
 * This is a simple test, not really a unit test, to make sure tht the
 * EscapeGameBuilder, in the starting code, is actually working.
 * 
 * @version May 30, 2020
 */
class EscapeGameBuilderTest {

	private static EscapeGameManager manager;

	@BeforeEach
	void loadGame() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test1.egc");
		manager = egb.makeGameManager();
	}

	@Test
	void canCreateGame() {
		assertNotNull(manager);
	}

	//TODO: get tests for coordinates directly in here

	@Test
	void coordinateNotNull() {
		assertNotNull(manager.makeCoordinate(1,1));
	}

	@Test
	void distanceToReturnsValue() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(2, 1));
		assertEquals(1, dist);
	}

	@Test
	void distanceToHorizontal() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(3, 1));
		assertEquals(2, dist);
	}

	@Test
	void distanceToHorizontalBackwards() {
		int dist = manager.makeCoordinate(3, 1).DistanceTo(manager.makeCoordinate(1, 1));
		assertEquals(2, dist);
	}

	@Test
	void distanceToVertical() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(1, 4));
		assertEquals(3, dist);
	}

	@Test
	void distanceToVerticalBackwards() {
		int dist = manager.makeCoordinate(1, 4).DistanceTo(manager.makeCoordinate(1, 1));
		assertEquals(3, dist);
	}

	@Test
	void distanceToDiagonal() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(5, 5));
		assertEquals(4, dist);
	}

	@Test
	void distanceToDiagonalBackwards() {
		int dist = manager.makeCoordinate(5, 5).DistanceTo(manager.makeCoordinate(1, 1));
		assertEquals(4, dist);
	}

	@Test
	void distanceToNonStraight() { //This worked without me changing anything. Apparently what I did for Diagonals also works for non-straight lines
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(3, 6));
		assertEquals(5, dist);
	}

	@Test
	void distanceToNonStraightBackwards() { //This worked without me changing anything. Apparently what I did for Diagonals also works for non-straight lines
		int dist = manager.makeCoordinate(3, 6).DistanceTo(manager.makeCoordinate(1, 1));
		assertEquals(5, dist);
	}

	@Test
	void incorrectCoordinateType() {
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = (c) -> {
			return 0;
		};
		assertThrows(EscapeException.class, () -> c1.DistanceTo(c2));
	}

	@Test
	void coordinateAlreadyExists() {
		manager.makeCoordinate(1, 1);
		assertNull(manager.makeCoordinate(1, 1));
	}

	@Test
	void coordinateOutOfBounds() {
		assertNull(manager.makeCoordinate(26, 1));
		assertNull(manager.makeCoordinate(1, 21));
		assertNull(manager.makeCoordinate(26, 21));
		assertNull(manager.makeCoordinate(0, 1));
		assertNull(manager.makeCoordinate(1, 0));
		assertNull(manager.makeCoordinate(0, 0));
	}

	@Test
	void getPieceNotNull() {
		assertNotNull(manager.getPieceAt(manager.makeCoordinate(4, 4)));
	}

	@Test
	void horseAt6_2() {
		EscapePiece piece = manager.getPieceAt(manager.makeCoordinate(6, 2));
		assertNotNull(piece);
		assertEquals(PieceName.HORSE, piece.getName());
		assertEquals(Player.PLAYER1, piece.getPlayer());
	}



}
