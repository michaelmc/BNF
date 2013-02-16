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
    private enum States { READY, IN_TERMINAL, IN_NONTERMINAL, IN_METASYMBOL, IN_ESCAPED, IN_DEFINED };
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
        char ch;
        int nextInt = -1;
        int secondInt = -1;
        States state;
        StringBuilder value = new StringBuilder();
        if (!hasNext()) {
            throw new NoSuchElementException("No more characters in the input.");
        }
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
                    if (Character.isWhitespace(ch)) { // done
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
                    } else if (ch == '\\') { // done
                        state = States.IN_ESCAPED;
                        break;
                    } else { // done
                        value.append(ch);
                        break;
                    }
                }
                case IN_ESCAPED: {
                    value.append(ch);
                    state = States.IN_TERMINAL;
                    break;
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
    
    public void back() {
        useLastToken = true;        
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported.");
    }
    
    


}
