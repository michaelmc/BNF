package bnf;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version CIT594 Spring 2013
 */
public class BNF {
    private enum States { READY, IN_KEY, IN_DEFINED, IN_SEQUENCE, IN_ANYNUM, IN_OPTION };
    HashMap<Token, Tree<Token>> rules;
    BnfTokenizer rulesTokenizer;

    /**
     * 
     */
    public BNF() {
        this.rules = new HashMap<Token, Tree<Token>>();
    }
    
    /**
     * @param reader
     */
    public void read(Reader reader) {
        rulesTokenizer = new BnfTokenizer(reader);
        Token currentKey = null;
        Tree<Token> currentRule = null;
        Token currentToken = null;
        States state;
        state = States.READY;
        while (rulesTokenizer.hasNext()) {
            currentToken = rulesTokenizer.next();
            switch (state) {
            case READY:
                if (currentKey == null) {
                    currentKey = currentToken;
                    break;
                } else {
                    if (currentToken.getValue() != "::=") {
                        throw new IllegalStateException("Assignment statement not follows be \"defined as\" operator.");
                    }
                    state = States.IN_SEQUENCE;
                    break;
                }
            case IN_SEQUENCE:
                break;
            case IN_ANYNUM:
                break;
            case IN_DEFINED:
                break;
            case IN_KEY:
                break;
            case IN_OPTION:
                break;
            default:
                break;
            }
        }
    }
    
    /**
     * @param writer
     */
    public void write(Writer writer) {
        
    }
    
    /**
     * @param nonterminal
     * @return
     */
    public Tree<Token> lookUp(String nonterminal) {
        return rules.get(new Token(TokenType.NONTERMINAL, nonterminal));
    }
    
    /**
     * @author mvm
     *
     * @param <T>
     */
    public class Tree<T> {
        T value;
        ArrayList<Tree<T>> children;
        
        public Tree(T token) {
            value = token;
            children = new ArrayList<Tree<T>>();
        }
        
        public T getValue() {
            return value;
        }
        
        public void addChild(int index, Tree<T> child) {
            children.add(index, child);
        }
        
        @SuppressWarnings("unchecked")
        public void addChildren(Tree<T>... children) {
            for (int i = 0; i < children.length; i++) {
                addChild(i + children.length, children[0]);
            }
        }
    }
}
