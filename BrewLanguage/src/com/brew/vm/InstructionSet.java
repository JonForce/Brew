package com.brew.vm;

public class InstructionSet {
	
	public static final short
		DUPLICATE = 0x00,
		PUSH = 0x01,
		ADD = 0x02,
		SUBTRACT = 0x03,
		DIVIDE = 0x04,
		MULTIPLY = 0x05,
		DEBUG_OUT = 0x06,
		DEBUG_STACK = 0x07,
		
		GOTO = 0x08,
		IF = 0x09,
		
		GREATER_THAN = 0x0A,
		GREATER_THAN_EQUAL = 0x0B,
		LESS_THAN = 0x0C,
		LESS_THAN_EQUAL = 0x0D,
		EQUAL_TO = 0x0E;
	
	/** @return false if the instruction is a arithmetic operator. */
	public static boolean isArithmeticOperator(short inst) {
		return inst >= ADD && inst <= MULTIPLY;
	}
	
	/** @return true if the instruction is a comparison operator. */
	public static boolean isComparisonOperator(short inst) {
		return inst >= GREATER_THAN && inst <= EQUAL_TO;
	}
	
	/** @return the bytecode instruction for the conditional operator given by name.
	 * @param name This is the name of the conditional operator. Valid inputs include >, <, >=, <=
	 */
	public static short getConditionalByName(String name) {
		if (name.equals(">"))
			return GREATER_THAN;
		else if (name.equals(">="))
			return GREATER_THAN_EQUAL;
		else if (name.equals("<"))
			return LESS_THAN;
		else if (name.equals("<="))
			return LESS_THAN_EQUAL;
		else if (name.equals("=="))
			return EQUAL_TO;
		else throw new RuntimeException("No such conditional operator : " + name);
	}
	
	/** @return the bytecode instruction for the operator given by the name.
	 * The world will end if you give this method operators with whitespace.
	 * @param name the name of the operator. Ex : +, -, *
	 */
	public static short getOperatorByName(String name) {
		if (name.equals("+"))
			return ADD;
		else if (name.equals("-"))
			return SUBTRACT;
		else if (name.equals("*"))
			return MULTIPLY;
		else if (name.equals("/"))
			return DIVIDE;
		else
			throw new RuntimeException("No such arithmetic operator : " + name);
	}
}
