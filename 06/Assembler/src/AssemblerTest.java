import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

public class AssemblerTest {

	private String multProgramNoVariables = "@2\n" + "M=0\n" + "@1\n" + "D=M\n" + "@16\n" + "M=D\n" + "@16\n" + "D=M\n"
			+ "@18\n" + "D;JLE\n" + "@0\n" + "D=M\n" + "@2\n" + "M=D+M\n" + "@16\n" + "M=M-1\n" + "@6\n" + "D;JMP\n";
	private String multProgramWithVariables = "@R2\n" + "M=0\n" + "@R1\n" + "D=M\n" + "@i\n" + "M=D\n" + "(LOOP)\n"
			+ "@i\n" + "D=M\n" + "@END\n" + "D;JLE\n" + "@R0\n" + "D=M\n" + "@R2\n" + "M=D+M\n" + "@i\n" + "M=M-1\n"
			+ "@LOOP\n" + "D;JMP\n" + "(END)\n";
	private String multProgramOut = "0000000000000010\n" + "1110101010001000\n" + "0000000000000001\n"
			+ "1111110000010000\n" + "0000000000010000\n" + "1110001100001000\n" + "0000000000010000\n"
			+ "1111110000010000\n" + "0000000000010010\n" + "1110001100000110\n" + "0000000000000000\n"
			+ "1111110000010000\n" + "0000000000000010\n" + "1111000010001000\n" + "0000000000010000\n"
			+ "1111110010001000\n" + "0000000000000110\n" + "1110001100000111\n";

	private String fillProgramWithVariables = "@i\n" + "M=0\n" + "(LOOP)\n" + "D=0\n" + "D=!D\n" + "@color\n" + "M=D\n"
			+ "@KBD\n" + "D=M\n" + "@SKIP_WHITE\n" + "D;JNE\n" + "@color\n" + "M=0\n" + "(SKIP_WHITE)\n" + "@i\n"
			+ "D=M\n" + "@SCREEN\n" + "D=D+A\n" + "@target\n" + "M=D\n" + "@color\n" + "D=M\n" + "@target\n" + "A=M\n"
			+ "M=D\n" + "@8191\n" + "D=A\n" + "@i\n" + "M=M+1\n" + "M=D&M\n" + "@LOOP\n" + "0;JMP\n";
	private String fillProgramOut = "0000000000010000\n" + "1110101010001000\n" + "1110101010010000\n"
			+ "1110001101010000\n" + "0000000000010001\n" + "1110001100001000\n" + "0110000000000000\n"
			+ "1111110000010000\n" + "0000000000001100\n" + "1110001100000101\n" + "0000000000010001\n"
			+ "1110101010001000\n" + "0000000000010000\n" + "1111110000010000\n" + "0100000000000000\n"
			+ "1110000010010000\n" + "0000000000010010\n" + "1110001100001000\n" + "0000000000010001\n"
			+ "1111110000010000\n" + "0000000000010010\n" + "1111110000100000\n" + "1110001100001000\n"
			+ "0001111111111111\n" + "1110110000010000\n" + "0000000000010000\n" + "1111110111001000\n"
			+ "1111000000001000\n" + "0000000000000010\n" + "1110101010000111\n";

	private String addProgram = "@2\n" + "D=A\n" + "@3\n" + "D=D+A\n" + "@0\n" + "M=D\n";
	private String addProgramOut = "0000000000000010\n" + "1110110000010000\n" + "0000000000000011\n"
			+ "1110000010010000\n" + "0000000000000000\n" + "1110001100001000\n";

	private String maxProgramNoVariables = "@0\n" + "D=M\n" + "@1\n" + "D=D-M\n" + "@10\n" + "D;JGT\n" + "@1\n"
			+ "D=M\n" + "@12\n" + "0;JMP\n" + "@0\n" + "D=M\n" + "@2\n" + "M=D\n" + "@14\n" + "0;JMP\n";
	private String maxProgramWithVariables = "   @R0\n" + "   D=M\n" + "   @R1\n" + "   D=D-M\n" + "   @OUTPUT_FIRST\n"
			+ "   D;JGT\n" + "   @R1\n" + "   D=M\n" + "   @OUTPUT_D\n" + "   0;JMP\n" + "(OUTPUT_FIRST)\n"
			+ "   @R0             \n" + "   D=M\n" + "(OUTPUT_D)\n" + "   @R2\n" + "   M=D\n" + "(INFINITE_LOOP)\n"
			+ "   @INFINITE_LOOP\n" + "   0;JMP\n";
	private String maxProgramOut = "0000000000000000\n" + "1111110000010000\n" + "0000000000000001\n"
			+ "1111010011010000\n" + "0000000000001010\n" + "1110001100000001\n" + "0000000000000001\n"
			+ "1111110000010000\n" + "0000000000001100\n" + "1110101010000111\n" + "0000000000000000\n"
			+ "1111110000010000\n" + "0000000000000010\n" + "1110001100001000\n" + "0000000000001110\n"
			+ "1110101010000111\n";

