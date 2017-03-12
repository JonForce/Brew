package com.brew.vm;

import java.util.ArrayList;

public class Runtime {
	
	public final byte[] instructions;
	public final ArrayList<Byte[]> frames;
	
	public int
		i,
		totalExecutedInstructions;
	
	public Runtime(byte[] instructions) {
		this.i = 0;
		this.frames = new ArrayList<Byte[]>();
		this.totalExecutedInstructions = 0;
		this.instructions = instructions;
	}
	
	public void popFrame() {
		frames.remove(frames.size() - 1);
	}
	
	/** Push a stack frame of a specified size. Frames are used to store variables. */
	public void pushFrame(byte size) {
		System.out.println("Pushed frame");
		Byte[] frame = new Byte[size];
		frames.add(frame);
	}
	
	/** Pull a variable off the specified stack frame. */
	public byte pullVar(byte frameID, byte id) {
		// Beware bad input.
		if (frameID >= frames.size() || frameID < 0)
			throw new RuntimeException("FrameID " + frameID + " is not valid.");
		else if (id >= frames.get(frameID).length || id < 0)
			throw new RuntimeException("Variable id " + id + " is not valid in frame " + frameID);
		
		return frames.get(frameID)[id];
	}
	
	public void pushVar(byte frameID, byte id, byte value) {
		// Beware bad input.
		if (frameID >= frames.size() || frameID < 0)
			throw new RuntimeException("FrameID " + frameID + " is not valid.");
		else if (id >= frames.get(frameID).length || id < 0)
			throw new RuntimeException("Variable id " + id + " is not valid in frame " + frameID);
		
		frames.get(frameID)[id] = value;
	}
	
	/** @return true while there are still more instructions to execute. */
	public boolean notFinished() {
		return i < instructions.length && totalExecutedInstructions < 300;
	}
	
	/** @return the current instruction; the instruction currently being executed. */
	public byte currentInstruction() {
		return instructions[i];
	}
	
	/** @return the n'th parameter for the current instruction. */
	public byte parameter(int n) {
		return instructions[i + n];
	}
	
	/** @return the next parameter and advance the pointer. */
	public byte popParameter() {
		byte value = parameter(1);
		advance();
		return value;
	}
	
	/** Advance to the next instruction. */
	public void advance() {
		advanceBy(1);
	}
	
	/** Advance by n instructions. */
	public void advanceBy(int n) {
		i += n;
	}
	
	/** Manually set the instruction pointer to the specified new value. */
	public void setI(int newI) {
		this.i = newI;
	}
}