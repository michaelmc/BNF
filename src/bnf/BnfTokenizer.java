package bnf;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A tokenizer that parses and generates tokens from Backus-Naur Form (BNF) 
 * notation. Tokens can be TERMINAL, NONTERMINAL, or METASYMBOL.
 * 
 * NONTERMINAL: enclosed by left and right angle brackets and may contain
 * any character except angle brackets (even escaped) or newline.
 * 
 * METASYMBOL: ::=, ., |, [, ], {, or }
 * 
 * TERMINAL: any other set of characters unbroken by unescaped whitespace/
 * whitespace characters or unescaped metasymbols.
 * 
 * Implements the Iterator interface, and allows the user to tell if there 
 * is more of the Reader input to parse (hasNext()), to get the next token
 * (next()), and to go back (back()) so the most recent token is given again
 * by next().
 * 
 * The remove() function is unsupported.
 * 
 * Much of the initial structure of the state machine derives from Professor
 * David Matuszek's slides on state machines and tokenizers, though it has
 * been heavily modified to handle BNF input.
 * 
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version CIT594 Spring 2013
 */
public class BnfTokenizer implements Iterator<Token> {
    private Reader input;
    private enum States { READY, IN_TERMINAL, IN_NONTERMINAL, IN_METASYMBOL, IN_ESCAPED, IN_DEFINED };
    private Token lastToken;
    boolean useLastToken;
    
    /**
     * Constructor for the BnfTokenizer. Accepts any Reader as input, typically
     * a StringReader or BufferedReader.
     * 
     * @param reader A Reader object or subclass object to parse.
     */
    public BnfTokenizer(java.io.Reader reader) {
        input = reader;
        lastToken = null;
        useLastToken = false;
    }

