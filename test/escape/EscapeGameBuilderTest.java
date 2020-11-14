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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

	@BeforeAll
	static void loadGame() throws Exception {
		EscapeGameBuilder egb = new EscapeGameBuilder("config/egc/test1.egc");
		manager = egb.makeGameManager();
	}

	@Test
	void canCreateGame() {
		assertNotNull(manager);
	}

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
	void distanceToVertical() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(1, 4));
		assertEquals(3, dist);
	}

	@Test
	void distanceToDiagonal() {
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(5, 5));
		assertEquals(4, dist);
	}

	@Test
	void distanceToNonStraight() { //This worked without me changing anything and I dont understand why
		int dist = manager.makeCoordinate(1, 1).DistanceTo(manager.makeCoordinate(3, 5));
		assertEquals(4, dist);
	}

	/*
	@Test
	void horseAt6_2() {
		EscapePiece piece = manager.getPieceAt(manager.makeCoordinate(6, 2));
		assertNotNull(piece);
		assertEquals(PieceName.HORSE, piece.getName());
		assertEquals(Player.PLAYER1, piece.getPlayer());
	}
	*/

}
