
public class Code {
	public static String dest(String mnemonic) {
		if (!mnemonic.matches("A?M?D?"))
			throw new IllegalStateException("Dest mnemonic had unexpected value: " + mnemonic);
		String aBit = "0";
		String mBit = "0";
		String dBit = "0";
		if (mnemonic.contains("A"))
			aBit = "1";
		if (mnemonic.contains("M"))
			mBit = "1";
		if (mnemonic.contains("D"))
			dBit = "1";
		return aBit + dBit + mBit;
	}

	public static String comp(String mnemonic) {
		String aBit = (mnemonic.contains("M")) ? "1" : "0";
		String cBits;
		if (mnemonic.equals("0"))
			cBits = "101010";
		else if (mnemonic.equals("1"))
			cBits = "111111";
		else if (mnemonic.equals("-1"))
			cBits = "111010";
		else if (mnemonic.equals("D"))
			cBits = "001100";
		else if (mnemonic.equals("A") || mnemonic.equals("M"))
			cBits = "110000";
		else if (mnemonic.equals("!D"))
			cBits = "001101";
		else if (mnemonic.equals("!A") || mnemonic.equals("!M"))
			cBits = "110001";
		else if (mnemonic.equals("-D"))
			cBits = "001111";
		else if (mnemonic.equals("-A") || mnemonic.equals("-M"))
			cBits = "110011";
		else if (mnemonic.equals("D+1"))
			cBits = "011111";
		else if (mnemonic.equals("A+1") || mnemonic.equals("M+1"))
			cBits = "110111";
		else if (mnemonic.equals("D-1"))
			cBits = "001110";
		else if (mnemonic.equals("A-1") || mnemonic.equals("M-1"))
			cBits = "110010";
		else if (mnemonic.equals("D+A") || mnemonic.equals("D+M"))
			cBits = "000010";
		else if (mnemonic.equals("D-A") || mnemonic.equals("D-M"))
			cBits = "010011";
		else if (mnemonic.equals("A-D") || mnemonic.equals("M-D"))
			cBits = "000111";
		else if (mnemonic.equals("D&A") || mnemonic.equals("D&M"))
			cBits = "000000";
		else if (mnemonic.equals("D|A") || mnemonic.equals("D|M"))
			cBits = "010101";
		else
			throw new IllegalStateException("Comp mnemonic had unexpected value: " + mnemonic);
		return aBit + cBits;
	}

	public static String jump(String mnemonic) {
		if (mnemonic.equals(""))
			return "000";
		else if (mnemonic.equals("JGT"))
			return "001";
		else if (mnemonic.equals("JEQ"))
			return "010";
		else if (mnemonic.equals("JGE"))
			return "011";
		else if (mnemonic.equals("JLT"))
			return "100";
		else if (mnemonic.equals("JNE"))
			return "101";
		else if (mnemonic.equals("JLE"))
			return "110";
		else if (mnemonic.equals("JMP"))
			return "111";
		else
			throw new IllegalStateException("Jump mnemonic had unexpected value: " + mnemonic);
	}
}
