import static org.junit.Assert.*;

import org.junit.Test;

public class CommandTest {

	private String[] storageSpecifiers = { "A", "M", "D", "AM", "AD", "MD", "AMD" };
	private String[] jumpSpecifiers = { "JGT", "JEQ", "JGE", "JLT", "JNE", "JLE", "JMP" };
	private String[] compSpecifiers = { "0", "1", "-1", "D", "A", "!D", "!A", "-D", "-A", "D+1", "A+1", "D-1", "A-1",
			"D+A", "D-A", "A-D", "D&A", "D|A", "M", "!M", "-M", "M+1", "M-1", "D+M", "D-M", "M-D", "D&M", "D|M" };
	private String[] comments = { "// hello", "//", "// ", " //" };
	private String[] whitespace = { " ", "\t", "  ", "\t \t ", " \t\t " };
	private String[] varNames = { "abc", "ABC", "aBc", "LOOP" };
	private String[] constants = { "1328", "32994", "21", "0", "1" };
	
	private void assertGetSymbolThrows(Command cmd) {
		boolean thrown = false;
		try {
			cmd.getSymbol();
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	@Test
	public void plainCInstruction() {
		for (String compSpec : compSpecifiers) {
			Command cmd = new Command(compSpec);
			assertEquals(Command.CommandType.C_COMMAND, cmd.getType());
			assertEquals(null, cmd.getSymbol());
		}
	}

	@Test
	public void cInstructionWithStorage() {
		for (String compSpec : compSpecifiers) {
			for (String storageSpec : storageSpecifiers) {
				Command cmd = new Command(storageSpec + "=" + compSpec);
				assertEquals(Command.CommandType.C_COMMAND, cmd.getType());
				assertEquals(null, cmd.getSymbol());
			}
		}
	}

	@Test
	public void cInstructionWithJump() {
		for (String compSpec : compSpecifiers) {
			for (String jumpSpec : jumpSpecifiers) {
				Command cmd = new Command(compSpec + ";" + jumpSpec);
				assertEquals(Command.CommandType.C_COMMAND, cmd.getType());
				assertGetSymbolThrows(cmd);
			}
		}
	}

	@Test
	public void cInstructionWithStorageAndJump() {
		for (String compSpec : compSpecifiers) {
			for (String storageSpec : storageSpecifiers) {
				for (String jumpSpec : jumpSpecifiers) {
					Command cmd = new Command(storageSpec + "=" + compSpec + ";" + jumpSpec);
					assertEquals(Command.CommandType.C_COMMAND, cmd.getType());
					assertGetSymbolThrows(cmd);
				}
			}
		}
	}

	@Test
	public void cInstructionWithComment() {
		for (String compSpec : compSpecifiers) {
			for (String comment : comments) {
				Command cmd = new Command(compSpec + comment);
				assertEquals(Command.CommandType.C_COMMAND, cmd.getType());
				assertGetSymbolThrows(cmd);
			}
		}
	}

	@Test
	public void cInstructionWithWhitespace() {
		for (String compSpec : compSpecifiers) {
			for (String ws : whitespace) {
				Command cmd = new Command(ws + compSpec + ws);
				assertEquals(Command.CommandType.C_COMMAND, cmd.getType());
				assertGetSymbolThrows(cmd);
			}
		}
	}

	@Test
	public void cInstructionStartingEqualsIsInvalid() {
		for (String compSpec : compSpecifiers) {
			Command cmd = new Command("=" + compSpec);
			assertEquals(Command.CommandType.INVALID, cmd.getType());
			assertGetSymbolThrows(cmd);
		}
	}

	@Test
	public void aInstructionWithConstant() {
		for (String constant : constants) {
			Command cmd = new Command("@" + constant);
			assertEquals(Command.CommandType.A_COMMAND, cmd.getType());
			assertEquals(constant, cmd.getSymbol());
		}
	}

	@Test
	public void aInstructionWithVarName() {
		for (String varName : varNames) {
			Command cmd = new Command("@" + varName);
			assertEquals(Command.CommandType.A_COMMAND, cmd.getType());
			assertEquals(varName, cmd.getSymbol());
		}
	}

	@Test
	public void plainLInstruction() {
		for (String varName : varNames) {
			Command cmd = new Command("(" + varName + ")");
			assertEquals(Command.CommandType.L_COMMAND, cmd.getType());
			assertEquals(varName, cmd.getSymbol());
		}
	}

	@Test
	public void whitespaceIsBlank() {
		for (String ws : whitespace) {
			Command cmd = new Command(ws);
			assertEquals(Command.CommandType.BLANK, cmd.getType());
			assertGetSymbolThrows(cmd);
		}
	}

	@Test
	public void commentOnlyIsBlank() {
		for (String comment : comments) {
			Command cmd = new Command(comment);
			assertEquals(Command.CommandType.BLANK, cmd.getType());
			assertGetSymbolThrows(cmd);
		}
	}
}
