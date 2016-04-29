
public class Command {
	
	private CommandType type;
	private String symbol;
	private String dest;
	
	public Command(String text) {
		parse(text);
	}
	
	public CommandType getType() {
		return type;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public String getDest() {
		return dest;
	}
	
	private void parse(String cmd) {
		cmd = preprocess(cmd);
		type = computeType(cmd);
		symbol = extractSymbol(cmd, type);
		dest = extractDest(cmd, type);
	}

	private String preprocess(String cmd) {
		int commentStart = cmd.indexOf("//");
		if (commentStart != -1)
			cmd = cmd.substring(0, commentStart);
		cmd = cmd.trim();
		return cmd;
	}
	
	private CommandType computeType(String cmd) {
		String regexLCommand = "^\\([A-Za-z]+\\)";
		String regexACommand = "^\\@[A-Za-z0-9]+";
		String jmpPart = "(;J(GT|EQ|GE|LT|NE|LE|MP))?";
		String regexCCommand = "^(A?M?D?(?<=[AMD])=)?(D((\\+|-)(1|A|M)|(&|\\|)(A|M))|(A((\\+|-)1|-D))|M\\+1|M-1|M-D|0|-?1|(-|\\!)?(A|M|D))" + jmpPart;
		if (cmd.matches(regexLCommand))
			return CommandType.L_COMMAND;
		else if (cmd.matches(regexACommand))
			return CommandType.A_COMMAND;
		else if (cmd.matches(regexCCommand))
			return CommandType.C_COMMAND;
		else if (cmd.isEmpty())
			return CommandType.BLANK;
		else
			return CommandType.INVALID;
	}
	
	private String extractSymbol(String cmd, CommandType type) {
		if (type == CommandType.A_COMMAND)
			return cmd.substring(1);
		else if (type == CommandType.L_COMMAND)
			return cmd.substring(1, cmd.length() - 1);
		else
			throw new IllegalArgumentException();
	}
	
	private String extractDest(String cmd, CommandType type) {
		if (type != CommandType.C_COMMAND)
			throw new IllegalArgumentException();
		int equalsIndex = cmd.indexOf('=');
		if (equalsIndex == -1)
			return null;
		else
			return cmd.substring(0, equalsIndex);
	}

	public static enum CommandType {
		A_COMMAND,
		C_COMMAND,
		L_COMMAND,
		BLANK,
		INVALID
	}
}
