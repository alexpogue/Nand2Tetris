import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Assembler {
	public static void main(String[] args) {
		Assembler assembler = new Assembler();
		assembler.run(args);
	}

	private void run(String[] args) {
		if (args.length < 1) {
			System.out.println("No hack file given");
			printUsage();
			return;
		}
		String asmFileName = args[0];
		String hackFileName = getHackFileName(asmFileName);
		FileWriter hackWriter = null;
		try {
			hackWriter = new FileWriter(hackFileName);
			Parser parser = new Parser(asmFileName);
			assemble(parser, hackWriter);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		} finally {
			try {
				hackWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void assemble(Parser parser, Writer writer) throws IOException {
		while (parser.hasMoreCommands()) {
			parser.advance();
			String binary = commandBinary(parser);
			if (binary.length() > 0)
				writer.write(binary + "\n");
		}
	}

	private String getHackFileName(String asmFileName) {
		int extensionIndex = asmFileName.indexOf(".");
		return asmFileName.substring(0, extensionIndex) + ".hack";
	}

	private void printUsage() {
		System.out.println("Usage:");
		System.out.println("java Assembler <filename>.asm");
	}

	private String commandBinary(Parser parser) {
		String result = "";
		if (parser.commandType() == Command.Type.C_COMMAND) {
			String destMnemonic = parser.dest();
			String destBits = Code.dest(destMnemonic);
			String compMnemonic = parser.comp();
			String compBits = Code.comp(compMnemonic);
			String jumpMnemonic = parser.jump();
			String jumpBits = Code.jump(jumpMnemonic);
			result = "111" + compBits + destBits + jumpBits;
		} else if (parser.commandType() == Command.Type.A_COMMAND) {
			String symbol = parser.symbol();
			Integer symbolInt = Integer.parseInt(symbol);
			if (symbolInt > 65535)
				throw new IllegalStateException("Address > 65535 in command: @" + symbol);
			String symbolBits = Integer.toBinaryString(symbolInt);
			symbolBits = String.format("%16s", symbolBits).replace(' ', '0'); // pad
																				// w/
																				// 0s
			result = symbolBits;
		}
		return result;
	}
}
