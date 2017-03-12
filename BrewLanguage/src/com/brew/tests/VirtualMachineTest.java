package com.brew.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brew.vm.InstructionSet;
import com.brew.vm.VirtualMachine;

public class VirtualMachineTest {

	@Test
	public void test() {
		VirtualMachine vm = new VirtualMachine();
		
		// This is the basic subtraction test.
		vm.interpreter().interpret(new byte[] {
				InstructionSet.PUSH, -10,
				InstructionSet.PUSH, 2,
				InstructionSet.SUBTRACT
		});
		assertTrue("Failed basic subtraction test.", vm.stack().pop() == -12);
		
		
		// This is a somewhat advanced test that tests many facets of the machine at once.
		// It should double the bottom number on the stack over and over again until it reaches 32.
		vm.interpreter().interpret(new byte[] {
				InstructionSet.PUSH, 1,
				InstructionSet.DUPLICATE,
				InstructionSet.ADD,
				
				InstructionSet.DUPLICATE,
				InstructionSet.PUSH, 32,
				InstructionSet.GREATER_THAN,
				InstructionSet.IF, 2,
				InstructionSet.GOTO, 2,
		});
		assertTrue("Failed advanced DUP/ADD/LOOP/CONDITIONAL test.", vm.stack().pop() == 32);
	}
	
}
