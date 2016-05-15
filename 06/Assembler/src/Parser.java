import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Parser implements Cloneable {
	private BufferedReader reader;
	private Command currentCommand;
	private String currentLine;
	private String nextLine;

	public Parser(StringReader sReader, int numBytes) throws IOException {
		this(sReader);
		reader.mark(numBytes + 1);
		nextLine = reader.readLine();
	}

	public Parser(String fileName) throws IOException {
		this(new FileReader(new File(fileName)));
		File file = new File(fileName);
		reader.mark((int) file.length() + 1);
		nextLine = reader.readLine();
	}

	private Parser(Reader readerIn) throws IOException {
		reader = new BufferedReader(readerIn);
	}

	public void reset() throws IOException {
		reader.reset();
		currentLine = null;
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
