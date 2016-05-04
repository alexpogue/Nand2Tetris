
public class CommandParser {

	public static Command parse(String cmd) {
		cmd = preprocess(cmd);
		Command.Type type = computeType(cmd);
		String symbol = extractSymbol(cmd, type);
		String dest = extractDest(cmd, type);
		String comp = extractComp(cmd, type);
		String jump = extractJump(cmd, type);
		return new Command(type, symbol, dest, comp, jump);
	}

	private static String preprocess(String cmd) {
		int commentStart = cmd.indexOf("//");
		if (commentStart != -1)
			cmd = cmd.substring(0, commentStart);
		cmd = cmd.trim();
		return cmd;
	}

	private static Command.Type computeType(String cmd) {
		String regexLCommand = "^\\([A-Za-z]+\\)";
		String regexACommand = "^\\@[A-Za-z0-9]+";
		String jmpPart = "(;J(GT|EQ|GE|LT|NE|LE|MP))?";
		String regexCCommand = "^(A?M?D?(?<=[AMD])=)?(D((\\+|-)(1|A|M)|(&|\\|)(A|M))|(A((\\+|-)1|-D))|M\\+1|M-1|M-D|0|-?1|(-|\\!)?(A|M|D))"
				+ jmpPart;
		if (cmd.matches(regexLCommand))
			return Command.Type.L_COMMAND;
		else if (cmd.matches(regexACommand))
			return Command.Type.A_COMMAND;
		else if (cmd.matches(regexCCommand))
			return Command.Type.C_COMMAND;
		else if (cmd.isEmpty())
			return Command.Type.BLANK;
		else
			return Command.Type.INVALID;
	}

	private static String extractSymbol(String cmd, Command.Type type) {
		if (type == Command.Type.A_COMMAND)
			return cmd.substring(1);
		else if (type == Command.Type.L_COMMAND)
			return cmd.substring(1, cmd.length() - 1);
		else
			return null;
	}

	private static String extractDest(String cmd, Command.Type type) {
		if (type != Command.Type.C_COMMAND)
			return null;
		int equalsIndex = cmd.indexOf('=');
		if (equalsIndex == -1)
			return "";
		else
			return cmd.substring(0, equalsIndex);
	}

	private static String extractComp(String cmd, Command.Type type) {
		if (type != Command.Type.C_COMMAND)
			return null;
		int equalsIndex = cmd.indexOf('=');
		int semicolonIndex = cmd.lastIndexOf(';');
		int beginIndex = (equalsIndex == -1) ? (0) : (equalsIndex + 1);
		int endIndex = (semicolonIndex == -1) ? (cmd.length()) : (semicolonIndex);
		return cmd.substring(beginIndex, endIndex);
	}

	private static String extractJump(String cmd, Command.Type type) {
		if (type != Command.Type.C_COMMAND)
			return null;
		int semicolonIndex = cmd.lastIndexOf(';');
		if (semicolonIndex == -1)
			return "";
		else
			return cmd.substring(semicolonIndex + 1);
	}
}
