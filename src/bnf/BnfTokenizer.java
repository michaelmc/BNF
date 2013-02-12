/**
 * 
 */
package bnf;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author mvm
 *
 */
public class BnfTokenizer implements Iterator<Token> {
    private String input;
    private int position;
    private enum States { READY, IN_TERMINAL, IN_NONTERMINAL, IN_METASYMBOL, ERROR };

    /**
     * @param reader
     */
    public BnfTokenizer(java.io.Reader reader) {
        BufferedReader readerInput = new BufferedReader(reader);
        String readerInputLine = null;
        StringBuilder wholeInput = new StringBuilder();
        try {
            while ((readerInputLine = readerInput.readLine()) != null) {
                wholeInput.append(readerInputLine);
            }
            System.out.println(wholeInput.toString());
        } catch (IOException e) {
            System.out.println("IOException when trying to read input.");
        }
        position = -1;
    }

    @Override
    public boolean hasNext() {
        return position < input.length() - 2;
    }

    @Override
    public Token next() {
        States state;
        String value = "";
        if (!hasNext()) {
            throw new IllegalStateException("No more tokens.");
        }
        state = States.READY;
        while ((++position) < input.length()) {
            char ch = input.charAt(position);
            switch (state) {
                case READY: {
                    value = ch + "";
                }
                case IN_TERMINAL: {}
                case IN_NONTERMINAL: {}
                case IN_METASYMBOL: {}
                default: {
                    return new Token(TokenType.ERROR, value);
                }
            }
        }
        assert false;
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported.");
    }

}
