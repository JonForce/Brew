package com.brew.vm;

import java.util.Stack;

import com.brew.vm.exceptions.StackUnderflowException;

/** This class represents the compilation of the components that compose a virtual machine.
 * Its primary purpose is to interpret instructions and modify its own state.
 * @author Jonathan Force
 */
public class VirtualMachine {
	
	private Stack<Byte> stack;
	private Interpreter interpreter;
	
	public VirtualMachine() {
		stack = new Stack<Byte>();
		interpreter = new Interpreter(stack);
	}
	
	/** Return the Virtual Machine's interpreter; the system which takes in and executes instructions. */
	public Interpreter interpreter() {
		return interpreter;
	}
	
	/** Print every element is the VM's Stack. */
	public void printStack() {
		for (int i = 0; i < stack.size(); i ++)
			System.out.println(i+": " + stack.get(i));
	}
	
	/** @return the Virtual Machine's Stack. This is where the machine
	 * performs it's operations. */
	public Stack<Byte> stack() {
		return stack;
	}
}