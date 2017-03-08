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
}
