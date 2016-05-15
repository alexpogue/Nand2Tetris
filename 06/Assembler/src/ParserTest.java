import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.Callable;

import org.junit.Test;

public class ParserTest {

	@Test
	public void testSpecialCharactersInLabel() throws IOException {
		String underscore = "(inner_underscore)\n(_leadingunderscore)\n(:$_.)\n(9invalid)\n(843)";
		StringReader reader = new StringReader(underscore);
		Parser p = new Parser(reader, underscore.length());
		assertNextCommand(p, Command.Type.L_COMMAND, "inner_underscore", null, null, null);
		assertNextCommand(p, Command.Type.L_COMMAND, "_leadingunderscore", null, null, null);
		assertNextCommand(p, Command.Type.L_COMMAND, ":$_.", null, null, null);
		assertNextCommand(p, Command.Type.INVALID, null, null, null, null);
		assertNextCommand(p, Command.Type.INVALID, null, null, null, null);
	}

	@Test
	public void testSpecialCharactersInAddress() throws IOException {
		String underscore = "@inner_underscore\n@_leadingunderscore\n@:$_.\n@123\n@123invalid";
		StringReader reader = new StringReader(underscore);
		Parser p = new Parser(reader, underscore.length());
		assertNextCommand(p, Command.Type.A_COMMAND, "inner_underscore", null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "_leadingunderscore", null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, ":$_.", null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "123", null, null, null);
		assertNextCommand(p, Command.Type.INVALID, null, null, null, null);
	}

	@Test
	public void test() throws IOException {
		String program = "// R2 = 0;\n" + "@R2\n" + "M=0\n" + "// D = R1\n" + "@R1\n" + "D=M\n"
				+ "// i = R1, and i will decrement until 0\n" + "@i\n" + "M=D\n" + "(LOOP)\n" + "\n"
				+ "// if (i <= 0) goto END;\n"
				+ "// use <= so that we break early and just return 0 if R1 is negative\n" + "@i\n" + "D=M\n" + "@END\n"
				+ "D;JLE\n" + "// D = R0\n" + "@R0\n" + "D=M\n" + "// R2 = R2 + R0\n" + "@R2\n" + "M=D+M\n"
				+ "// i = i - 1\n" + "@i\n" + "M=M-1\n" + "// goto LOOP;\n" + "@LOOP\n" + "D;JMP\n" + "(END)";
		StringReader reader = new StringReader(program);
		Parser p = new Parser(reader, program.length());
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "R2", null, null, null);
		assertNextCommand(p, Command.Type.C_COMMAND, null, "M", "0", "");
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "R1", null, null, null);
		assertNextCommand(p, Command.Type.C_COMMAND, null, "D", "M", "");
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "i", null, null, null);
		assertNextCommand(p, Command.Type.C_COMMAND, null, "M", "D", "");
		assertNextCommand(p, Command.Type.L_COMMAND, "LOOP", null, null, null);
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "i", null, null, null);
		assertNextCommand(p, Command.Type.C_COMMAND, null, "D", "M", "");
		assertNextCommand(p, Command.Type.A_COMMAND, "END", null, null, null);
		assertNextCommand(p, Command.Type.C_COMMAND, null, "", "D", "JLE");
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "R0", null, null, null);
		assertNextCommand(p, Command.Type.C_COMMAND, null, "D", "M", "");
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "R2", null, null, null);
		assertNextCommand(p, Command.Type.C_COMMAND, null, "M", "D+M", "");
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "i", null, null, null);
		assertNextCommand(p, Command.Type.C_COMMAND, null, "M", "M-1", "");
		assertNextCommand(p, Command.Type.BLANK, null, null, null, null);
		assertNextCommand(p, Command.Type.A_COMMAND, "LOOP", null, null, null);
		assertNextCommand(p, Command.Type.C_COMMAND, null, "", "D", "JMP");
		assertNextCommand(p, Command.Type.L_COMMAND, "END", null, null, null);

		assertEquals(false, p.hasMoreCommands());
	}

	private void assertNextCommand(Parser p, Command.Type type, String symbol, String dest, String comp, String jump)
			throws IOException {
		p.advance();
		assertEquals(type, p.commandType());
		if (symbol == null) {
			assertThrows(p, new Callable<String>() {
				public String call() {
					return p.symbol();
				}
			});
		} else {
			assertEquals(symbol, p.symbol());
		}
		if (dest == null) {
			assertThrows(p, new Callable<String>() {
				public String call() {
					return p.dest();
				}
			});
		} else {
			assertEquals(dest, p.dest());
		}
		if (comp == null) {
			assertThrows(p, new Callable<String>() {
				public String call() {
					return p.comp();
				}
			});
		} else {
			assertEquals(comp, p.comp());
		}
		if (jump == null) {
			assertThrows(p, new Callable<String>() {
				public String call() {
					return p.jump();
				}
			});
		} else {
			assertEquals(jump, p.jump());
		}
	}

	private void assertThrows(Parser p, Callable<String> c) {
		boolean thrown = false;
		try {
			c.call();
		} catch (Exception e) {
			thrown = true;
		}
		assertEquals(true, thrown);
	}

}
