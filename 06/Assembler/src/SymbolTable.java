import java.util.HashMap;

public class SymbolTable {
	private HashMap<String, Integer> symbols;

	public SymbolTable() {
		symbols = new HashMap<>();
	}

	public void addEntry(String symbol, int address) {
		symbols.put(symbol, address);
	}

	public boolean contains(String symbol) {
		return symbols.containsKey(symbol);
	}

	public int getAddress(String symbol) {
		return symbols.get(symbol);
	}
}
