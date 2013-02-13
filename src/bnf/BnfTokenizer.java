/**
 * 
 */
package bnf;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author mvm
 *
 */
public class BnfTokenizer implements Iterator<Token> {
    private BufferedReader input;
    private int position;
    private enum States { READY, IN_TERMINAL, IN_NONTERMINAL, IN_DEFINED, ERROR };

    /**
     * @param reader
     */
    public BnfTokenizer(java.io.Reader reader) {
        BufferedReader input = new BufferedReader(reader);
        position = -1;
    }

    @Override
    public boolean hasNext() {
        try {
            input.mark(10);
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
        int readInt = -1;
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
                        return new Token(TokenType.METASYMBOL, value);
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
                        return new Token(TokenType.ERROR, value);
                    }
                    else {
                        state = States.IN_TERMINAL;
                        break;
                    }
                }
                case IN_TERMINAL: {}
                case IN_NONTERMINAL: {
                    if (value == "<") {
                        value += ch;
                        if (ch == '>' || ch == '\n') {
                            return new Token(TokenType.ERROR, value);
                        } else {
                            break;
                        }
                    } else {
                        value += ch;
                        if (ch == '\n') {
                            return new Token(TokenType.ERROR, value);
                        } else if (ch == '>') {
                            return new Token(TokenType.NONTERMINAL, value);
                        } else {
                            break;
                        }
                    }
                }
                case IN_DEFINED: { // TODO test this
                    value += ch;
                    if (value == "::") {
                        break;
                    } else if (value == "::=") {
                        return new Token(TokenType.METASYMBOL, value);
                    } else {
                        state = States.IN_TERMINAL;
                        break;
                    }
                }
                default: {
                    return new Token(TokenType.ERROR, value);
                }
            }
        } while (hasNext());
        return null;
    }
    
    public void back() {
        
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported.");
    }

}
