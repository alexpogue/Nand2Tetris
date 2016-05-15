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

	public void assemble(Parser parser, Writer hackWriter) throws IOException {
		SymbolTable symbolTable = new SymbolTable();
		initializeSymbolTable(symbolTable);
		buildSymbolTable(parser, symbolTable);
		while (parser.hasMoreCommands()) {
			parser.advance();
			String binary = commandBinary(parser, symbolTable);
			if (binary.length() > 0)
				hackWriter.write(binary + "\n");
		}
	}

	private void buildSymbolTable(Parser parser, SymbolTable symbolTable) throws IOException {
		firstPass(parser, symbolTable);
		parser.reset();
		secondPass(parser, symbolTable);
		parser.reset();
	}

	/**
	 * For each L-Instruction found with parser, add "name->ROM address"
	 * mappings to symbolTable.
	 *
	 * @param parser
	 * @param symbolTable
	 * @throws IOException
	 */
	private void firstPass(Parser parser, SymbolTable symbolTable) throws IOException {
		int instruction = 0;
		while (parser.hasMoreCommands()) {
			parser.advance();
			Command.Type cmdType = parser.commandType();
			switch (cmdType) {
			case BLANK:
				break;
			case A_COMMAND: // fall through
			case C_COMMAND:
				instruction++;
				break;
			case L_COMMAND:
				symbolTable.addEntry(parser.symbol(), instruction);
				break;
			case INVALID:
				throw new RuntimeException("Encountered invalid instruction. Instruction number " + instruction);
			default:
				throw new RuntimeException("Encountered unknown instruction type " + cmdType.toString());
			}
		}
	}

	/**
	 * For each A-Instruction whose symbol is not already in symbolTable, add
	 * "symbol -> RAM address" mappings to symbolTable
	 *
	 * @param parser
	 * @param symbolTable
	 * @throws IOException
	 */
	private void secondPass(Parser parser, SymbolTable symbolTable) throws IOException {
		int curAddress = 16;
		while (parser.hasMoreCommands()) {
			parser.advance();
			if (parser.commandType() == Command.Type.A_COMMAND) {
				String symbol = parser.symbol();
				if (!isNumber(symbol) && !symbolTable.contains(symbol)) {
					symbolTable.addEntry(symbol, curAddress);
					curAddress++;
				}
			}
		}
	}

	private void initializeSymbolTable(SymbolTable table) {
		table.addEntry("SP", 0);
		table.addEntry("LCL", 1);
		table.addEntry("ARG", 2);
		table.addEntry("THIS", 3);
		table.addEntry("THAT", 4);
		for (int i = 0; i < 16; i++)
			table.addEntry("R" + i, i);
		table.addEntry("SCREEN", 16384);
		table.addEntry("KBD", 24576);
	}

	private String getHackFileName(String asmFileName) {
		int extensionIndex = asmFileName.indexOf(".");
		return asmFileName.substring(0, extensionIndex) + ".hack";
	}

	private void printUsage() {
		System.out.println("Usage:");
		System.out.println("java Assembler <filename>.asm");
	}

	private String commandBinary(Parser parser, SymbolTable symbolTable) {
		String result = "";
		Command.Type cmdType = parser.commandType();
		if (cmdType == Command.Type.C_COMMAND) {
			String destMnemonic = parser.dest();
			String destBits = Code.dest(destMnemonic);
			String compMnemonic = parser.comp();
			String compBits = Code.comp(compMnemonic);
			String jumpMnemonic = parser.jump();
			String jumpBits = Code.jump(jumpMnemonic);
			result = "111" + compBits + destBits + jumpBits;
		} else if (cmdType == Command.Type.A_COMMAND) {
			String symbol = parser.symbol();
			Integer symbolInt;
			if (!isNumber(symbol))
				symbolInt = symbolTable.getAddress(symbol);
			else
				symbolInt = Integer.parseInt(symbol);
			if (symbolInt > 65535)
				throw new IllegalStateException("Address > 65535 in command: @" + symbol);
			String symbolBits = Integer.toBinaryString(symbolInt);
			// left-pad with zeros to get 16 bits
			symbolBits = String.format("%16s", symbolBits).replace(' ', '0');
			result = symbolBits;
		}
		return result;
	}

	private boolean isNumber(String symbol) {
		return symbol.matches("^[0-9]+");
	}
}
