package bnf;

/**
 * An enum of types to be used with the Token class as the types of those
 * tokens. The types enumerated here are useful in the processing of BNF
 * notation, denoting the various parts of BNF notation and ways of
 * processing BNF notation. 
 * 
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version CIT594 Spring 2013
 */
public enum TokenType {
	NONTERMINAL, TERMINAL, METASYMBOL, SEQUENCE, OR, OPTION, ANYNUM; 	
}
