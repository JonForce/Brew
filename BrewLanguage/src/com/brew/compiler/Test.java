package com.brew.compiler;

import com.brew.vm.InstructionSet;
import com.brew.vm.VirtualMachine;

public class Test {
	
	public static void main(String[] args) {
		
		VirtualMachine vm = new VirtualMachine();
		//vm.interpreter().enableDebugMessages();
		Utilities util = new Utilities();
		
		String source = "99 *2 * (3 - -9) + 7 - 5 * 7";
		
		System.out.println("Source : " + source);
		
		System.out.println("\nTokenizing source : ");
		for (String s : util.tokenize(source))
			System.out.println(s);
		
		System.out.println("\nConverting to RPN : ");
		for (String s : util.toPostfix(util.tokenize(source)))
			System.out.println(s);
		
		System.out.println("\nPutting into virtual machine : ");
		vm.interpreter().interpret(util.expressionToInstructions(util.toPostfix(util.tokenize(source))));
		vm.interpreter().printStack();
	}
}
