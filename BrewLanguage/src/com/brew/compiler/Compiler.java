package com.brew.compiler;

import java.util.HashMap;

import com.brew.compiler.exceptions.CompilationException;
import com.brew.vm.InstructionSet;

public class Compiler {
	
	private final HashMap<String, StackPointer> variableToPointerMap;
	private final Utilities util;
	private int pointerID, frameID;
	
	public Compiler() {
		this.variableToPointerMap = new HashMap<String, StackPointer>();
		this.util = new Utilities();
		pointerID = frameID = 0;
	}
	
	/** This command will compile and compose a simple assignment statement into its bytecode form.
	 * A valid assignment statement is of the following form :
	 * <b>T name = ex</b>
	 * Where :
	 * "T" is a valid type identifier.
	 * "name" is a valid variable name identifier.
	 * "ex" is a valid expression.
	 * @param source The assignment statement to compile.
	 * @return The compiled bytecode for the assignment statement.
	 */
	public byte[] compileAssignmentStatement(String source) {
		if (!source.contains("="))
			throw new CompilationException("Valid assignment statements contain the = operator.");
		
		// Split the assignment into its left hand side and right hand side.
		String[] splitByAssignmentOp = source.split("=");
		
		if (splitByAssignmentOp.length != 2)
			throw new CompilationException("Valid assignment statements contain exactly one = operator. Yours contains " + splitByAssignmentOp.length);
		
		// Left and right hand sides.
		String lhs = splitByAssignmentOp[0];
		String rhs = splitByAssignmentOp[1];
		
		// Compile the right hand expression.
		byte[] compiledRHS = compileExpression(rhs);
		// Tokenize the left hand side of the expression.
		String[] lhsTokens = lhs.split(" ");
		
		StackPointer p;
		
		if (lhsTokens.length == 1) {
			// This means that we should be assigning to the value of a variable that already exists.
			if (!variableToPointerMap.containsKey(lhsTokens[0]))
				throw new CompilationException("Unknown variable \"" + lhsTokens[0] + "\".");
			p = variableToPointerMap.get(lhsTokens[0]);
		} else if (lhsTokens.length == 2) {
			// This means that we are creating a new variable.
			if (!lhsTokens[0].equals("byte"))
				throw new CompilationException("Invalid data type \"" + lhsTokens[0] + "\".");
			else if (variableToPointerMap.containsKey(lhsTokens[1]))
				throw new CompilationException("Variable \"" + lhsTokens[1] + "\" already exists.");
			variableToPointerMap.put(lhsTokens[1], generatePointer());
			p = variableToPointerMap.get(lhsTokens[1]);
		} else
			throw new CompilationException(
					"Problem on the left hand side of the assignment statement. There are too many tokens (Or too few)."
					+ " The left hand side of an assignment statement should only contain a data type and the variable name.");
		
		// Define the final size of the output. This accounts for the 3 bytes needed to push a variable.
		int length = compiledRHS.length + 3;
		byte[] output = new byte[length]; 
		
		util.copyInto(compiledRHS, output, 0);
		output[length - 3] = InstructionSet.PUSH_VAR;
		output[length - 2] = p.frame();
		output[length - 1] = p.variableID();
		
		return output;
	}
	
	/** Compose the instructions into a single array of instructions. */
	public byte[] compose(byte[] ... instructions) {
		int length = 0;
		for (byte[] inst : instructions)
			length += inst.length;
		
		int i = 0;
		byte[] store = new byte[length];
		for (byte[] inst : instructions) {
			util.copyInto(inst, store, i);
			i += inst.length;
		}
		return store;
	}
	
	/** This method will take an expression in Brew and compile it to byte code.
	 * Expressions do NOT have the assignment operator. Expressions are simple, contain only variable names
	 * and arithmetic operators.
	 * @param source The source code to compile.
	 * @return The compiled byte code to be put into the Brew Virtual Machine. */
	public byte[] compileExpression(String source) {
		String[] tokens = util.tokenize(source, true);
		String[] postfix = util.toPostfix(tokens);
		byte[] compiledCode = util.expressionToInstructions(postfix, variableToPointerMap);
		return compiledCode;
	}
	
	/** This method resets the Compiler to its factory settings. It will forget any variables that were
	 * created. */
	public void reset() {
		this.variableToPointerMap.clear();
		pointerID = frameID = 0;
	}
	
	/** Generate a new and unique pointer. */
	private StackPointer generatePointer() {
		return new StackPointer((byte)frameID, (byte)pointerID++);
	}
	
}