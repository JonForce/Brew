package com.brew.vm;

import java.util.Stack;

import com.brew.vm.exceptions.StackUnderflowException;

/**
 * This class is the system that interprets instructions and operates on the VM's Stack.
 * @author Jonathan Force
 */
public class Interpreter {
	
	private final Stack<Byte> stack;
	private boolean loud = false;
	private Runtime runtime = null;
	
	public Interpreter(Stack<Byte> stack) {
		this.stack = stack;
	}
	
	/** This method takes in a program in the form of an array of instructions. Each instruction
	 * will be executed in order starting from instructions[0].
	 * @param instructions The instruction array to process.
	 */
	public void interpret(byte[] instructions) {
		runtime = new Runtime(instructions);
		
		while (runtime.notFinished()) {
			byte instruction = runtime.currentInstruction();
			//if (loud)
			//	System.out.println("Executing instruction " + instruction);
			
			try {
				
				// These conditionals map the instruction to the corresponding method in the interpreter.
				if (InstructionSet.isArithmeticOperator(instruction))
					doArithmeticOperator(instruction);
				else if (instruction == InstructionSet.DUPLICATE)
					doDuplicate();
				else if (instruction == InstructionSet.PUSH)
					doPush(runtime.popParameter());
				else if (instruction == InstructionSet.DEBUG_OUT)
					System.out.println("[DEBUG] " + stack.peek());
				else if (instruction == InstructionSet.DEBUG_STACK)
					printStack();
				else if (instruction == InstructionSet.GOTO)
					doGoto(runtime.parameter(1));
				else if (instruction == InstructionSet.IF)
					doIf();
				else if (InstructionSet.isComparisonOperator(instruction))
					doComparison(instruction);
				else if (instruction == InstructionSet.POP_FRAME)
					doPopFrame();
				else if (instruction == InstructionSet.PUSH_FRAME)
					doPushFrame(runtime.popParameter());
				else if (instruction == InstructionSet.PULL_VAR)
					doPullVar(runtime.popParameter(), runtime.popParameter());
				else if (instruction == InstructionSet.PUSH_VAR)
					doPushVar(runtime.popParameter(), runtime.popParameter());
				else
					throw new RuntimeException("No such instruction : " + instruction);
				
			} catch (StackUnderflowException e) {
				e.printStackTrace();
				printStack();
				System.exit(0);
			}
			
			// Advance to the next instruction.
			runtime.advance();
			runtime.totalExecutedInstructions ++;
		}
	}
	
	/** Print every element on the Stack. */
	public void printStack() {
		System.out.println("[DEBUG STACK]");
		for (int i = 0; i < stack.size(); i ++)
			System.out.println(i+": " + stack.get(i));
	}
	
	/** Enabling debug messages will make the Virtual Machine print out a message every time
	 * an instruction is interpreted. */
	public void enableDebugMessages() {
		loud = true;
	}
	
	/** This will disable the messages being printed after every instruction is
	 * interpreted. */
	public void disableDebugMessages() {
		loud = false;
	}
	
	/** If the condition is equal to 1, goto the parameter. If the condition
	 * is equal to 0, do nothing. If the condition is neither, throw an exception. */
	private void doIf() {
		testForUnderflow(1);
		short condition = stack.pop();
		
		if (loud)
			System.out.println("If " + condition);
		if (condition == 0) {
			// Condition is false.
			doGoto(runtime.i + 2 + runtime.popParameter());
		} else if (condition == 1) {
			// Condition is true.
			doGoto(runtime.i + 2);
		} else {
			// This isn't C++, non 1 or 0 values are illegal.
			throw new RuntimeException("Invalid truth value : " + condition);
		}
	}
	
	private void doGoto(int location) {
		if (loud)
			System.out.println("Going to " + location);
		runtime.i = location - 1;
	}
	
	private void doPush(byte value) {
		if (loud)
			System.out.println("Pushing " + value);
		stack.push(value);
	}
	
	private void doArithmeticOperator(byte instruction) {
		if (loud)
			System.out.println("Doing simple arithmetic operation");
		
		testForUnderflow(2);
		
		byte
			b = stack.pop(),
			a = stack.pop();
		
		if (instruction == InstructionSet.ADD)
			stack.push((byte) (a + b));
		else if (instruction == InstructionSet.SUBTRACT)
			stack.push((byte) (a - b));
		else if (instruction == InstructionSet.DIVIDE)
			stack.push((byte) (a / b));
		else if (instruction == InstructionSet.MULTIPLY)
			stack.push((byte) (a * b));
		else
			throw new RuntimeException("Not arithmetic operator.");
	}
	
	private void doDuplicate() {
		if (loud)
			System.out.println("Duplicating");
		
		testForUnderflow(1);
		byte item = stack.peek();
		stack.push(item);
	}
	
	private void doPopFrame() {
		if (loud)
			System.out.println("Popping frame.");
		runtime.popFrame();
	}
	
	private void doPushFrame(byte size) {
		if (loud)
			System.out.println("Pushing frame of size : " + size);
		runtime.pushFrame(size);
	}
	
	private void doPushVar(byte frameID, byte varID) {
		if (loud)
			System.out.println("Pushing variable in frame " + frameID + " at var " + varID);
		this.testForUnderflow(1);
		byte value = stack.pop();
		runtime.pushVar(frameID, varID, value);
	}
	
	private void doPullVar(byte frameID, byte varID) {
		if (loud)
			System.out.println("Pulling var " + frameID + " at var " + varID);
		byte value = runtime.pullVar(frameID, varID);
		stack.push(value);
	}
	
	private void doComparison(byte comparison) {
		testForUnderflow(2);
		
		byte
			a = stack.pop(),
			b = stack.pop();
		boolean result;
		
		if (loud)
			System.out.println("Doing comparison " + a + " " + b);
		
		// Map the comparison operator.
		if (comparison == InstructionSet.GREATER_THAN)
			result = a > b;
		else if (comparison == InstructionSet.GREATER_THAN_EQUAL)
			result = a >= b;
		else if (comparison == InstructionSet.LESS_THAN)
			result = a < b;
		else if (comparison == InstructionSet.LESS_THAN_EQUAL)
			result = a <= b;
		else if (comparison == InstructionSet.EQUAL_TO)
			result = a == b;
		else
			throw new RuntimeException("Unkown comparison operator.");
		
		if (result)
			stack.push((byte) 1);
		else
			stack.push((byte) 0);
	}
	
	private void testForUnderflow(int guarenteeSpace) {
		if (stack.size() < guarenteeSpace)
			throw new StackUnderflowException(runtime.i);
	}
}