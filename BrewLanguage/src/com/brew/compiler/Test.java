package com.brew.compiler;

import com.brew.vm.VirtualMachine;

public class Test {
	
	public static void main(String[] args) {
		
		VirtualMachine vm = new VirtualMachine();
		//vm.interpreter().enableDebugMessages();
		Compiler compiler = new Compiler();
		
		
		// TODO : This compiles? "byte x ="
		// TODO : Add support for nested blocks
		
		byte[] code = compiler.compile(
						"byte x = 0",
						"if (x == 0) {",
						"	byte t = 50",
						"	x = t",
						"}",
						"x = x + 1",
						"DEBUG");
		vm.interpreter().interpret(code);
	}
}
