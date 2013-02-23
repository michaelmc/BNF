package bnf;

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
    Token currentKey;
    /**
     * 
     */
    public BNF() {
        this.rules = new HashMap<Token, Tree<Token>>();
        this.stack = new Stack<Tree<Token>>();
        this.currentToken = null;
        this.currentKey = null;
    }
    
    /**
     * @param reader
     */
    public void read(java.io.Reader reader) {
        rulesTokenizer = new BnfTokenizer(reader);
        while (rulesTokenizer.hasNext()) {
//            System.out.println(rulesTokenizer.next().toString());
            try {
                boolean success = isRule();
                System.out.println("Parsed okay?  " + success);
//                System.out.println(stack.peek());
//                stack.peek().print();
            } catch (RuntimeException e) {
                System.out.println("***Runtime*** " + e.getMessage());
            } catch (Exception e) {
                System.out.println("***Input*** " + e.getMessage());
            }
        }
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
    
    public boolean nextTokenEquals(String value) {
        if (! rulesTokenizer.hasNext() && ! rulesTokenizer.useLastToken) {
            return false;
        }                
        Token t = rulesTokenizer.next();
        if (t.getValue().equals(value)) {
            stack.push(new Tree<Token>(t));
            return true;
        }
        rulesTokenizer.back();
        return false;
    }
    public boolean nextTokenEquals(String value, TokenType type) {
        if (! rulesTokenizer.hasNext() && ! rulesTokenizer.useLastToken) {
            return false;
        }                
        Token t = rulesTokenizer.next();
        if (t.getValue().equals(value) && t.getType().equals(type)) {
            stack.push(new Tree<Token>(t));
            return true;
        }
        rulesTokenizer.back();
        return false;
    }
    
    public void error(String message) {
        throw new RuntimeException(message);
    }
    
    public boolean isRule() {
        if (isNonterminal()) {
            currentKey = stack.pop().getValue();
            if (nextTokenEquals("::=")) {
                stack.pop();
                if (isDefinition()) {
                    makeTree(1);
                } 
                while (! nextTokenEquals(".") && rulesTokenizer.hasNext()) {
                    if (nextTokenEquals("|")) {
                        stack.pop();
                        if (isDefinition()) {
                            stack.push(new Tree<Token>(new Token(TokenType.OR, "OR")));
                            makeTree(1, 3, 2);
                        } else {
                            error ("No definition followed a '|'");
                        }
                        while (nextTokenEquals("|")) {
                            stack.pop();
                            if (isDefinition()) {
                                makeTree(2, 1);
                            } else {
                                error("No definition followed a '|'");
                            }
                        }
                    } else {
                        if (isDefinition()) {
                            // what
                        }
                    }
                }
                if (stack.peek().getValue().equals(".")) stack.pop();
                rules.put(currentKey, stack.pop());
                return true;
            }
            error("Rule doesn't contain a '::=' in the second position.");
        }
        return false;
    }
    
    public boolean isDefinition() {
        if (isTerm()) {
            if (isTerm()) {
                stack.push(new Tree<Token>(new Token(TokenType.SEQUENCE, "SEQUENCE")));
                makeTree(1, 3, 2);
                while (isTerm()) {
                    makeTree(2, 1);
                }
            }
            return true;
        }
        return false;
    }
    
    public boolean isTerm() {
        if (isOption()) {
            return true;
        }
        if (isAnyNum()) {
            return true;
        }
        if (isTerminal()) {
            return true;
        }
        if (isNonterminal()) {
            return true;
        }
        return false;
    }
    
    public boolean isOption() {
        if (nextTokenEquals("[", TokenType.METASYMBOL)) {
            stack.pop();
            stack.push(new Tree<Token>(new Token(TokenType.OPTION, "OPTION")));
            if (! isDefinition()) error("No definition following '[");
            if (! nextTokenEquals("]")) error("No closing bracket.");
            stack.pop();
            makeTree(2, 1);
            return true;
        }
        return false;
    }
    
    public boolean isAnyNum() {
        if (nextTokenEquals("{", TokenType.METASYMBOL)) {
            stack.pop();
            stack.push(new Tree<Token>(new Token(TokenType.ANYNUM, "ANYNUM")));
            if (! isDefinition()) error("No definition following '{");
            if (! nextTokenEquals("}")) error("No closing brace.");
            stack.pop();
            makeTree(2, 1);
            return true;
        }
        return false;
    }
//    
//    public boolean isChoice() {
//        return false;
//    }
    
    public boolean isTerminal() {
        if (! rulesTokenizer.hasNext() && ! rulesTokenizer.useLastToken) {
            return false;
        }                
        Token t = rulesTokenizer.next();
        if (t.getType() == TokenType.TERMINAL) {
            stack.add(new Tree<Token>(t));
            return true;
        } else {
            rulesTokenizer.back();
            return false;
        }
    }
    
    public boolean isNonterminal() {
        if (! rulesTokenizer.hasNext() && ! rulesTokenizer.useLastToken) {
            return false;
        }                
        Token t = rulesTokenizer.next();
        if (t.getType() == TokenType.NONTERMINAL) {
            stack.push(new Tree<Token>(t));
            return true;
        }
        rulesTokenizer.back();
        return false;
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
        
        @Override
        public String toString() {
            String s = value.toString();
            if (children.size() > 0) {
                s += "(";
                for (Tree<T> child : children) {
                    s += child.toString() + " ";
                }
                s = s.substring(0, s.length() - 1) + ")";
            }
            return s;
        }
        
        public void print() {
            this.printHelper(1);
        }

        void printHelper(int level) {
            System.out.println(this.value + "");
            if (this.children.size() > 0) {
                for (int i = 0; i < this.children.size(); i++) {
                    for (int j = 0; j < level; j++) {
                        System.out.print("|  ");
                    }
                    children.get(i).printHelper(level + 1);
                }
            }
        }

    }
}
