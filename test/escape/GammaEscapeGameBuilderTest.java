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


public class GammaEscapeGameBuilderTest {
	
	private static EscapeGameManager manager, bManager;

	@BeforeEach
	void loadGame() throws Exception {
		manager = new EscapeGameBuilder("config/egc/gtest1.egc").makeGameManager();
		bManager = new EscapeGameBuilder("config/egc/blocks.egc").makeGameManager();
	}

	// #1
	@Test
	void cantCapturePieceWithoutRemove() {
		assertFalse(manager.move(manager.makeCoordinate(4, 4), manager.makeCoordinate(10, 12)));
	}

	// #2
	@Test
	void cantJumpOverBlock() {
		assertFalse(bManager.move(bManager.makeCoordinate(1, 1), bManager.makeCoordinate(3, 1)));
	}
}
