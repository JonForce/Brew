package com.brew.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brew.compiler.Utilities;
import com.brew.vm.InstructionSet;

public class CompilerTest {
	
	@Test
	public void testCompiler() {
		// Test the ability of the compiler to translate a conditional correctly.
		short[] output = new Utilities().generateConditionalBytecode(
				new short[] {
						InstructionSet.PUSH, 5,
						InstructionSet.PUSH, 10,
						InstructionSet.ADD,
				},
				">",
				new short[] {
						InstructionSet.PUSH, 5,
						InstructionSet.PUSH, 7,
						InstructionSet.SUBTRACT,
				});
		short[] correctOutput = new short[] {
				InstructionSet.PUSH, 5,
				InstructionSet.PUSH, 7,
				InstructionSet.SUBTRACT,
				InstructionSet.PUSH, 5,
				InstructionSet.PUSH, 10,
				InstructionSet.ADD,
				InstructionSet.GREATER_THAN
		};
		for (int i = 0; i < output.length; i ++)
			if (output[i] != correctOutput[i])
				fail("Did not correctly translate a conditional properly. Problem at instruction : " + i);
		
		
		// ~~~~~~~~~~~~
		
		
		// Test the ability of the compiler to split a conditional expression into 3 pieces.
		String[] array = new Utilities().splitConditionalExpression("7*3 + 5/ 6 <= 51 * B - F");
		assertTrue(
				"Failed to split a conditional expression into 3 parts correctly.",
				array[0].equals("7*3+5/6") && array[1].equals("<=") && array[2].equals("51*B-F"));
		
		
		// ~~~~~~~~~~~~
		
		// Test the ability of the compiler to tokenize an expression.
		String[] out = new Utilities().tokenize("1 - -87 * (-5 + 1)");
		assertTrue(
				"Failed to tokenize advanced string.",
				out[0].equals("1") &&
				out[1].equals("-") &&
				out[2].equals("-87") &&
				out[3].equals("*") &&
				out[4].equals("(") &&
				out[5].equals("-5") &&
				out[6].equals("+") &&
				out[7].equals("1") &&
				out[8].equals(")"));
	}
	
}
