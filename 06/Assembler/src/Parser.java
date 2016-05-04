import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Parser {
	private BufferedReader reader;
	private Command currentCommand;
	private String currentLine;
	private String nextLine;

	public void init(String file) throws IOException {
		init(new FileReader(file));
	}

	public void init(Reader readerIn) throws IOException {
		reader = new BufferedReader(readerIn);
		nextLine = reader.readLine();
	}

	public boolean hasMoreCommands() {
		return nextLine != null;
	}

	public void advance() throws IOException {
		currentLine = nextLine;
		currentCommand = CommandParser.parse(currentLine);
		nextLine = reader.readLine();
	}

	public Command.Type commandType() {
		return currentCommand.getType();
	}

	public String symbol() {
		String symbol = currentCommand.getSymbol();
		if (symbol == null)
			throw new IllegalStateException();
		return symbol;
	}

	public String dest() {
		String dest = currentCommand.getDest();
		if (dest == null)
			throw new IllegalStateException();
		return dest;
	}

	public String comp() {
		String comp = currentCommand.getComp();
		if (comp == null)
			throw new IllegalStateException();
		return comp;
	}

	public String jump() {
		String jump = currentCommand.getJump();
		if (jump == null)
			throw new IllegalStateException();
		return jump;
	}
}
