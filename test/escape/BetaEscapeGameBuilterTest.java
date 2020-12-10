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

import escape.required.*;

/**
 * This is a simple test, not really a unit test, to make sure tht the
 * EscapeGameBuilder, in the starting code, is actually working.
 * 
 * @version May 30, 2020
 */
class BetaEscapeGameBuilderTest {

	private static EscapeGameManager manager2, manager3, manager4, manager5, manager6, manager7, manager8, tManager, tManager2, tManager3, tManager4;

	@BeforeEach
	void loadGame() throws Exception {
		manager2 = new EscapeGameBuilder("config/egc/test2.egc").makeGameManager();
		manager3 = new EscapeGameBuilder("config/egc/test3.egc").makeGameManager();
		manager4 = new EscapeGameBuilder("config/egc/test4.egc").makeGameManager();
		manager5 = new EscapeGameBuilder("config/egc/test5.egc").makeGameManager();
		manager6 = new EscapeGameBuilder("config/egc/test6.egc").makeGameManager();
		manager7 = new EscapeGameBuilder("config/egc/test7.egc").makeGameManager();
		manager8 = new EscapeGameBuilder("config/egc/test8.egc").makeGameManager();
		tManager = new EscapeGameBuilder("config/egc/triangles.egc").makeGameManager();
		tManager2 = new EscapeGameBuilder("config/egc/triangles2.egc").makeGameManager();
		tManager3 = new EscapeGameBuilder("config/egc/triangles3.egc").makeGameManager();
		tManager4 = new EscapeGameBuilder("config/egc/triangles4.egc").makeGameManager();
	}

	// #1
	@Test
	void limitedByDistanceOmni() { //this test is kind of unnecessary :p
		Coordinate c = manager2.makeCoordinate(1, 1);
		assertFalse(manager2.move(c, manager2.makeCoordinate(7, 1)));
		assertFalse(manager2.move(c, manager2.makeCoordinate(1, 8)));
		assertFalse(manager2.move(c, manager2.makeCoordinate(9, 10)));
	}

	/**
	 * Test valid moves
	 * (First values of x/ySequence are the starting space)
	 * Last move will be the last move indicated in the sequences (NOT A DUMMY MOVE)
	 * @param manager manager to act on
	 * @param xSequence X values (paired with corresponding Y in Ys to build coordinates). Must be same length as ySequence
	 * @param ySequence Y values (paired with corresponding X in Xs to build coordinates). Must be same length as xSequence
	 * @param dummyFrom start space of dummy piece (shouldn't get in the way of anything being tested)
	 * @param dummyTo spot to move dummy piece to (shouldn't get in the way of anything being tested)
	 */
	public static void validMoves(EscapeGameManager manager, int[] xSequence, int[] ySequence, Coordinate dummyFrom, Coordinate dummyTo) {
		for (int i = 1; i < xSequence.length; i++) {
			assertTrue(
				"<"+xSequence[i-1]+","+ySequence[i-1]+"> to <"+xSequence[i]+","+ySequence[i]+">",
				manager.move(
					manager.makeCoordinate(xSequence[i-1], ySequence[i-1]),
					manager.makeCoordinate(xSequence[i], ySequence[i])));	
			
			if (i != xSequence.length-1) {
				manager.move(dummyFrom, dummyTo); //move dummy to change turn
				Coordinate temp = dummyFrom;
				dummyFrom = dummyTo;
				dummyTo = temp;
			}
		}
	}


	// #2
	@Test
	void validLinearFlyMove() {
		validMoves(
			manager2, 
			new int[]{1,4,4,2,4,6,4,2,2}, 
			new int[]{3,3,8,6,4,6,8,8,4}, 
			manager2.makeCoordinate(1, 9), 
			manager2.makeCoordinate(1, 10));
	}

