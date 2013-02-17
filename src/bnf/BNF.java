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
    HashMap<Token, Tree<Token>> rules;

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
        BnfTokenizer rulesTokenizer = new BnfTokenizer(reader);
        Token currentToken = null;
        while (rulesTokenizer.hasNext()) {
            // read in the rules one by one
            currentToken = rulesTokenizer.next();
            
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
