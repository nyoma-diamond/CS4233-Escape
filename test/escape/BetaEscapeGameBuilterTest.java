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

	private static EscapeGameManager manager2;
	private static EscapeGameManager mpManager;

	@BeforeEach
	void loadGame() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test2.egc");
		manager2 = egb.makeGameManager();
		
		egb = new EscapeGameBuilder("config/egc/test3.egc");
		mpManager = egb.makeGameManager();
	}

	// #1
	@Test
	void limitedByDistanceOmni() {
		Coordinate c = manager2.makeCoordinate(1, 1);
		assertFalse(manager2.move(c, manager2.makeCoordinate(7, 1)));
		assertFalse(manager2.move(c, manager2.makeCoordinate(1, 8)));
		assertFalse(manager2.move(c, manager2.makeCoordinate(9, 10)));
	}

	// #2
	@Test
	void validLinearMove() { //this will work by default as a result of Alpha assumptions, but is needed to make sure future tests dont break behavior
		Coordinate c1 = manager2.makeCoordinate(1, 9);
		Coordinate c2 = manager2.makeCoordinate(1, 10);

		assertTrue(manager2.move(
			manager2.makeCoordinate(1, 3), 
			manager2.makeCoordinate(4, 3)
		)); //+x
		manager2.move(c1, c2); //just to make the turn change back
		
		assertTrue(manager2.move(
			manager2.makeCoordinate(4, 3), 
			manager2.makeCoordinate(4, 8)
		)); //+y
		manager2.move(c2, c1); //just to make the turn change back

		assertTrue(manager2.move(
			manager2.makeCoordinate(4, 8), 
			manager2.makeCoordinate(2, 6)
		)); //-x,-y diag
		manager2.move(c1, c2); //just to make the turn change back
		
		assertTrue(manager2.move(
			manager2.makeCoordinate(2, 6), 
			manager2.makeCoordinate(4, 4)
		)); //x,-y diag
		manager2.move(c2, c1); //just to make the turn change back

		assertTrue(manager2.move(
			manager2.makeCoordinate(4, 4), 
			manager2.makeCoordinate(6, 6)
		)); //x,y diag
		manager2.move(c1, c2); //just to make the turn change back
		
		assertTrue(manager2.move(
			manager2.makeCoordinate(6, 6), 
			manager2.makeCoordinate(4, 8)
		)); //-x,y diag
		manager2.move(c2, c1); //just to make the turn change back

		assertTrue(manager2.move(
			manager2.makeCoordinate(4, 8), 
			manager2.makeCoordinate(2, 8)
		)); //-x
		manager2.move(c1, c2); //just to make the turn change back
		
		assertTrue(manager2.move(
			manager2.makeCoordinate(2, 8), 
			manager2.makeCoordinate(2, 4)
		)); //-y
	}

	// #3
	@Test
	void invalidLinearMove() {
		Coordinate c = manager2.makeCoordinate(6, 8);		
		assertTrue(manager2.move(manager2.makeCoordinate(1, 3), c)); //move piece to better spot for testing (these have asserts to ensure these went through and we're not getting false positives)
		assertTrue(manager2.move(manager2.makeCoordinate(1, 9), manager2.makeCoordinate(1, 10))); //change turn back

		assertFalse(manager2.move(c, manager2.makeCoordinate(8, 9))); //+2,+1
		assertFalse(manager2.move(c, manager2.makeCoordinate(9, 6))); //+3,-2
		assertFalse(manager2.move(c, manager2.makeCoordinate(2, 9))); //-4,+1
		assertFalse(manager2.move(c, manager2.makeCoordinate(5, 5))); //-1,-3
		assertFalse(manager2.move(c, manager2.makeCoordinate(16, 8))); // valid direction, too far
	}

	// #4
	@Test
	void validOrthogonalMove() {
		Coordinate c1 = manager2.makeCoordinate(1, 9);
		Coordinate c2 = manager2.makeCoordinate(1, 10);

		assertTrue(manager2.move(
			manager2.makeCoordinate(1, 7), 
			manager2.makeCoordinate(2, 11)
		)); 
		manager2.move(c1, c2); //just to make the turn change back
		
		assertTrue(manager2.move(
			manager2.makeCoordinate(2, 11), 
			manager2.makeCoordinate(7, 11)
		)); 
		manager2.move(c2, c1); //just to make the turn change back

		assertTrue(manager2.move(
			manager2.makeCoordinate(7, 11), 
			manager2.makeCoordinate(5, 8)
		));
	}

	// #5
	@Test
	void invalidOrthogonalMove() {
		Coordinate c = manager2.makeCoordinate(5, 8);		
		assertTrue(manager2.move(manager2.makeCoordinate(1, 7), c)); //move piece to better spot for testing (these have asserts to ensure these went through and we're not getting false positives)
		assertTrue(manager2.move(manager2.makeCoordinate(1, 9), manager2.makeCoordinate(1, 10))); //change turn back

		assertFalse(manager2.move(c, manager2.makeCoordinate(6, 3))); 
		assertFalse(manager2.move(c, manager2.makeCoordinate(1, 6))); 
		assertFalse(manager2.move(c, manager2.makeCoordinate(8, 11))); 
		assertFalse(manager2.move(c, manager2.makeCoordinate(5, 14))); 
		assertFalse(manager2.move(c, manager2.makeCoordinate(11, 8)));
	}

	// #6
	@Test
	void validDiagonalMove() {
		Coordinate c1 = manager2.makeCoordinate(1, 9);
		Coordinate c2 = manager2.makeCoordinate(1, 10);

		assertTrue(manager2.move(
			manager2.makeCoordinate(1, 5), 
			manager2.makeCoordinate(6, 10)
		)); 
		manager2.move(c1, c2); //just to make the turn change back
		
		assertTrue(manager2.move(
			manager2.makeCoordinate(6, 10), 
			manager2.makeCoordinate(7, 5)
		)); 
		manager2.move(c2, c1); //just to make the turn change back

		assertTrue(manager2.move(
			manager2.makeCoordinate(7, 5), 
			manager2.makeCoordinate(2, 2)
		));
	}

	// #7
	@Test
	void invalidDiagonalMove() {
		Coordinate c = manager2.makeCoordinate(1, 5);		
		
		assertFalse(manager2.move(c, manager2.makeCoordinate(1, 6))); 
		assertFalse(manager2.move(c, manager2.makeCoordinate(5, 8))); 
		assertFalse(manager2.move(c, manager2.makeCoordinate(1, 2))); 
		assertFalse(manager2.move(c, manager2.makeCoordinate(7, 5))); 
		assertFalse(manager2.move(c, manager2.makeCoordinate(5, 4)));
	}
}
