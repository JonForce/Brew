package com.brew.compiler;

/**
 * This is a simple utility class that's sole purpose is to point to a variable in the stack.
 * Variables in the stack are stored in frames, which are arrays of bytes. To point to a variable
 * on the stack you must have the frame's ID and the variable's ID.
 * @author Jonathan Force
 */
public class StackPointer {
	
	private final byte frame, variableID;
	
	public StackPointer(byte frame, byte variableID) {
		this.frame = frame;
		this.variableID = variableID;
	}
	
	/** @return the frame's ID that the pointer points to in the stack. */
	public byte frame() {
		return frame;
	}
	
	/** @return the variable's ID that the pointer points to in the frame. */
	public byte variableID() {
		return variableID;
	}
	
}