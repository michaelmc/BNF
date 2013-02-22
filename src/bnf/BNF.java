package bnf;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version CIT594 Spring 2013
 */
public class BNF {
    HashMap<Token, Tree<Token>> rules;
    BnfTokenizer rulesTokenizer;
    Stack<Tree<Token>> stack;
    Token currentToken;

    /**
     * 
     */
    public BNF() {
        this.rules = new HashMap<Token, Tree<Token>>();
        this.stack = new Stack<Tree<Token>>();
        this.currentToken = null;
    }
    
    /**
     * @param reader
     */
    public void read(Reader reader) {
        rulesTokenizer = new BnfTokenizer(reader);
//        Token currentKey = null;
//        Tree<Token> currentRule = null;
//        while (rulesTokenizer.hasNext()) {
//            currentToken = rulesTokenizer.next();
//            
//        }
    }
    
    public void makeTree(int rootIndex, int...childIndices) {
        Tree<Token> root = getStackItem(rootIndex);
        for (int i = 0; i < childIndices.length; i++) {
            root.addChild(getStackItem(childIndices[i]));
        }
        for (int i = 0; i <= childIndices.length; i++) {
            stack.pop();
        }
        stack.push(root);
    }
    
    private Tree<Token> getStackItem(int n) {
        return stack.get(stack.size() - n);
    }
    
    public boolean isRule() {
        if (isNonterminal()) {
            if (currentToken.getValue() == "::=" & 
                    currentToken.getType() == TokenType.METASYMBOL) {
                if (isDefinition()) {
                    
                }
                while (isDefinition()) {
                    
                }
            }
        }
        return false;
    }
    
    public boolean isDefinition() {
        if (!isTerm()) {
            rulesTokenizer.back();
            currentToken = null;
            return false;
        } else {
            stack.add(new Tree<Token>(currentToken));
            // make tree?
        }
        while (isTerm()) {
            stack.add(new Tree<Token>(currentToken));
            // make tree?
        }
        return true;
    }
    
    // TODO something circular going on here.
    public boolean isTerm() {
        if (isOption() || isAnyNum() || isTerminal() || isNonterminal()) {
            System.out.println(currentToken.getValue());
            return true;
        }
        return false;
    }
    
    public boolean isOption() {
        currentToken = rulesTokenizer.next();
        if (currentToken.getValue().equals("[")) {
            if (isDefinition()) {
                currentToken = rulesTokenizer.next();
                if (currentToken.getValue().equals("]")) {
                    stack.add(new Tree<Token>(currentToken));
                    // make tree here;
                    return true;
                } else {
                    throw new IllegalArgumentException("No closing bracket for Optional clause.");
                }
            } else {
                throw new IllegalArgumentException("Optional clause doesn't contain a Definition.");
            }
        }
        rulesTokenizer.back();
        currentToken = null;
        return false;
    }
    
    public boolean isAnyNum() {
        currentToken = rulesTokenizer.next();
        if (currentToken.getValue().equals("{")) {
            if (isDefinition()) {
                currentToken = rulesTokenizer.next();
                if (currentToken.getValue().equals("}")) {
                    stack.add(new Tree<Token>(currentToken));
                    // make tree here;
                    return true;
                } else {
                    throw new IllegalArgumentException("No closing brace for Any Number clause.");
                }
            } else {
                throw new IllegalArgumentException("Any Number clause doesn't contain a Definition.");
            }
        }
        rulesTokenizer.back();
        currentToken = null;
        return false;
    }
    
    public boolean isTerminal() {
        currentToken = rulesTokenizer.next();
        if (currentToken.getType() == TokenType.TERMINAL) {
            stack.add(new Tree<Token>(currentToken));
            return true;
        } else {
            rulesTokenizer.back();
            currentToken = null;
            return false;
        }
    }
    
    public boolean isNonterminal() {
        currentToken = rulesTokenizer.next();
        if (currentToken.getType() == TokenType.NONTERMINAL) {
            stack.add(new Tree<Token>(currentToken));
            return true;
        } else {
            rulesTokenizer.back();
            currentToken = null;
            return false;
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
        public void addChild(Tree<T> child) {
            children.add(children.size(), child);
        }
        
        @SuppressWarnings("unchecked")
        public void addChildren(Tree<T>... children) {
            for (int i = 0; i < children.length; i++) {
                addChild(i + children.length, children[0]);
            }
        }
    }
}
