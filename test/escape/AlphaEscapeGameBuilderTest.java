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
class AlphaEscapeGameBuilderTest {

	private static EscapeGameManager manager;

	@BeforeEach
	void loadGame() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test1.egc");
		manager = egb.makeGameManager();
	}

	// #1
	@Test
	void canCreateGame() {
		assertNotNull(manager);
	}

	// #2
	@Test
	void coordinateNotNull() {
		assertNotNull(manager.makeCoordinate(1,1));
	}

	// #3
	@Test
	void distanceToReturnsValue() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(2, 1));
		assertEquals(1, dist);
	}

	// #4
	@Test
	void distanceToHorizontal() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(3, 1));
		assertEquals(2, dist);
	}

	// #5
	@Test
	void distanceToHorizontalBackwards() {
		int dist = manager.makeCoordinate(3, 1).DistanceTo(manager.makeCoordinate(1, 1));
		assertEquals(2, dist);
	}

	// #6
	@Test
	void distanceToVertical() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(1, 4));
		assertEquals(3, dist);
	}

	// #7
	@Test
	void distanceToVerticalBackwards() {
		int dist = manager.makeCoordinate(1, 4).DistanceTo(manager.makeCoordinate(1, 1));
		assertEquals(3, dist);
	}

	// #8
	@Test
	void distanceToDiagonal() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(5, 5));
		assertEquals(4, dist);
	}

	// #9
	@Test
	void distanceToDiagonalBackwards() {
		int dist = manager.makeCoordinate(5, 5).DistanceTo(manager.makeCoordinate(1, 1));
		assertEquals(4, dist);
	}

	// #10
	@Test
	void distanceToNonStraight() { //This worked without me changing anything. Apparently what I did for Diagonals also works for non-straight lines
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(3, 6));
		assertEquals(5, dist);
	}

	// #11
	@Test
	void distanceToNonStraightBackwards() { //This worked without me changing anything. Apparently what I did for Diagonals also works for non-straight lines
		int dist = manager.makeCoordinate(3, 6).DistanceTo(manager.makeCoordinate(1, 1));
		assertEquals(5, dist);
	}

	// #12
	@Test
	void distanceToInvalidCoordinate() {
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = (c) -> {
			return 0;
		};
		assertThrows(EscapeException.class, () -> c1.DistanceTo(c2));
	}

	// #13
	@Test
	void coordinatesAreEquivalent() { 
		Coordinate c = manager.makeCoordinate(1, 1);
		assertTrue(manager.makeCoordinate(1, 1).equals(c));
	}

	// #14
	@Test
	void makeCoordinateIndependentOfBoard() {
		assertNotNull(manager.makeCoordinate(26, 1));
		assertNotNull(manager.makeCoordinate(1, 33));
		assertNotNull(manager.makeCoordinate(26, 40));
		assertNotNull(manager.makeCoordinate(-20, 1));
		assertNotNull(manager.makeCoordinate(1, -21));
		assertNotNull(manager.makeCoordinate(0, -10));
	}

	// #15
	@Test
	void distanceToOutOfBoundsWorks() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(-1, -5));
		assertEquals(6, dist);		
	}

	// #16
	@Test
	void nothingAt1_1() { //location not made during initialization
		EscapePiece piece = manager.getPieceAt(manager.makeCoordinate(1, 1));
		assertNull(piece);
	}

	// #17
	@Test
	void nothingAt3_5() { //location made during initialization
		EscapePiece piece = manager.getPieceAt(manager.makeCoordinate(3, 5));
		assertNull(piece);
	}
	
	// #18
	@Test
	void getPieceNotNull() {
		assertNotNull(manager.getPieceAt(manager.makeCoordinate(4, 4)));
	}

	// #19
	@Test
	void snailAt4_4() {
		EscapePiece piece = manager.getPieceAt(manager.makeCoordinate(4, 4));
		assertNotNull(piece);
		assertEquals(PieceName.SNAIL, piece.getName());
		assertEquals(Player.PLAYER1, piece.getPlayer());
	}

	// #20
	@Test
	void getPieceAtMadeCoordinate() {
		EscapePiece p1 = manager.getPieceAt(manager.makeCoordinate(4, 4));
		EscapePiece p2 = manager.getPieceAt(manager.makeCoordinate(4, 4));
		assertEquals(p1, p2);
	}

	// #21
	@Test
	void movePieceToEmptySpace() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(1, 1);
		assertTrue(manager.move(c1, c2));
	}

	// #22
	@Test
	void movePieceToFilledOwnedSpace() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(7, 6);
		assertFalse(manager.move(c1, c2));
	}

	// #23
	@Test
	void movePieceFromEmptySpace() {
		Coordinate c1 = manager.makeCoordinate(1, 1);
		Coordinate c2 = manager.makeCoordinate(2, 2);
		assertFalse(manager.move(c1, c2));
	}

	// #24
	@Test
	void moveNullCoord() {
		Coordinate c = manager.makeCoordinate(1, 1);
		assertFalse(manager.move(c, null));
		assertFalse(manager.move(null, c));
		assertFalse(manager.move(null, null));
	}

	// #25
	@Test
	void moveToBlock() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(3, 5);
		assertFalse(manager.move(c1, c2));
	}

	// #26
	@Test
	void moveRemovesPieceFromSource() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(1, 1);
		manager.move(c1, c2);
		assertNull(manager.getPieceAt(c1));
	}

	// #27
	@Test
	void invalidMoveLeavesPieceAtSource() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(3, 5);

		EscapePiece p = manager.getPieceAt(c1);
		manager.move(c1, c2);
		assertEquals(p, manager.getPieceAt(c1));
	}

	// #28
	@Test
	void movePutsPieceAtDestination() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(1, 1);

		EscapePiece p = manager.getPieceAt(c1);
		manager.move(c1, c2);
		assertEquals(p, manager.getPieceAt(c2));
	}

	// #29
	@Test
	void invalidMoveDoesntPutPieceAtDestination() {
		Coordinate c1 = manager.makeCoordinate(4, 4); 
		Coordinate c2 = manager.makeCoordinate(3, 5); 

		manager.move(c1, c2);
		assertNull(manager.getPieceAt(c2));
	}

	// #30
	@Test
	void moveToExitRemovesPiece() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(5, 12);
		assertTrue(manager.move(c1, c2));
		assertNull(manager.getPieceAt(c2));
	}
	
	// #31
	@Test
	void movePieceToEnemyFilledSpace() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(10, 12);
		assertTrue(manager.move(c1, c2));
	}

	// #32
	@Test
	void movePieceToEnemyFilledSpaceReplacesPiece() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(10, 12);

		EscapePiece p = manager.getPieceAt(c1);
		manager.move(c1, c2);
		assertEquals(p, manager.getPieceAt(c2));
	}

	// #33
	@Test
	void movePieceToSource() { 
		Coordinate c = manager.makeCoordinate(4, 4);
		assertFalse(manager.move(c,c)); //this was assertTrue in Alpha, but is now assertFalse from Beta on
	}

	// #34
	@Test
	void movePieceToSourceKeepsPiece() {
		Coordinate c = manager.makeCoordinate(4, 4);
		EscapePiece p = manager.getPieceAt(c);
		manager.move(c,c);
		assertEquals(p, manager.getPieceAt(c));
	}

	// #35
	@Test
	void player1CantMovePlayer2() {
		Coordinate c1 = manager.makeCoordinate(10, 12);
		Coordinate c2 = manager.makeCoordinate(1, 1);

		assertFalse(manager.move(c1, c2));
	}

	// #36
	@Test
	void player2CantMovePlayer1() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(1, 1);

		manager.move(c1, c2);
		assertFalse(manager.move(c2, c1));
	}

	// #37
	@Test
	void moveChangesBackTurn() {
		Coordinate c1 = manager.makeCoordinate(4, 4); //p1 piece
		Coordinate c2 = manager.makeCoordinate(1, 1); //clear
		Coordinate c3 = manager.makeCoordinate(10, 12); //p2 piece

		manager.move(c1, c2); //p1 turn: p1 to clear 
		manager.move(c3, c1); //p2 turn: p2 to clear 
		assertFalse(manager.move(c1, c3)); //p1 turn: p2 to clear
	}

	// #38
	@Test
	void invalidMoveDoesntChangeTurn() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(10, 12);

		assertFalse(manager.move(c2, c1));
		assertTrue(manager.move(c1, c2));
	}

	// #39
	@Test
	void moveOutOfBoundsFails() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(50, 50);
		Coordinate c3 = manager.makeCoordinate(-1, 4);
		Coordinate c4 = manager.makeCoordinate(1, 1);

		assertFalse(manager.move(c1, c2));
		assertFalse(manager.move(c1, c3));
		assertFalse(manager.move(c2, c1));
		assertFalse(manager.move(c2, c4));
	}

	// #40
	@Test
	void moveToCoordinateMadeByOtherManager() throws Exception {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = new EscapeGameBuilder("config/egc/test2.egc").makeGameManager().makeCoordinate(1, 1);
		assertTrue(manager.move(c1, c2));
	}

	// #41
	@Test
	void badMoveFromCoordinateMadeByOtherManager() throws Exception {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = new EscapeGameBuilder("config/egc/test2.egc").makeGameManager().makeCoordinate(1, 1);
		assertFalse(manager.move(c2, c1));
	}

	// #42
	@Test
	void moveFromCoordinateMadeByOtherManager() throws Exception {
		Coordinate c1 = new EscapeGameBuilder("config/egc/test2.egc").makeGameManager().makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(1, 1);
		assertTrue(manager.move(c1, c2));
	}

	// #43
	@Test
	void moveToInitializedClear() throws Exception { //This is to try and test if creating a board with a empty clear location initializer causes problems. Test doesn't actually work because EscapeGameBuilder already filters those out
		EscapeGameManager m = new EscapeGameBuilder("config/egc/test3.egc").makeGameManager();
		assertTrue(m.move(m.makeCoordinate(4, 4), m.makeCoordinate(5, 5)));
	}
}