	/**
	 * Test invalid moves
	 * @param manager manager to act on
	 * @param c coordinate of piece to attempt to move
	 * @param xSequence X values (paired with corresponding Y in Ys to build coordinates). Must be same length as ySequence 
	 * @param ySequence Y values (paired with corresponding X in Xs to build coordinates). Must be same length as xSequence
	 */
	public static void invalidMoves(EscapeGameManager manager, Coordinate c, int[] xSequence, int[] ySequence) {
		for (int i = 0; i < xSequence.length; i++) 
			assertFalse(
				"<"+xSequence[i]+","+ySequence[i]+">",
				manager.move(c, manager.makeCoordinate(xSequence[i], ySequence[i])));
	}

	// #3
	@Test
	void invalidLinearFlyMove() {
		Coordinate c = manager2.makeCoordinate(6, 8);
		assertTrue(manager2.move(manager2.makeCoordinate(1, 3), c)); //move piece to better spot for testing (these have asserts to ensure these went through and we're not getting false positives)
		assertTrue(manager2.move(manager2.makeCoordinate(1, 9), manager2.makeCoordinate(1, 10))); //change turn back
		
		invalidMoves(
			manager2,
			c,
			new int[]{8,9,2,5,16}, 
			new int[]{9,6,9,5,8});
	}

	// #4
	@Test
	void validOrthogonalFlyMove() {
		validMoves(
			manager2, 
			new int[]{1,2,7,5}, 
			new int[]{7,11,11,8}, 
			manager2.makeCoordinate(1, 9), 
			manager2.makeCoordinate(1, 10));
	}

	// #5
	@Test
	void invalidOrthogonalFlyMove() {
		Coordinate c = manager2.makeCoordinate(5, 8);
		assertTrue(manager2.move(manager2.makeCoordinate(1, 7), c)); //move piece to better spot for testing (these have asserts to ensure these went through and we're not getting false positives)
		assertTrue(manager2.move(manager2.makeCoordinate(1, 9), manager2.makeCoordinate(1, 10))); //change turn back

		invalidMoves(
			manager2, 
			c,
			new int[]{6,1,8, 5, 11}, 
			new int[]{3,6,11,14,8});
	}

	// #6
	@Test
	void validDiagonalFlyMove() {
		validMoves(
			manager2, 
			new int[]{1,6,7,2}, 
			new int[]{5,10,5,2}, 
			manager2.makeCoordinate(1, 9), 
			manager2.makeCoordinate(1, 10));
	}

	// #7
	@Test
	void invalidDiagonalFlyMove() {
		invalidMoves(
			manager2, 
			manager2.makeCoordinate(1, 5),
			new int[]{1,5,1,7,5}, 
			new int[]{6,8,2,5,4});
	}


	// ========================= DISTANCE ==========================


	// #8
	@Test
	void validOmniDistanceMove() {
		validMoves(
			manager3, 
			new int[]{1,6,1,2,6}, 
			new int[]{1,5,5,2,6}, 
			manager3.makeCoordinate(1, 9), 
			manager3.makeCoordinate(1, 10));
	}

	// #9
	@Test
	void invalidOmniDistanceMove() {
		invalidMoves(
			manager3, 
			manager3.makeCoordinate(1, 1),
			new int[]{7,1,6,4}, 
			new int[]{1,4,6,6});
	}

	// #10
	@Test
	void validLinearDistanceMove() { //This worked immediately because these all also work under OMNI, which is the only other thing tested for DISTANCE right now
		validMoves(
			manager3, 
			new int[]{3,3,8,4,2}, 
			new int[]{3,8,3,3,1}, 
			manager3.makeCoordinate(1, 9), 
			manager3.makeCoordinate(1, 10));
	}

	// #11
	@Test
	void invalidLinearDistanceMove() {
		Coordinate c = manager3.makeCoordinate(2, 4);
		assertTrue(manager3.move(manager3.makeCoordinate(3, 3), c)); //move piece to better spot for testing (these have asserts to ensure these went through and we're not getting false positives)
		assertTrue(manager3.move(manager3.makeCoordinate(1, 9), manager3.makeCoordinate(1, 10))); //change turn back

		invalidMoves(
			manager3, 
			c,
			new int[]{5,2,4}, 
			new int[]{1,2,3});
	}

