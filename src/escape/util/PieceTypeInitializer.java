/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package escape.util;

import java.util.Arrays;
import escape.required.EscapePiece.*;

/**
 * A general initializer for a piece type. Since this is used
 * internally, we take a shortcut and make the instance variables
 * public rather than private since this class is not part of the
 * actual game implementation.
 * 
 * This file mirrors the <pieceTypes> structure in the
 * XML configuration files.
 * 
 * You do not have to use this, but are encouraged to do so. 
 * 
 * However, you do need to be able to load the appropriate named
 * data from the configuration file in order to create a game
 * correctly.
 *  
 * MODIFIABLE: YES
 * MOVEABLE: YES
 * REQUIRED: NO 
 */
public class PieceTypeInitializer
{
    private PieceName pieceName;
    private MovementPattern movementPattern;
    private PieceAttribute[] attributes;
    
    public PieceTypeInitializer() {}
    
    /**
     * @return the pieceName
     */
    public PieceName getPieceName()
    {
        return pieceName;
    }
    /**
     * @param pieceName the pieceName to set
     */
    public void setPieceName(PieceName pieceName)
    {
        this.pieceName = pieceName;
    }
    /**
     * @return the movementPattern
     */
    public MovementPattern getMovementPattern()
    {
        return movementPattern;
    }
    /**
     * @param movementPattern the movementPattern to set
     */
    public void setMovementPattern(MovementPattern movementPattern)
    {
        this.movementPattern = movementPattern;
    }
    /**
     * @return the attributes
     */
    public PieceAttribute[] getAttributes()
    {
        return attributes;
    }
    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(PieceAttribute ... attributes)
    {
        this.attributes = attributes;
    }

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "PieceTypeInitializer [pieceName=" + pieceName + ", movementPattern="
		    + movementPattern + ", attributes=" + Arrays.toString(attributes) + "]";
	}
    
    
}
