package com.brew.compiler;

import com.brew.vm.InstructionSet;
import com.brew.vm.VirtualMachine;

public class Test {
	
	public static void main(String[] args) {
		
		VirtualMachine vm = new VirtualMachine();
		vm.interpreter().enableDebugMessages();
		Utilities util = new Utilities();
		
//		String[] array = util.splitConditionalExpression("7*3 + 5/ 6 <= 51 * B - F");
//		for (String s : array)
//			System.out.println(s);
	}
	
}
