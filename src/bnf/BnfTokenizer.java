/**
 * 
 */
package bnf;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author mvm
 *
 */
public class BnfTokenizer implements Iterator<Token> {
    private Reader input;
    private enum States { READY, IN_TERMINAL, IN_NONTERMINAL, IN_DEFINED };
    private Token lastToken;
    private boolean useLastToken;
    
    /**
     * @param reader
     */
    public BnfTokenizer(java.io.Reader reader) {
        input = reader;
        lastToken = null;
        useLastToken = false;
    }

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

    @Override
    public Token next() {
        if (useLastToken == true) {
            useLastToken = false;
            return lastToken;
        }   
        int readInt = -1;
        int nextInt = -1;
        char ch;
        States state;
        String value = "";
        if (!hasNext()) {
            throw new NoSuchElementException("No more characters in the input.");
        }
        state = States.READY;
        do {
            try {
                readInt = input.read();
                input.mark(1);
                nextInt = input.read();
                input.reset();
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
                    value = ch + "";
                    if (Character.isWhitespace(ch)) break;
                    if (".|[]{}".contains(ch + "")) {
                        lastToken = new Token(TokenType.METASYMBOL, value);
                        return lastToken;
                    }
                    if (":".contains(ch + "")) {
                        state = States.IN_DEFINED;
                        break;
                    }
                    if ("<".contains(ch + "")) {
                        state = States.IN_NONTERMINAL;
                        break;
                    }
                    if (">".contains(ch + "")) {
                        throw new IllegalArgumentException("No token can start with '>'.");
                    }
                    else {
                        if (nextInt != -1 && ".|[]{}<>".contains((char)nextInt + "")) {
                            lastToken = new Token(TokenType.TERMINAL, value);
                            return lastToken;
                        } else {
                            state = States.IN_TERMINAL;
                            break;
                        }
                    }
                }
                case IN_TERMINAL: {
                    if (Character.isWhitespace(ch) && !(value.charAt(value.length() - 1) == '\\')) {
                        lastToken = new Token(TokenType.TERMINAL, value);
                        return lastToken;
                    } else {
                        if (nextInt == -1 || (".|[]{}<>".contains((char)nextInt + "")) && !("\\".contains(ch + ""))) {
                            // next int ends line or is a metasymbol
                            lastToken = new Token(TokenType.TERMINAL, value);
                            return lastToken;
                        } else if ("=".contains((char)nextInt + "")) {
                            
                        } else {
                            value += ch;
                            break;
                        }
                    }
                    
                    
                        //                        
//                    } else {
//                        if (nextInt == -1) {
//                            value += ch;
//                            lastToken = new Token(TokenType.TERMINAL, value);
//                            return lastToken;
//                        } else if (".|[]{}<>".contains((char)nextInt + "")) {
//                            
//                        } else {
//                            
//                        }
//                    }
//                    
//                    
//                    if (".|[]{}<>".contains(ch + "") && !(value.charAt(value.length() - 1) == '\\')) {
//                        throw new IllegalArgumentException("Terminals cannot contain metasymbols or angle brackets.");
//                    } else if (Character.isWhitespace(ch) && !(value.charAt(value.length() - 1) == '\\')) {
//                        lastToken = new Token(TokenType.TERMINAL, value);
//                        return lastToken;
//                    } else if (ch == '=' && value.endsWith("::") && !value.endsWith("\\::")) {
//                        throw new IllegalArgumentException("Whitespace must separate terminals and metasymbols");
//                    } else {
//                        value += ch;
//                        break;
//                    }
                }
                case IN_NONTERMINAL: { // TODO fix so it accounts for nextInt metasymbols and end of inputs
                    value += ch;
                    if (ch == '\n') {
                        throw new IllegalArgumentException("Nonterminals cannot contain newlines.");
                    } else if (ch == '<') {
                        throw new IllegalArgumentException("Nonterminals cannot contain angle brackets.");
                    } else if (ch == '>') {
                        lastToken = new Token(TokenType.NONTERMINAL, value);
                        return lastToken;
                    } else if (nextInt == -1) {
                        throw new IllegalArgumentException("Nonterminal at end of input doesn't have closing angle bracket.");
                    } else {
                        break;
                    }
                }
                case IN_DEFINED: { // TODO test, fix so it accounts for nextInt metasymbols and end of inputs
                    value += ch;
                    if (value.startsWith("::=") && value.length() == 3) {
                        lastToken = new Token(TokenType.METASYMBOL, value);
                        return lastToken;
                    } else if (value.startsWith("::") && value.length() == 2) {
                        if (nextInt == -1) {
                            lastToken = new Token(TokenType.TERMINAL, value);
                            return lastToken;
                        } else if ((char)nextInt != '=') {
                            state = States.IN_TERMINAL;
                            break;
                        } else {
                            break;
                        }
                    } else {
                        state = States.IN_TERMINAL;
                        break;
                    }
                                                            
//                    if (value.startsWith("::") && value.length() == 2) {
//                        break;
//                    } else if (value.startsWith("::=") && value.length() == 3) {
//                        lastToken = new Token(TokenType.METASYMBOL, value);
//                        return lastToken;
//                    } else {
//                        state = States.IN_TERMINAL;
//                        break;
//                    }
                }
                default: {
//                    lastToken = new Token(TokenType.ERROR, value); 
//                    return lastToken;
                }
            }
        } while (hasNext());
        if (value != "") {
            if (state == States.IN_TERMINAL) {
                lastToken = new Token(TokenType.TERMINAL, value);
                return lastToken;
            } else if (state == States.IN_NONTERMINAL) {
//                lastToken = new Token(TokenType.NONTERMINAL, value);
//                return lastToken;
                throw new IllegalArgumentException("Nonterminal at end of input doesn't have closing angle bracket.");
            } else if (state == States.IN_DEFINED) {
                lastToken = new Token(TokenType.TERMINAL, value);
                return lastToken;
            }
        }
        return null;
    }
    
    public void back() {
        useLastToken = true;        
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported.");
    }
    
    


}
