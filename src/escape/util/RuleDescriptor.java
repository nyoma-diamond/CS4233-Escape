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
import escape.required.Rule.RuleID;

/**
 * A JavaBean that represents an attribute for rule. This file
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
public class RuleDescriptor {
	public RuleID ruleId;
	public int ruleValue;

	public RuleDescriptor() {
		// needed for JAXB unmarshalling
	}

	/**
	 * Description
	 * @param ruleId
	 * @param ruleValue
	 */
	public RuleDescriptor(RuleID ruleId, int ruleValue) {
		this.ruleId = ruleId;
		this.ruleValue = ruleValue;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleDescriptor [ruleId=" + ruleId + ", ruleValue=" + ruleValue + "]";
	}
}
