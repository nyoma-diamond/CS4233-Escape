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
	void nothingAt1_1() { //location not made during initialization
		EscapePiece piece = manager.getPieceAt(manager.makeCoordinate(1, 1));
		assertNull(piece);
	}

	@Test
	void nothingAt3_5() { //location made during initialization
		EscapePiece piece = manager.getPieceAt(manager.makeCoordinate(3, 5));
		assertNull(piece);
	}
	
	@Test
	void getPieceNotNull() {
		assertNotNull(manager.getPieceAt(manager.makeCoordinate(4, 4)));
	}

	@Test
	void snailAt4_4() {
		EscapePiece piece = manager.getPieceAt(manager.makeCoordinate(4, 4));
		assertNotNull(piece);
		assertEquals(PieceName.SNAIL, piece.getName());
		assertEquals(Player.PLAYER1, piece.getPlayer());
	}

	@Test
	void getPieceAtBadCoordinate() throws Exception {
		EscapePiece piece = manager.getPieceAt(new EscapeGameBuilder("config/egc/test1.egc").makeGameManager().makeCoordinate(4, 4));
		assertNull(piece);
	}

	@Test
	void movePieceToEmptySpace() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(1, 1);
		assertTrue(manager.move(c1, c2));
	}

	@Test
	void movePieceToFilledSpace() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(7, 6);
		assertFalse(manager.move(c1, c2));
	}

	@Test
	void movePieceFromEmptySpace() {
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(2, 2);
		assertFalse(manager.move(c1, c2));
	}

	@Test
	void moveNullCoord() {
		Coordinate c = manager.makeCoordinate(1, 1);
		assertFalse(manager.move(c, null));
		assertFalse(manager.move(null, c));
		assertFalse(manager.move(null, null));
	}

	@Test
	void moveToBlock() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(3, 5);
		assertFalse(manager.move(c1, c2));
	}

	@Test
	void moveRemovesPieceFromSource() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(1, 1);
		manager.move(c1, c2);
		assertNull(manager.getPieceAt(c1));
	}

	@Test
	void invalidMoveLeavesPieceAtSource() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(3, 5);

		EscapePiece p = manager.getPieceAt(c1);
		manager.move(c1, c2);
		assertEquals(p, manager.getPieceAt(c1));
	}

	@Test
	void movePutsPieceAtDestination() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(1, 1);

		EscapePiece p = manager.getPieceAt(c1);
		manager.move(c1, c2);
		assertEquals(p, manager.getPieceAt(c2));
	}

	@Test
	void invalidMoveDoesntPutPieceAtDestination() {
		Coordinate c1 = manager.makeCoordinate(4, 4); 
		Coordinate c2 = manager.makeCoordinate(3, 5); 

		manager.move(c1, c2);
		assertNull(manager.getPieceAt(c2));
	}

	@Test
	void moveToExitRemovesPiece() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(5, 12);
		assertTrue(manager.move(c1, c2));
		assertNull(manager.getPieceAt(c2));
	}
	
	@Test
	void movePieceToEnemyFilledSpace() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(10, 12);
		assertTrue(manager.move(c1, c2));
	}

	@Test
	void movePieceToEnemyFilledSpaceReplacesPiece() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(10, 12);

		EscapePiece p = manager.getPieceAt(c1);
		manager.move(c1, c2);
		assertEquals(p, manager.getPieceAt(c2));
	}

	@Test
	void player1CantMovePlayer2() {
		Coordinate c1 = manager.makeCoordinate(10, 12);
		Coordinate c2 = manager.makeCoordinate(1, 1);

		assertFalse(manager.move(c1, c2));
	}

	@Test
	void player2CantMovePlayer1() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(1, 1);

		manager.move(c1, c2);
		assertFalse(manager.move(c2, c1));
	}

	@Test
	void moveChangesBackTurn() {
		Coordinate c1 = manager.makeCoordinate(4, 4); //p1 piece
		Coordinate c2 = manager.makeCoordinate(1, 1); //clear
		Coordinate c3 = manager.makeCoordinate(10, 12); //p2 piece

		manager.move(c1, c2); //p1 turn: p1 to clear 
		manager.move(c3, c1); //p2 turn: p2 to clear 
		assertFalse(manager.move(c1, c3)); //p1 turn: p2 to clear
	}

	@Test
	void invalidMoveDoesntChangeTurn() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(10, 12);

		assertFalse(manager.move(c2, c1));
		assertTrue(manager.move(c1, c2));
	}

	@Test
	void foo() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(10, 12);

		assertFalse(manager.move(c2, c1));
		assertTrue(manager.move(c1, c2));
	}
}