	private String rectProgramNoVariables = "@0\n" + "D=M\n" + "@23\n" + "D;JLE\n" + "@16\n" + "M=D\n" + "@16384\n"
			+ "D=A\n" + "@17\n" + "M=D\n" + "@17\n" + "A=M\n" + "M=-1\n" + "@17\n" + "D=M\n" + "@32\n" + "D=D+A\n"
			+ "@17\n" + "M=D\n" + "@16\n" + "MD=M-1\n" + "@10\n" + "D;JGT\n" + "@23\n" + "0;JMP\n";
	private String rectProgramWithVariables = "@0\n" + "D=M\n" + "@INFINITE_LOOP\n" + "D;JLE \n" + "@counter\n"
			+ "M=D\n" + "@SCREEN\n" + "D=A\n" + "@address\n" + "M=D\n" + "(LOOP)\n" + "@address\n" + "A=M\n" + "M=-1\n"
			+ "@address\n" + "D=M\n" + "@32\n" + "D=D+A\n" + "@address\n" + "M=D\n" + "@counter\n" + "MD=M-1\n"
			+ "@LOOP\n" + "D;JGT\n" + "(INFINITE_LOOP)\n" + "@INFINITE_LOOP\n" + "0;JMP\n";
	private String rectProgramOut = "0000000000000000\n" + "1111110000010000\n" + "0000000000010111\n"
			+ "1110001100000110\n" + "0000000000010000\n" + "1110001100001000\n" + "0100000000000000\n"
			+ "1110110000010000\n" + "0000000000010001\n" + "1110001100001000\n" + "0000000000010001\n"
			+ "1111110000100000\n" + "1110111010001000\n" + "0000000000010001\n" + "1111110000010000\n"
			+ "0000000000100000\n" + "1110000010010000\n" + "0000000000010001\n" + "1110001100001000\n"
			+ "0000000000010000\n" + "1111110010011000\n" + "0000000000001010\n" + "1110001100000001\n"
			+ "0000000000010111\n" + "1110101010000111\n";

	@Test
	public void testMultNoVariables() {
		testStringAssembleResult(multProgramNoVariables, multProgramOut);
	}

	@Test
	public void testMultWithVariables() {
		testStringAssembleResult(multProgramWithVariables, multProgramOut);
	}

	@Test
	public void testFillWithVariables() {
		testStringAssembleResult(fillProgramWithVariables, fillProgramOut);
	}

	@Test
	public void testAdd() {
		testStringAssembleResult(addProgram, addProgramOut);
	}

	@Test
	public void testMaxNoVariables() {
		testStringAssembleResult(maxProgramNoVariables, maxProgramOut);
	}

	@Test
	public void testMaxWithVariables() {
		testStringAssembleResult(maxProgramWithVariables, maxProgramOut);
	}

	@Test
	public void testRectNoVariables() {
		testStringAssembleResult(rectProgramNoVariables, rectProgramOut);
	}

	@Test
	public void testRectWithVariables() {
		testStringAssembleResult(rectProgramWithVariables, rectProgramOut);
	}

	private void testStringAssembleResult(String program, String expectedOutput) {
		Assembler assembler = new Assembler();
		StringReader reader = new StringReader(program);
		Parser parser = null;
		try {
			parser = new Parser(reader, program.length());
		} catch (Exception e) {
			fail("new Parser(reader) threw exception: " + e.getMessage());
		}
		StringWriter writer = new StringWriter();
		try {
			assembler.assemble(parser, writer);
		} catch (Exception e) {
			fail("assembler.assemble() threw exception: " + e.getMessage());
		}
		assertEquals(expectedOutput, writer.toString());

	}
}