    /**
     * Tells whether there is more input to be read from the Reader. Having
     * more characters to be read does not guarantee that any are valid
     * tokens, so a subsequent call of next() may return null rather than a
     * token.
     * 
     * @return true if there is more to be read from the Reader
     */
    @Override
    public boolean hasNext() {
        try {
            input.mark(1);
            int chr = input.read();
            input.reset();
            if (chr == -1) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Generates and returns the next token from the input. May generate tokens
     * of the types NONTERMINAL, TERMINAL, or METASYMBOL. May return null if
     * there are no more valid tokens to be generated from the input.
     * 
     * @throws IllegalStateException in the case of malformed BNF input.
     * 
     * @return the next token from the Reader.
     */
    @Override
    public Token next() {
        if (useLastToken == true) {
            useLastToken = false;
            return lastToken;
        }   
        int readInt = -1;
        char ch;
        int nextInt = -1;
        int secondInt = -1;
        States state;
        StringBuilder value = new StringBuilder();
//        if (!hasNext()) {
//            throw new NoSuchElementException("No more characters in the input.");
//        }
        state = States.READY;
        do {
            try {
                input.mark(1);
                readInt = input.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (readInt == -1) {
                throw new NoSuchElementException("No more characters in the input.");
            } else {
                ch = (char)readInt;
            }
            switch (state) {
                case READY: {
                    if (Character.isWhitespace(ch)) break;
                    if ("\\".contains(ch + "")) {
                        state = States.IN_ESCAPED;
                        break;
                    }
                    value.append(ch);
                    if (".|[]{}".contains(ch + "")) {
                        lastToken = new Token(TokenType.METASYMBOL, value.toString());
                        return lastToken;
                    } else if (":".contains(ch + "")) {
                        state = States.IN_DEFINED;
                        break;
                    } else if ("<".contains(ch + "")) {
                        state = States.IN_NONTERMINAL;
                        break;
                    } else if (">".contains(ch + "")) {
                        throw new IllegalStateException("No token can start with '>'.");
                    } else {
                        state = States.IN_TERMINAL;
                        break;
                    }
                }
                case IN_TERMINAL: {
                    if (Character.isWhitespace(ch)) {
                        lastToken = new Token(TokenType.TERMINAL, value.toString());
                        return lastToken;
                    } else if (".|[]{}<>".contains(ch + "")) {
                        try {
                            input.reset();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        lastToken = new Token(TokenType.TERMINAL, value.toString());
                        return lastToken;
                    } else if (ch == ':') {
                        try {
                            input.reset();
                            input.mark(3);
                            readInt = input.read();
                            nextInt = input.read();
                            secondInt = input.read();
                            input.reset();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ch = ':';
                        if (nextInt == -1 || secondInt == -1) {
                            try {
                                readInt = input.read();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            value.append(ch);
                            break;
                        } else if ((char)nextInt == ':' && (char)secondInt == '=') {
                            lastToken = new Token(TokenType.TERMINAL, value.toString());
                            return lastToken;
                        } else {
                            try {
                                readInt = input.read();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            value.append(ch);
                            break;
                        }
                    } else if (ch == '\\') {
                        state = States.IN_ESCAPED;
                        break;
                    } else {
                        value.append(ch);
                        break;
                    }
                }
                case IN_ESCAPED: {
                    if (ch == 'n' || ch == 't' || ch == 'r') {
                        lastToken = new Token(TokenType.TERMINAL, value.toString());
                        return lastToken;
                    } else {
                        value.append(ch);
                        state = States.IN_TERMINAL;
                        break;
                    }
                }
                case IN_NONTERMINAL: {
                    value.append(ch);
                    if (ch == '\n') {
                        throw new IllegalStateException("Nonterminals cannot contain newlines.");
                    } else if (ch == '<' && !(value.toString().endsWith("\\<"))) {
                        throw new IllegalStateException("Nonterminals cannot contain angle brackets.");
                    } else if (ch == '>' && !(value.toString().endsWith("\\>"))) {
                        lastToken = new Token(TokenType.NONTERMINAL, value.toString());
                        return lastToken;
                    } else {
                        break;
                    }
                }
                case IN_DEFINED: {
                    if (Character.isWhitespace(ch)) {
                        lastToken = new Token(TokenType.TERMINAL, value.toString());
                        return lastToken;
                    } else {
                        if (":=".contains(ch + "")) {
                            value.append(ch);
                            if (value.toString().equals("::=")) {
                                lastToken = new Token(TokenType.METASYMBOL, value.toString());
                                return lastToken;
                            } else if (value.toString().equals("::")) {
                                break;
                            } else {
                                state = States.IN_TERMINAL;
                                break;
                            }                            
                        } else if (".|[]{}<>".contains(ch + "")) {
                            try {
                                input.reset();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            lastToken = new Token(TokenType.METASYMBOL, value.toString());
                            return lastToken;
                        } else {
                            value.append(ch);
                            state = States.IN_TERMINAL;
                            break;
                        }
                    }
                }
                default: {
                    throw new IllegalStateException("Something went wrong with your input.");
                }
            }
        } while (hasNext());
        if (value.toString() != "") {
            if (state == States.IN_TERMINAL) {
                lastToken = new Token(TokenType.TERMINAL, value.toString());
                return lastToken;
            } else if (state == States.IN_NONTERMINAL) {
                if (value.toString().endsWith(">")) {
                    lastToken = new Token(TokenType.NONTERMINAL, value.toString());
                    return lastToken;
                } else {
                    throw new IllegalStateException("Nonterminal at end of input doesn't have closing angle bracket.");    
                }
            } else if (state == States.IN_DEFINED) {
                lastToken = new Token(TokenType.TERMINAL, value.toString());
                return lastToken;
            }
        }
        return null;
    }
    
    /**
     * Goes back one token, so the next call to next() will return the same
     * token as the most recent previous call to next().
     * 
     * This is not cumulative--the same most recent token will be returned
     * again no matter how many times back() is called.
     */
    public void back() {
        useLastToken = true;        
    }
    
    /**
     * This function is not supported by the BnfTokenizer class.
     * 
     * @throws UnsupportedOperationException
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported.");
    }
}
