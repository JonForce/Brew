package com.brew.compiler;

import com.brew.vm.InstructionSet;
import com.brew.vm.VirtualMachine;

public class Test {
	
	public static void main(String[] args) {
		
		VirtualMachine vm = new VirtualMachine();
		//vm.interpreter().enableDebugMessages();
		Compiler compiler = new Compiler();
		
		
		// TODO : This compiles? "byte x ="
		
//		vm.interpreter().interpret(
//				compiler.compose(
//						new byte[] { InstructionSet.PUSH_FRAME, 2, },
//						compiler.compileAssignmentStatement("byte x = 2"),
//						compiler.compileAssignmentStatement("byte x = 2"),
//						compiler.compileAssignmentStatement("byte poop = 1"),
//						compiler.compileAssignmentStatement("poop = x*x"),
//						new byte[] { InstructionSet.DEBUG_STACK }));
		
		
		compiler.compileIfStatement("if (x > 3)", 16);
	}
}
