package bnf;

/**
 * A class that allows the creation of Token data structures. These simple 
 * objects hold a type and value, both specified at the time of creation.
 * 
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version CIT594 Spring 2013
 */
public class Token {
	private TokenType type;
	private String value;
	
	/**
	 * Constructor for Token. Creates a token with the type and value
	 * specified in the parameters.
	 * 
	 * @param type The type of token to create.
	 * @param value The value of the token to create.
	 */
	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Gets the token type of this token.
	 * 
	 * @return The token type of this token.
	 */
	public TokenType getType() {
		return type;
	}
	
	/**
	 * Gets the value of this token.
	 * 
	 * @return The value of this token.
	 */
	public String getValue() {
		return value;
	}
	
	/** 
	 * Compares this Token to another object and tests for equality. Tokens
	 * are considered equal if they are both Tokens, and they have the same
	 * type and same value.
	 * 
	 * Code borrowed from Dave Matuszek's tokenizers.ppt slide deck,
	 * CIT594, Spring 2013.
	 * 
	 * @param object The object to test for equality.
	 * @return true if the objects are equal.
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
