import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	BufferedReader reader;
	Command currentCommand;
	String currentLine;
	String nextLine;
	
	public void init(String file) throws FileNotFoundException, IOException {
		reader = new BufferedReader(new FileReader(file));
		nextLine = reader.readLine();
	}
	
	boolean hasMoreCommands() {
		return nextLine != null;
	}
	
	void advance() throws IOException {
		currentLine = nextLine;
		currentCommand = new Command(currentLine);
		nextLine = reader.readLine();
	}
	
	Command.CommandType commandType() {
		return currentCommand.getType();
	}
	
	String symbol() {
		return currentCommand.getSymbol();
	}
	
	String dest() {
		return currentCommand.getDest();
	}
}