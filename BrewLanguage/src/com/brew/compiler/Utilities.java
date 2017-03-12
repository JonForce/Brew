package com.brew.compiler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;

import com.brew.vm.InstructionSet;

public class Utilities {
	
	/** Translate an expression tokenized and given in postfix notation to machine instructions. */
	public byte[] expressionToInstructions(String[] postfix, HashMap<String, StackPointer> variableNameToPointerMap) {
		ArrayList<Byte> list = new ArrayList<Byte>();
		
		for (String token : postfix) {
			if (isNumber(token)) {
				list.add(InstructionSet.PUSH);
				list.add(Byte.parseByte(token));
			} else if (token.length() == 1 && isSimpleOperator(token.charAt(0))) {
				list.add(InstructionSet.getOperatorByName(token));
			} else {
				StackPointer pointer = variableNameToPointerMap.get(token);
				list.add(InstructionSet.PULL_VAR);
				list.add(pointer.frame());
				list.add(pointer.variableID());
			}
		}
		
		byte[] out = new byte[list.size()];
		for (int i = 0; i < list.size(); i ++)
			out[i] = list.get(i);
		
		return out;
	}
	
	/** Translate a infix expression to postfix notation using Edsger Dijkstra's shunting-yard algorithm.
	 * @param infix The tokenized infix input.
	 * @return The postfix output.
	 */
	public String[] toPostfix(String[] infix) {
		
		Stack<Character> operatorStack = new Stack<Character>();
		ArrayDeque<String> queue = new ArrayDeque<String>();
		
		for (String token : infix) {
			
			if (isNumber(token) || Character.isLetter(token.charAt(0)))
				queue.add(token);
			else if (token.length() == 1 && token.charAt(0) == '(')
				operatorStack.push('(');
			else if (token.length() == 1 && token.charAt(0) == ')') {
				char c;
				dance: while (true) {
					if (operatorStack.isEmpty())
						throw new RuntimeException("Cannot convert tokenized infix to postfix; mismatched parenthesis.");
					else if ((c = operatorStack.pop()) == '(')
						break dance;
					queue.add(c + "");
				}
			} else if (token.length() == 1 && isSimpleOperator(token.charAt(0))) {
				char operator = token.charAt(0);
				while (!operatorStack.isEmpty() && precidenceOf(operator) <= precidenceOf(operatorStack.peek())) {
					queue.add("" + operatorStack.pop());
				}
				operatorStack.add(operator);
			} else
				throw new RuntimeException("Unrecognized token \"" + token + "\". Cannot convert the tokenized infix to postfix.");
			
		}
		
		while (!operatorStack.isEmpty()) {
			char c = operatorStack.pop();
			if (c == '(' || c == ')')
				throw new RuntimeException("Cannot convert tokenized infix to postfix; mismatched parenthesis.");
			else
				queue.add(c + "");
		}
		
		return queue.toArray(new String[0]);
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
	public byte[] composeConditional(byte[] left, String conditional, byte[] right) {
		// The + 1 accounts for the single conditional operator that must be appended.
		byte[] returnValue = new byte[left.length + right.length + 1];
		
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
	
	/** Take the infix input and break it up into its pieces. Each token is
	 * a operator or operand.
	 * @param source The input infix expression.
	 * @return The infix expression broken up into its tokens.
	 */
	public String[] tokenize(String source) {
		ArrayList<String> list = new ArrayList<String>();
		
		// Remove all whitespace.
		source = source.replaceAll("\\s+","");
		
		int i = 0;
		int numberOfTokens = 0;
		while (i < source.length()) {
			// This line determines whether or not the next token is potentially a negative sign.
			// A - could actually be a negative sign if it's the first thing in the expression OR the previous token is not a number.
			boolean isPossiblyANegativeSign = i == 0 || (!isNumber(list.get(numberOfTokens - 1)) && list.get(numberOfTokens-1).charAt(0) != ')' && !Character.isLetter(list.get(numberOfTokens-1).charAt(0)));
			
			String token = parseFirstTokenIn(source.substring(i), isPossiblyANegativeSign);
			list.add(token);
			numberOfTokens ++;
			i += token.length();
		}
		
		return list.toArray(new String[0]);
	}
	
	/** This is a utility method that copies the source array into the destination array
	 * starting at the specified location. */
	public void copyInto(byte[] source, byte[] destination, int location) {
		if (location + source.length >= destination.length)
			throw new RuntimeException("Not enough room to copy array.");
		
		for (int i = 0; i < source.length; i ++)
			if (destination[i + location] != 0)
				throw new RuntimeException("Problem copying array, it would override destination at index : " + i);
			else
				destination[i + location] = source[i];
	}
	
	/** @return the first token in the source. A token is either a operand or an operator. */
	private String parseFirstTokenIn(String source, boolean isPotentialNegativeSign) {
		if (Character.isLetter(source.charAt(0)))
			return parseFirstVariableIn(source);
		// If the first character in the source is parenthesis OR it's an operator and not a potential negative sign,
		else if (isParenthesis(source.charAt(0)) || (isSimpleOperator(source.charAt(0)) && !isPotentialNegativeSign))
			// Return the first character because it must be an operator.
			return source.substring(0, 1);
		// Else we have a number. But we need to find the length of the number so we can parse it out of the source.
		else {
			// i represents the size of the number. It will either start at 0 or 1 depending if the number starts with a negative sign or not.
			int i = (source.charAt(0) == '-')? 1 : 0;
			// While i is less than the length of our source and the current character is not an operator,
			while (i < source.length() && !isSimpleOperator(source.charAt(i))) {
				// Increase i
				i ++;
			}
			// Finally, return the number now that we know its length.
			return source.substring(0, i);
		}
	}
	
	/** @return the first alphabetic variable token in the specified source string. The
	 * token must start at the beginning of the string. */
	private String parseFirstVariableIn(String source) {
		String total = "";
		int i = 0;
		while (Character.isLetter(source.charAt(i))) {
			total += source.substring(i, i + 1);
			i ++;
		}
		return total;
	}
	
	/** @return true if the thing is a simple operator. */
	private boolean isSimpleOperator(char thing) {
		return
				thing == '+' ||
				thing == '-' ||
				thing == '*' ||
				thing == '/' ||
				thing == '(' ||
				thing == ')';
	}
	
	/** @return true of the thing is parenthesis. (Either ( or ) ). */
	private boolean isParenthesis(char thing) {
		return thing == '(' || thing == ')';
	}
	
	/** @return true if the token is a simple number. */
	private boolean isNumber(String token) {
		if (token.length() == 1 && isSimpleOperator(token.charAt(0)))
			return false;
		return token.matches("-?\\d+(\\.\\d+)?");
	}
	
	/** @return the numerical precedence of the operator. */
	private int precidenceOf(char op) {
		return
				(op == '+' || op == '-')? 2 :
				(op == '*' || op == '/')? 3 : -1;
	}
}