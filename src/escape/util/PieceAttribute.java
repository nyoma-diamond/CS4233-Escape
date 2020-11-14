/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Copyright ©2016-2020 Gary F. Pollice
 *******************************************************************************/
package escape.util;

import escape.required.EscapePiece.PieceAttributeID;

/**
 * A JavaBean that represents an attribute for piece. This file
 * is provided as an example that can be used to initialize instances of a GameManager
 * via the EscapeBuilder. You do not have to use this, but are encouraged to do so.
 *
 * However, you do need to be able to load the appropriate named data from the 
 * configuration file in order to create a game correctly.
 * 
 * MODIFIABLE: YES
 * MOVEABLE: YES
 * REQUIRED: NO
 */
public class PieceAttribute {
    PieceAttributeID id;
    int value;
    
    public PieceAttribute() {}
    
    public PieceAttributeID getId() { return id; }
    public void setId(PieceAttributeID id) { this.id = id; }
    public int getValue() { return value; }
    public void setValue(int intValue) { this.value = intValue; }

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "PieceAttribute [id=" + id + ", value=" + value + "]";
	}
}