	// #12
	@Test
	void validOrthoDistanceMove() {
		validMoves(
			manager3, 
			new int[]{2,4,2,1,5}, 
			new int[]{3,3,6,2,1}, 
			manager3.makeCoordinate(1, 9), 
			manager3.makeCoordinate(1, 10));
	}

	// #13
	@Test
	void invalidOrthoDistanceMove() {
		/* P = piece, * = valid move, [ ] = invalid move, X = source
		6 [ ][ ][ ][*][ ][ ][ ][ ]
		5 [*][ ][*][*][*][ ][ ][ ]
		4 [*][P][*][*][*][*][ ][ ]
		3 [*][*][P][*][*][*][*][ ]
		2 [*][*][X][*][*][*][*][*]
		1 [P][*][P][*][*][*][*][ ]
		   1  2  3  4  5  6  7  8
		*/

		Coordinate c = manager3.makeCoordinate(2, 3);
		invalidMoves(
			manager3, 
			c,
			new int[]{6,6,5,6,2}, 
			new int[]{1,2,2,3,9});
		
		/* P = piece, * = valid move, [ ] = invalid move, X = source
		6 [*][*][ ][ ][ ][ ][ ][ ]
		5 [*][*][*][ ][ ][ ][ ][ ]
		4 [*][P][ ][*][ ][ ][ ][ ]
		3 [*][X][P][*][*][ ][ ][ ]
		2 [*][*][*][*][*][*][ ][ ]
		1 [P][*][P][*][*][ ][ ][ ]
		   1  2  3  4  5  6  7  8
		*/
		assertTrue(manager3.move(c, c = manager3.makeCoordinate(3, 2))); //move to another spot
		assertTrue(manager3.move(manager3.makeCoordinate(1, 9), manager3.makeCoordinate(1, 10))); //change turn back

		invalidMoves(
			manager3, 
			c,
			new int[]{4,6,5,3}, 
			new int[]{3,3,4,6});
	}

	// #14
	@Test
	void validDiagDistanceMove() {
		validMoves(
			manager3, 
			new int[]{1,6,5,2,3}, 
			new int[]{3,2,1,6,1}, 
			manager3.makeCoordinate(1, 9), 
			manager3.makeCoordinate(1, 10));
	}

	// #15
	@Test
	void invalidDiagDistanceMove() {
		/* P = piece, * = valid move, [ ] = invalid move, X = source
		6 [ ][*][ ][*][ ][*][ ][*]
		5 [ ][ ][*][ ][*][ ][*][ ]
		4 [ ][P][ ][*][ ][*][ ][*]
		3 [*][ ][P][ ][*][ ][*][ ]
		2 [ ][*][P][*][ ][*][ ][*]
		1 [P][ ][X][ ][*][ ][*][ ]
		   1  2  3  4  5  6  7  8
		*/
		invalidMoves(
			manager3, 
			manager3.makeCoordinate(1, 3),
			new int[]{1,7,5,1}, 
			new int[]{2,9,1,9});

		/* P = piece, * = valid move, [ ] = invalid move, X = source
		6 [ ][*][ ][*][ ][*][ ][ ]
		5 [*][ ][*][ ][*][ ][*][ ]
		4 [ ][X][ ][*][ ][*][ ][ ]
		3 [*][ ][P][ ][*][ ][*][ ]
		2 [ ][*][P][*][ ][*][ ][ ]
		1 [P][ ][P][ ][ ][ ][ ][ ]
		   1  2  3  4  5  6  7  8
		*/
		invalidMoves(
			manager3, 
			manager3.makeCoordinate(3, 2), //other diag piece
			new int[]{1,1}, 
			new int[]{5,7});
	}


	// ========================= JUMP ========================== 
	
	// Testing linear first because it has different pathfinding
	// #16
	@Test //This is expected to already work. Needed just to make sure I don't break earlier code.
	void validLinearMoveWithJump() {
		validMoves( 
			manager4, 
			new int[]{3,6,1,6,5,3}, 
			new int[]{7,4,4,9,9,7}, 
			manager4.makeCoordinate(1, 9), 
			manager4.makeCoordinate(1,10));
	}

