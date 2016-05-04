
public class Command {

	private Type type;
	private String symbol;
	private String dest;
	private String jump;
	private String comp;

	public Command(Type type, String symbol, String dest, String comp, String jump) {
		this.type = type;
		this.symbol = symbol;
		this.dest = dest;
		this.comp = comp;
		this.jump = jump;
	}

	public Type getType() {
		return type;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getDest() {
		return dest;
	}

	public String getJump() {
		return jump;
	}

	public String getComp() {
		return comp;
	}

	public static enum Type {
		A_COMMAND, C_COMMAND, L_COMMAND, BLANK, INVALID
	}
}
