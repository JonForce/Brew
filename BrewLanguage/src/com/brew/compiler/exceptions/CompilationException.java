package com.brew.compiler.exceptions;

public class CompilationException extends RuntimeException {
	
	public CompilationException(String message) {
		super("Compilation Exception : " + message);
	}
	
}