	// #17
	@Test
	void validSingleLinearJump() {
		validMoves(
			manager4, 
			new int[]{3,3,5,5,7}, 
			new int[]{7,4,6,8,6}, 
			manager4.makeCoordinate(1, 9), 
			manager4.makeCoordinate(1,10));	
	}

	// #18
	@Test
	void invalidLinearJump() { //trying to jump over two+ in the same jump
		invalidMoves(
			manager4, 
			manager4.makeCoordinate(3,7), 
			new int[]{7,3,3}, 
			new int[]{7,12,11});
	}

	// #19
	@Test
	void validMultiLinearJump() {
		validMoves(
			manager4, 
			new int[]{3,3,2,2,6}, 
			new int[]{7,2,3,4,8}, 
			manager4.makeCoordinate(1, 9), 
			manager4.makeCoordinate(1,10));	
	}

	// #20
	@Test
	void LinearJumpIntoValidNonEmpty() {
		validMoves(
			manager4, 
			new int[]{3,6,8}, 
			new int[]{7,7,9}, 
			manager4.makeCoordinate(1, 9), 
			manager4.makeCoordinate(1,10));
	}

	// #21
	@Test
	void validSingleOrthoJump() {
		validMoves(
			manager5, 
			new int[]{1,3,3,3,5}, 
			new int[]{1,4,9,4,6}, 
			manager5.makeCoordinate(15, 15), 
			manager5.makeCoordinate(15, 16));
	}

	// #22
	@Test
	void invalidOrthoJump() {
		assertTrue(manager5.move(manager5.makeCoordinate(1, 1), manager5.makeCoordinate(2, 2))); //move piece to better spot for testing (these have asserts to ensure these went through and we're not getting false positives)
		assertTrue(manager5.move(manager5.makeCoordinate(15, 15), manager5.makeCoordinate(15, 16))); //change turn back
		
		assertFalse(manager5.move(manager5.makeCoordinate(2, 2), manager5.makeCoordinate(2, 7)));
	}

	// #23
	@Test
	void validMultiOrthoJump() {
		validMoves(
			manager5, 
			new int[]{1,1,5}, 
			new int[]{1,6,6}, 
			manager5.makeCoordinate(15, 15), 
			manager5.makeCoordinate(15, 16));
	}

	// #24
	@Test
	void validSingleOmniJump() {
		validMoves(
			manager6, 
			new int[]{1,3,3}, 
			new int[]{1,1,4}, 
			manager6.makeCoordinate(15, 15), 
			manager6.makeCoordinate(15, 16));
	}

	// #25
	@Test
	void invalidOmniJump() {
		invalidMoves(
			manager6, 
			manager6.makeCoordinate(1, 1), 
			new int[]{1,4}, 
			new int[]{5,4});
	}

	// #26
	@Test
	void validMultiOmniJump() {
		validMoves(
			manager6, 
			new int[]{1,6,1,5}, 
			new int[]{1,1,1,4}, 
			manager6.makeCoordinate(15, 15), 
			manager6.makeCoordinate(15, 16));
	}

	// #24
	@Test //OOPS i accidentally tested ortho again. Forgot I already did it
	void validOrthoJump() {
		validMoves(
			manager7, 
			new int[]{1,6,3,3,1}, 
			new int[]{1,1,2,4,1}, 
			manager7.makeCoordinate(15, 15), 
			manager7.makeCoordinate(15, 16));
	}

	// #25
	@Test
	void invalidOrthJump() {
		invalidMoves(
			manager7, 
			manager7.makeCoordinate(1, 1), 
			new int[]{1,2,4}, 
			new int[]{4,3,3});
	}

	// #26
	@Test
	void validDiagJump() {
		validMoves(
			manager8, 
			new int[]{1,5,5}, 
			new int[]{1,5,1}, 
			manager8.makeCoordinate(15, 15), 
			manager8.makeCoordinate(15, 16));
	}

