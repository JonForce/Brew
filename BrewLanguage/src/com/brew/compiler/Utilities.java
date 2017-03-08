package com.brew.compiler;

import com.brew.vm.InstructionSet;

public class Utilities {
	
	/** Translate a infix expression to postfix notation using Edsger Dijkstra's shunting-yard algorithm.
	 * @param infix The infix input.
	 * @return The postfix output.
	 */
	public String[] toPostfix(String infix) {
		// Remove all whitespace.
		infix = infix.replaceAll("\\s+","");
		
		return null;
	}
	
	/** @return three Strings. The first will be the left hand side of the expression.
	 * The second will be the conditional operator. The third will be the right hand
	 * side of the expression.
	 * All whitespace is removed by this method if there is any. */
	public String[] splitConditionalExpression(String expression) {
		String[] out = new String[3];
		
		// Remove all whitespace.
		expression = expression.replaceAll("\\s+","");
		
		// We need to find the index of the operator in the expression if we can possibly hope to split it.
		int indexOfOperator = -1;
		// Make an array of all the operators.
		String[] operators = new String[] { ">", "<", ">=",  "<=", "==" };
		// This is the operator that was found in the expression.
		String operator = null;
		
		for (String op : operators)
			// If the expression contains this operator.
			if (expression.indexOf(op) != -1) {
				// If we already have found an operator, then this is the second operator we found. That is cause for an error.
				if (indexOfOperator != -1 && indexOfOperator != expression.indexOf(op)) throw new RuntimeException("There is more than one conditional operator in this expression.");
				// Else we found the correct index of the operator in the expression, so set it.
				else {
					indexOfOperator = expression.indexOf(op);
					operator = op;
				}
			}
		
		// Set the 0 element to be the left hand side of the expression.
		out[0] = expression.substring(0, indexOfOperator);
		// Set the 1 element to be the operator of the expression.
		out[1] = operator;
		// Set the 2 element to be the right hand side of the expression.
		out[2] = expression.substring(indexOfOperator + operator.length());
		
		return out;
	}
	
	/** Generates the bytecode for a simple conditional.
	 * @param left The instructions required to push the left hand operator to the stack.
	 * @param conditional The conditional by name. Should look something like this : >, <, >=, <=, ==
	 * @param right The instructions required to push the right hand operator to the stack.
	 */
	public short[] generateConditionalBytecode(short[] left, String conditional, short[] right) {
		// The + 1 accounts for the single conditional operator that must be appended.
		short[] returnValue = new short[left.length + right.length + 1];
		
		// Copy over the right hand instructions.
		copyInto(right, returnValue, 0);
		
		// Copy over the left hand instructions.
		copyInto(left, returnValue, right.length);
		
		// This represents the starting place in the array where we can put the actual conditional instructions.
		int p = left.length + right.length;
		
		// Add the actual conditional operator.
		returnValue[p] = InstructionSet.getConditionalByName(conditional);
		
		return returnValue;
	}
	
	/** This is a utility method that copies the source array into the destination array
	 * starting at the specified location. */
	private void copyInto(short[] source, short[] destination, int location) {
		if (location + source.length >= destination.length)
			throw new RuntimeException("Not enough room to copy array.");
		
		for (int i = 0; i < source.length; i ++)
			if (destination[i + location] != 0)
				throw new RuntimeException("Problem copying array, it would override destination at index : " + i);
			else
				destination[i + location] = source[i];
	}
}