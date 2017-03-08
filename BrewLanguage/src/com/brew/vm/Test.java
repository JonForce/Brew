package com.brew.vm;

public class Test {
	
	private VirtualMachine vm = new VirtualMachine();
	
	public static void main(String[] args) {
		Test test = new Test();
		//test.vm.interpreter().enableDebugMessages();
		
		test.doubler();
	}
	
	private void basic() {
		vm.interpreter().interpret(new short[] {
				InstructionSet.PUSH,
				10,
				InstructionSet.PUSH,
				2,
				InstructionSet.SUBTRACT,
				InstructionSet.DEBUG_STACK
		});
	}
	
	private void controlFlow() {
		vm.interpreter().interpret(new short[] {
				InstructionSet.PUSH,
				-1,
				InstructionSet.PUSH,
				1,
				InstructionSet.ADD,
				InstructionSet.DEBUG_OUT,
				InstructionSet.GOTO,
				2
		});
	}
	
	private void conditional() {
		vm.interpreter().interpret(new short[] {
				InstructionSet.PUSH,
				1,
				InstructionSet.PUSH,
				1,
				InstructionSet.ADD,
				InstructionSet.DUPLICATE,
				InstructionSet.PUSH,
				10,
				InstructionSet.GREATER_THAN,
				InstructionSet.DEBUG_STACK,
				InstructionSet.IF,
				2,
		});
	}
	
	private void doubler() {
		vm.interpreter().interpret(new short[] {
				InstructionSet.PUSH, 1,
				InstructionSet.DUPLICATE,
				InstructionSet.ADD,
				InstructionSet.DEBUG_OUT,
				
				InstructionSet.DUPLICATE,
				InstructionSet.PUSH, 32,
				InstructionSet.GREATER_THAN,
				InstructionSet.IF, 2
		});
	}
}