	// #27
	@Test
	void invalidDiagJump() {
		invalidMoves(
			manager8, 
			manager8.makeCoordinate(1, 1), 
			new int[]{6,5}, 
			new int[]{2,3});
		
		assertTrue(manager8.move(manager8.makeCoordinate(1, 1), manager8.makeCoordinate(6, 4))); // move to another spot
		assertTrue(manager8.move(manager8.makeCoordinate(15, 15), manager8.makeCoordinate(15, 16))); // change turn

		invalidMoves(
			manager8, 
			manager8.makeCoordinate(5, 4), 
			new int[]{2,1}, 
			new int[]{8,7});
	}


	// ========================= TRIANGLES =========================


	// #28
	@Test
	void triangleDistanceSameColSameO() {
		Coordinate c = tManager.makeCoordinate(3, 3);
		assertEquals(4, c.DistanceTo(tManager.makeCoordinate(5, 3)));
		assertEquals(4, c.DistanceTo(tManager.makeCoordinate(1, 3)));
		assertEquals(8, c.DistanceTo(tManager.makeCoordinate(7, 3)));
	}

	// #29
	@Test
	void triangleDistanceDownToUpDownward() {
		Coordinate c = tManager.makeCoordinate(5, 3);
		assertEquals(3, c.DistanceTo(tManager.makeCoordinate(4, 3)));
		assertEquals(7, c.DistanceTo(tManager.makeCoordinate(2, 3)));
	}

	// #30
	@Test
	void triangleDistanceDownToUpUpward() {
		Coordinate c = tManager.makeCoordinate(1, 3);
		assertEquals(1, c.DistanceTo(tManager.makeCoordinate(2, 3)));
		assertEquals(5, c.DistanceTo(tManager.makeCoordinate(4, 3)));
	}

	// #31
	@Test
	void triangleDistanceUpToDownDownward() {
		Coordinate c = tManager.makeCoordinate(5, 2);
		assertEquals(1, c.DistanceTo(tManager.makeCoordinate(4, 2)));
		assertEquals(5, c.DistanceTo(tManager.makeCoordinate(2, 2)));
	}

	// #32
	@Test
	void triangleDistanceUpToDownUpward() {
		Coordinate c = tManager.makeCoordinate(1, 2);
		assertEquals(3, c.DistanceTo(tManager.makeCoordinate(2, 2)));
		assertEquals(7, c.DistanceTo(tManager.makeCoordinate(4, 2)));
	}

	// #33
	@Test
	void triangleDistanceDxLessThanDy() {
		Coordinate c = tManager.makeCoordinate(3, 5);
		assertEquals(3, c.DistanceTo(tManager.makeCoordinate(4, 3)));
		assertEquals(2, c.DistanceTo(tManager.makeCoordinate(3, 3)));
		assertEquals(4, c.DistanceTo(tManager.makeCoordinate(2, 2)));
		assertEquals(3, c.DistanceTo(tManager.makeCoordinate(4, 7)));
		assertEquals(4, c.DistanceTo(tManager.makeCoordinate(2, 8)));
		assertEquals(5, c.DistanceTo(tManager.makeCoordinate(2, 9)));
	}

	// #34
	@Test
	void triangleDistanceDxGreaterThanDySameOrientation() { //NOTE: this also includes Dx = Dy
		Coordinate c = tManager.makeCoordinate(5, 4);
		assertEquals(2, c.DistanceTo(tManager.makeCoordinate(6, 5)));
		assertEquals(6, c.DistanceTo(tManager.makeCoordinate(8, 5)));
		assertEquals(4, c.DistanceTo(tManager.makeCoordinate(7, 2)));
		assertEquals(6, c.DistanceTo(tManager.makeCoordinate(8, 7)));
		assertEquals(8, c.DistanceTo(tManager.makeCoordinate(9, 2)));
		assertEquals(2, c.DistanceTo(tManager.makeCoordinate(4, 5)));
		assertEquals(6, c.DistanceTo(tManager.makeCoordinate(2, 5)));
		assertEquals(4, c.DistanceTo(tManager.makeCoordinate(3, 2)));
		assertEquals(6, c.DistanceTo(tManager.makeCoordinate(2, 7)));
		assertEquals(8, c.DistanceTo(tManager.makeCoordinate(1, 2)));
	}

