package bnf;

public class Token {
	private TokenType type;
	private String value;
	
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
	
	/** 
	 * Compares this <pre>Token</pre> to another object and tests for
	 * equality. Tokens are considered equal if they are both <pre>Tokens</pre>,
	 * and they have the same type and same value.
	 * 
	 * Code borrowed from Dave Matuszek's tokenizers.ppt slide deck,
	 * CIT594, Spring 2013.
	 * 
	 * @param object The object to test for equality.
	 * @return <pre>true</pre> if the objects are equal.
	 */
	public boolean equals(Object object) {
	    if (object == null) return false;
	    if (!(object instanceof Token)) return false;
	    Token that = (Token)object;
	    return this.type == that.type && this.value.equals(that.value);
	}
	
	/**
	 * Returns a string representation of the token, of the format:
	 * value:type
	 * 
	 * Code borrowed from Dave Matuszek's tokenizers.ppt slide deck,
         * CIT594, Spring 2013.
         * 
         * @return The string representation of the token.
	 */
	public String toString() {
	    return value + ":" + type;
	}
}
