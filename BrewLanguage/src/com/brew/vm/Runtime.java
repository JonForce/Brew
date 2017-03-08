package com.brew.vm;

public class Runtime {
	
	public final short[] instructions;
	
	public int
		i,
		totalExecutedInstructions;
	
	public Runtime(short[] instructions) {
		this.i = 0;
		this.totalExecutedInstructions = 0;
		this.instructions = instructions;
	}
	
	/** @return true while there are still more instructions to execute. */
	public boolean notFinished() {
		return i < instructions.length && totalExecutedInstructions < 300;
	}
	
	/** @return the current instruction; the instruction currently being executed. */
	public short currentInstruction() {
		return instructions[i];
	}
	
	/** @return the n'th parameter for the current instruction. */
	public short parameter(int n) {
		return instructions[i + n];
	}
	
	/** @return the next parameter and advance the pointer. */
	public short popParameter() {
		short value = parameter(1);
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