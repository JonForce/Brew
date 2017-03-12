package com.brew.compiler;

import com.brew.vm.InstructionSet;
import com.brew.vm.VirtualMachine;

public class Test {
	
	public static void main(String[] args) {
		
		VirtualMachine vm = new VirtualMachine();
		//vm.interpreter().enableDebugMessages();
		Utilities util = new Utilities();
		
		String source = "99 *poop * (3 - -9) + 7 - 5 * 7";
		
		System.out.println("Source : " + source);
		
		System.out.println("\nTokenizing source : ");
		String[] tokens = util.tokenize(source);
		for (String s : tokens)
			System.out.println(s);
		
		System.out.println("\nConverting to RPN : ");
		String[] rpn = util.toPostfix(tokens);
		for (String s : rpn)
			System.out.println(s);
		
		System.out.println("\nPutting into virtual machine : ");
		vm.interpreter().interpret(util.expressionToInstructions(rpn));
		vm.interpreter().printStack();
	}
}
