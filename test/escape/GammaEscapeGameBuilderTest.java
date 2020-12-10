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

import escape.required.Coordinate;

import static escape.BetaEscapeGameBuilderTest.*;

public class GammaEscapeGameBuilderTest {
	
	private static EscapeGameManager manager, bManager, vManager, uManager;

	@BeforeEach
	void loadGame() throws Exception {
		manager = new EscapeGameBuilder("config/egc/gtest1.egc").makeGameManager();
		bManager = new EscapeGameBuilder("config/egc/blocks.egc").makeGameManager();
		vManager = new EscapeGameBuilder("config/egc/values.egc").makeGameManager();
		uManager = new EscapeGameBuilder("config/egc/unblock.egc").makeGameManager();
	}

	// #1
	@Test
	void cantCapturePieceWithoutRemove() {
		assertFalse(manager.move(manager.makeCoordinate(4, 4), manager.makeCoordinate(10, 12)));
	}

	// #2
	@Test
	void cantJumpOverBlock() {
		invalidMoves(
			bManager, 
			bManager.makeCoordinate(3, 3),
			new int[]{5,5,5,3,3,1,1,1}, 
			new int[]{1,3,5,1,5,1,3,5});
	}


	// #3
	@Test
	void endAfterTurnLimit() {
		Coordinate c1 = manager.makeCoordinate(4, 4);
		Coordinate c2 = manager.makeCoordinate(4, 5);
		Coordinate c3 = manager.makeCoordinate(7, 6);
		Coordinate c4 = manager.makeCoordinate(7, 7);

		assertTrue(manager.move(c1,c2));
		assertTrue(manager.move(c3,c4));
		assertTrue(manager.move(c2,c1));
		assertTrue(manager.move(c4,c3)); //LAST TURN, GAME SHOULD END AND ALL ADDITIONAL MOVES WILL FAIL
		assertFalse(manager.move(c1,c2));
		assertFalse(manager.move(c3,c4));
	}


	// #4
	@Test
	void noTurnLimit() {
		assertTrue(vManager.move(vManager.makeCoordinate(1, 1), vManager.makeCoordinate(3, 4)));
	}


	// #5
	@Test
	void winWithDefaultPoints() {
		assertTrue(vManager.move(vManager.makeCoordinate(1, 1), vManager.makeCoordinate(5, 5)));
		assertTrue(vManager.move(vManager.makeCoordinate(1, 2), vManager.makeCoordinate(5, 5)));
		assertTrue(vManager.move(vManager.makeCoordinate(1, 3), vManager.makeCoordinate(5, 5)));
		assertFalse(vManager.move(vManager.makeCoordinate(1, 4), vManager.makeCoordinate(5, 5)));
		assertFalse(vManager.move(vManager.makeCoordinate(1, 4), vManager.makeCoordinate(2, 4)));
	}

	// #5
	@Test
	void winWithSetPoints() {
		assertTrue(vManager.move(vManager.makeCoordinate(1, 5), vManager.makeCoordinate(5, 5)));
		assertFalse(vManager.move(vManager.makeCoordinate(1, 2), vManager.makeCoordinate(5, 5)));
		assertFalse(vManager.move(vManager.makeCoordinate(1, 4), vManager.makeCoordinate(5, 5)));
		assertFalse(vManager.move(vManager.makeCoordinate(1, 4), vManager.makeCoordinate(1, 5)));
	}

	// #5
	@Test 
	void cantJumpOverExit() {
		assertFalse(uManager.move(uManager.makeCoordinate(7, 3), uManager.makeCoordinate(5, 3)));
		assertTrue(uManager.move(uManager.makeCoordinate(7, 3), uManager.makeCoordinate(6, 3)));
	}

	// #6
	@Test
	void unblockWorks() {
		validMoves(
			uManager, 
			new int[]{3,5,6,2,3,5}, 
			new int[]{3,3,5,5,1,3}, 
			uManager.makeCoordinate(15, 15), 
			uManager.makeCoordinate(15, 16));
	}
}