	// #35
	@Test
	void triangleDistanceDxGreaterThanDyDifferentOrientationDownDownward() {
		Coordinate c = tManager.makeCoordinate(4, 4);
		assertEquals(5, c.DistanceTo(tManager.makeCoordinate(2, 5)));
		assertEquals(5, c.DistanceTo(tManager.makeCoordinate(2, 1)));
		assertEquals(7, c.DistanceTo(tManager.makeCoordinate(1, 2)));
	}

	// #36
	@Test
	void triangleDistanceDxGreaterThanDyDifferentOrientationDownUpward() {
		Coordinate c = tManager.makeCoordinate(1, 5);
		assertEquals(1, c.DistanceTo(tManager.makeCoordinate(2, 5)));
		assertEquals(7, c.DistanceTo(tManager.makeCoordinate(5, 4)));
	}

	// #37
	@Test
	void triangleDistanceDxGreaterThanDyDifferentOrientationUpDownward() {
		Coordinate c = tManager.makeCoordinate(5, 4);
		assertEquals(3, c.DistanceTo(tManager.makeCoordinate(3, 5)));
		assertEquals(5, c.DistanceTo(tManager.makeCoordinate(2, 2)));
		assertEquals(7, c.DistanceTo(tManager.makeCoordinate(1, 5)));
	}

	// #38
	@Test
	void triangleDistanceDxGreaterThanDyDifferentOrientationUpUpward() {
		Coordinate c = tManager.makeCoordinate(1, 4);
		assertEquals(3, c.DistanceTo(tManager.makeCoordinate(2, 6)));
		assertEquals(5, c.DistanceTo(tManager.makeCoordinate(3, 3)));
		assertEquals(6, c.DistanceTo(tManager.makeCoordinate(4, 5)));
	}

	// #39
	@Test //already worked as a result of earlier code being well written :)
	void triangleFiniteBoardInvalidMoves() {
		invalidMoves(
			tManager, 
			tManager.makeCoordinate(4, 5),
			new int[]{1,1,5,7}, 
			new int[]{4,6,11,5});
	}
	
	// #40
	@Test //already worked as a result of earlier code being well written :)
	void triangleFiniteBoardValidMoves() {
		validMoves(
			tManager, 
			new int[]{4,1,4,4,7}, 
			new int[]{5,5,3,8,8}, 
			tManager.makeCoordinate(1, 10), 
			tManager.makeCoordinate(1, 11));
	}

	// #41
	@Test
	void triangleInfiteBoardValidMoves() {
		validMoves(
			tManager2, 
			new int[]{1,0, 0, 2, 2}, 
			new int[]{1,0,-5,-6,-1}, 
			tManager2.makeCoordinate(1, 10), 
			tManager2.makeCoordinate(1, 11));
	}

	// #42
	@Test //already worked as a result of earlier code being well written :)
	void triangleInfiniteBoardInvalidMoves() {
		invalidMoves(
			tManager2, 
			tManager2.makeCoordinate(1, 1),
			new int[]{4,-2, 0}, 
			new int[]{0, 1,-4});
	}

	// #43
	@Test
	void triangleDistanceInvalidMoves() {
		invalidMoves(
			tManager3, 
			tManager3.makeCoordinate(1, 1), 
			new int[]{3,3,4,3,4}, 
			new int[]{2,1,1,3,3});
	}

	// #44
	@Test
	void triangleDistanceValidMoves() {
		validMoves(
			tManager3, 
			new int[]{1,3,4,2,2,1}, 
			new int[]{1,4,1,4,1,4},
			tManager3.makeCoordinate(1, 10),
			tManager3.makeCoordinate(1, 11));
	}

	// #45
	@Test
	void triangleInvalidJump() {
		invalidMoves(
			tManager4, 
			tManager4.makeCoordinate(1, 1), 
			new int[]{3,1,3}, 
			new int[]{2,6,3});
	}

	// #46
	@Test
	void triangleValidJump() {
		validMoves(
			tManager4, 
			new int[]{1,2,1,2,4,2,4},		
			new int[]{1,4,6,2,2,2,4},				
			tManager4.makeCoordinate(1, 10),
			tManager4.makeCoordinate(1, 11));
	}
}
