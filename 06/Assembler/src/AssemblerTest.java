import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

public class AssemblerTest {

	public String multProgramNoVariables = "@2\n" + "M=0\n" + "@1\n" + "D=M\n" + "@16\n" + "M=D\n" + "@16\n" + "D=M\n"
			+ "@18\n" + "D;JLE\n" + "@0\n" + "D=M\n" + "@2\n" + "M=D+M\n" + "@16\n" + "M=M-1\n" + "@6\n" + "D;JMP\n";
	public String multProgramOut = "0000000000000010\n" + "1110101010001000\n" + "0000000000000001\n"
			+ "1111110000010000\n" + "0000000000010000\n" + "1110001100001000\n" + "0000000000010000\n"
			+ "1111110000010000\n" + "0000000000010010\n" + "1110001100000110\n" + "0000000000000000\n"
			+ "1111110000010000\n" + "0000000000000010\n" + "1111000010001000\n" + "0000000000010000\n"
			+ "1111110010001000\n" + "0000000000000110\n" + "1110001100000111\n";

	@Test
	public void testMultNoVariables() {
		Assembler assembler = new Assembler();
		Reader reader = new StringReader(multProgramNoVariables);
		Parser parser = null;
		try {
			parser = new Parser(reader);
		} catch (Exception e) {
			fail("new Parser(reader) threw exception: " + e.getMessage());
		}
		StringWriter writer = new StringWriter();
		try {
			assembler.assemble(parser, writer);
		} catch (Exception e) {
			fail("assembler.assemble() threw exception: " + e.getMessage());
		}
		assertEquals(multProgramOut, writer.toString());
	}
}
