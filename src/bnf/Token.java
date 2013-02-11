package bnf;

public class Token {
		TokenType type;
		String value;
	
	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public TokenType getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}

}
