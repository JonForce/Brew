package com.brew.vm.exceptions;

public class StackUnderflowException extends RuntimeException {
	public StackUnderflowException(int i) {
		super("Stack Underflow Exception at instruction " + i);
	}
}
