package com.brew.tests;

import static org.junit.Assert.*;

import com.brew.compiler.Compiler;
import com.brew.compiler.exceptions.CompilationException;
import com.brew.vm.InstructionSet;
import com.brew.vm.VirtualMachine;

import org.junit.Test;

/**
 * This class is for tests that involve both the compiler and the virtual machine.
 * It's important that they both can work, and work together.
 * @author Jonathan Force
 */
public class CompositeTests {

	@Test
	public void test() {
		// Create the compiler and virtual machine.
		Compiler compiler = new Compiler();
		VirtualMachine vm = new VirtualMachine();
		
		// ~~~~~~~~~~~~~~~~~~~~
		
		
		// Do a basic variable instantiation test.
		vm.interpreter().interpret(compiler.compose(
				new byte[] { InstructionSet.PUSH_FRAME, 3 },
				compiler.compileAssignmentStatement("byte foo = 4 * -4"),
				compiler.compileAssignmentStatement("byte x = 10 / 2"),
				compiler.compileAssignmentStatement("byte bar = x + foo")));
		assertTrue(
				"Failed a variable test.",
				vm.interpreter().getRuntime().pullVar((byte)0, (byte)2) == (byte) -11);
		
		compiler.reset();
		
		// Do a different variable instantiation test.
		vm.interpreter().interpret(compiler.compose(
				new byte[] { InstructionSet.PUSH_FRAME, 1 },
				compiler.compileAssignmentStatement("byte foo = 0"),
				compiler.compileAssignmentStatement("foo = foo + 5"),
				compiler.compileAssignmentStatement("foo = foo / foo")));
		assertTrue(
				"Failed a variable test.",
				vm.interpreter().getRuntime().pullVar((byte)0, (byte)0) == (byte) 1);
		
		
		
		// ~~~~~~~~~~~~
		compiler.reset();
		vm.stack().clear();
		
		// Do an if statement test
		vm.interpreter().interpret(
				compiler.compose(
						new byte[] { InstructionSet.PUSH_FRAME, 1, },
						compiler.compileAssignmentStatement("byte x = 5"),
						compiler.compileIfStatement("if (x > 3)", 2),
						new byte[] { InstructionSet.PUSH, (byte)66 }));
		
		assertTrue(
				"Failed an if statement composite test.",
				vm.stack().pop() == (byte)66);
		
		
		
	}

}
