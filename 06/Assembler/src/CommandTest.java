import static org.junit.Assert.assertEquals;

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

	@Test
	public void plainCInstruction() {
		for (String compSpec : compSpecifiers) {
			Command cmd = CommandParser.parse(compSpec);
			assertEquals(Command.Type.C_COMMAND, cmd.getType());
			assertEquals(null, cmd.getSymbol());
			assertEquals("", cmd.getDest());
			assertEquals(compSpec, cmd.getComp());
			assertEquals("", cmd.getJump());
		}
	}

	@Test
	public void cInstructionWithStorage() {
		for (String compSpec : compSpecifiers) {
			for (String storageSpec : storageSpecifiers) {
				Command cmd = CommandParser.parse(storageSpec + "=" + compSpec);
				assertEquals(Command.Type.C_COMMAND, cmd.getType());
				assertEquals(null, cmd.getSymbol());
				assertEquals(storageSpec, cmd.getDest());
				assertEquals(compSpec, cmd.getComp());
				assertEquals("", cmd.getJump());
			}
		}
	}

	@Test
	public void cInstructionWithJump() {
		for (String compSpec : compSpecifiers) {
			for (String jumpSpec : jumpSpecifiers) {
				Command cmd = CommandParser.parse(compSpec + ";" + jumpSpec);
				assertEquals(Command.Type.C_COMMAND, cmd.getType());
				assertEquals(null, cmd.getSymbol());
				assertEquals("", cmd.getDest());
				assertEquals(compSpec, cmd.getComp());
				assertEquals(jumpSpec, cmd.getJump());
			}
		}
	}

	@Test
	public void cInstructionWithStorageAndJump() {
		for (String compSpec : compSpecifiers) {
			for (String storageSpec : storageSpecifiers) {
				for (String jumpSpec : jumpSpecifiers) {
					Command cmd = CommandParser.parse(storageSpec + "=" + compSpec + ";" + jumpSpec);
					assertEquals(Command.Type.C_COMMAND, cmd.getType());
					assertEquals(null, cmd.getSymbol());
					assertEquals(storageSpec, cmd.getDest());
					assertEquals(compSpec, cmd.getComp());
					assertEquals(jumpSpec, cmd.getJump());
				}
			}
		}
	}

	@Test
	public void cInstructionWithComment() {
		for (String compSpec : compSpecifiers) {
			for (String comment : comments) {
				Command cmd = CommandParser.parse(compSpec + comment);
				assertEquals(Command.Type.C_COMMAND, cmd.getType());
				assertEquals(null, cmd.getSymbol());
				assertEquals("", cmd.getDest());
				assertEquals(compSpec, cmd.getComp());
				assertEquals("", cmd.getJump());
			}
		}
	}

	@Test
	public void cInstructionWithWhitespace() {
		for (String compSpec : compSpecifiers) {
			for (String ws : whitespace) {
				Command cmd = CommandParser.parse(ws + compSpec + ws);
				assertEquals(Command.Type.C_COMMAND, cmd.getType());
				assertEquals(null, cmd.getSymbol());
				assertEquals("", cmd.getDest());
				assertEquals(compSpec, cmd.getComp());
				assertEquals("", cmd.getJump());
			}
		}
	}

	@Test
	public void cInstructionStartingEqualsIsInvalid() {
		for (String compSpec : compSpecifiers) {
			Command cmd = CommandParser.parse("=" + compSpec);
			assertEquals(Command.Type.INVALID, cmd.getType());
			assertEquals(null, cmd.getSymbol());
			assertEquals(null, cmd.getDest());
			assertEquals(null, cmd.getComp());
			assertEquals(null, cmd.getJump());
		}
	}

	@Test
	public void aInstructionWithConstant() {
		for (String constant : constants) {
			Command cmd = CommandParser.parse("@" + constant);
			assertEquals(Command.Type.A_COMMAND, cmd.getType());
			assertEquals(constant, cmd.getSymbol());
			assertEquals(null, cmd.getDest());
			assertEquals(null, cmd.getComp());
			assertEquals(null, cmd.getJump());
		}
	}

	@Test
	public void aInstructionWithVarName() {
		for (String varName : varNames) {
			Command cmd = CommandParser.parse("@" + varName);
			assertEquals(Command.Type.A_COMMAND, cmd.getType());
			assertEquals(varName, cmd.getSymbol());
			assertEquals(null, cmd.getDest());
			assertEquals(null, cmd.getComp());
			assertEquals(null, cmd.getJump());
		}
	}

	@Test
	public void plainLInstruction() {
		for (String varName : varNames) {
			Command cmd = CommandParser.parse("(" + varName + ")");
			assertEquals(Command.Type.L_COMMAND, cmd.getType());
			assertEquals(varName, cmd.getSymbol());
			assertEquals(null, cmd.getDest());
			assertEquals(null, cmd.getComp());
			assertEquals(null, cmd.getJump());
		}
	}

	@Test
	public void whitespaceIsBlank() {
		for (String ws : whitespace) {
			Command cmd = CommandParser.parse(ws);
			assertEquals(Command.Type.BLANK, cmd.getType());
			assertEquals(null, cmd.getSymbol());
			assertEquals(null, cmd.getDest());
			assertEquals(null, cmd.getComp());
			assertEquals(null, cmd.getJump());
		}
	}

	@Test
	public void commentOnlyIsBlank() {
		for (String comment : comments) {
			Command cmd = CommandParser.parse(comment);
			assertEquals(Command.Type.BLANK, cmd.getType());
			assertEquals(null, cmd.getSymbol());
			assertEquals(null, cmd.getDest());
			assertEquals(null, cmd.getComp());
			assertEquals(null, cmd.getJump());
		}
	}
}
