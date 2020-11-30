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
		assertFalse(manager2.move(
			manager2.makeCoordinate(1, 1),
			manager2.makeCoordinate(7, 1)
		));
		assertFalse(manager2.move(
			manager2.makeCoordinate(1, 1),
			manager2.makeCoordinate(1, 8)
		));
		assertFalse(manager2.move(
			manager2.makeCoordinate(1, 1),
			manager2.makeCoordinate(9, 10)
		));
	}

	// #2
	@Test
	void limitedByDistanceLinear() {
		assertFalse(manager2.move(
			manager2.makeCoordinate(1, 1),
			manager2.makeCoordinate(7, 1)
		));
		assertFalse(manager2.move(
			manager2.makeCoordinate(1, 1),
			manager2.makeCoordinate(1, 8)
		));
		assertFalse(manager2.move(
			manager2.makeCoordinate(1, 1),
			manager2.makeCoordinate(9, 10)
		));
	}
}
