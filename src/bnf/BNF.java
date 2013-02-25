package bnf;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;

/**
 * A parsing tool for BNF grammar. This class reads in a BNF grammar describing
 * a language and parses the grammar into a set of grammar rules. Once stored, 
 * a user can look up rules by their nonterminal key and can write the entire
 * grammar to string, file, etc.
 *
 * Meant to be used in conjunction with the BnfTokenizer and ProgramGenerator
 * classes.
 * 
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version CIT594 Spring 2013, 2/25/2013
 */
public class BNF {
    HashMap<Token, Tree<Token>> rules;
    BnfTokenizer rulesTokenizer;
    Stack<Tree<Token>> stack;
    Token currentKey;
    StringBuilder string;

    /**
     * Constructor for the BNF class. Creates an empty instance.
     */
    public BNF() {
        this.rules = new HashMap<Token, Tree<Token>>();
        this.stack = new Stack<Tree<Token>>();
        this.currentKey = null;
        this.string = null;
    }
    
    /**
     * Reads a source specified by the argument, tokenizes it, and parses the 
     * entire set of tokens at once. The source must comply with Extended 
     * Backus-Naur Form grammar; particularly each rule must be of the form:
     * 
     * <pre>&lt;nonterminal&gt;</pre> ::= <i>the definition of the nonterminal</i> .
     * 
     * Rules missing a period or definition symbol (::=) will cause errors in 
     * the parsing of the grammar.
     * 
     * This method accepts any Reader object as input, so files, strings, and
     * other sources may all be used as input.
     * 
     * @param reader The reader object containing EBNF to parse.
     */
    public void read(java.io.Reader reader) {
        rulesTokenizer = new BnfTokenizer(reader);
        while (rulesTokenizer.hasNext()) {
            try {
                isRule();
            } catch (RuntimeException e) {
                System.out.println("***Runtime*** " + e.getMessage());
                throw new RuntimeException("Read ending: fix input and try again.");
            } catch (Exception e) {
                System.out.println("***Input*** " + e.getMessage());
                throw new RuntimeException("Read ending: fix input and try again.");
            }
        }
    }
    
    
    /**
     * Creates a tree from other trees stored on a stack in a BNF instance. The
     * first argument designates the root node; subsequent arguments are
     * children of the root added in left-to-right order. The trees are removed
     * from the stack while building the new tree and only the new tree is 
     * returned to the stack at the end. 
     * 
     * Borrowed from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @param rootIndex The position of the root on the stack (top = 1).
     * @param childIndices The positions of the children on the stack (top = 1).
     */
    public void makeTree(int rootIndex, int...childIndices) {
        Tree<Token> root = getStackItem(rootIndex);
        for (int i = 0; i < childIndices.length; i++) {
            root.addChild(root.getNumberOfChildren(), getStackItem(childIndices[i]));
        }
        for (int i = 0; i <= childIndices.length; i++) {
            stack.pop();
        }
        stack.push(root);
    }
    
    /**
     * Gets items from a stack from their position rather than by index.
     * Top item is 1, bottom item is the number of items on the stack. Items
     * are not removed from the stack when retrieved.
     * 
     * Borrowed from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @param n The position of the item to get from the stack.
     * @return The Tree from that position on the stack.
     */
    Tree<Token> getStackItem(int n) {
        return stack.get(stack.size() - n);
    }
    
    /**
     * Checks the next token to see if it's the expected one. If so, a tree
     * containing the token is placed on the stack; otherwise, the tokenizer
     * is set to return that token again on the next call.
     * 
     * Adapted from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @param value The value to test for equality.
     * @return true if the value matches.
     */
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
    /**
     * Checks the next token to see if it's the expected one and has the
     * expected type. If so, a tree containing the token is placed on the 
     * stack; otherwise, the tokenizer is set to return that token again on
     * the next call.
     * 
     * Adapted from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @param value The value to test for equality.
     * @param type The type to test for equality.
     * @return true if the value matches.
     */
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
    
    /**
     * A helper method that throws an error with the provided message.
     * 
     * Adapted from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @param message The error message to send with the exception.
     */
    public void error(String message) {
        throw new RuntimeException(message);
    }
    
    /**
     * Checks a set of tokens in sequence to determine whether they comprise 
     * a valid BNF rule of the form:
     * 
     * <pre>&lt;nonterminal&gt;</pre> ::= <i>definition(s)</i> .
     * 
     * Each rule must begin with a nonterminal followed by a '::=' and a valid
     * definition. After the definition, the rule may contain alternating pipes
     * and definitions, provided a pipe is always followed by a definition and
     * that the entire phrase is followed by a period.
     * 
     * If any condition is not met, an error is thrown with an appropriate 
     * error message.
     * 
     * If a valid rule is found, it is added to the set of valid rules stored
     * in this instance with the initial nonterminal as the key and the 
     * definition as the value.
     * 
     * Inspiration from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @return true if a valid rule is found.
     */
    public boolean isRule() {
        if (isNonterminal()) {
            currentKey = stack.pop().getValue();
            if (nextTokenEquals("::=")) {
                stack.pop();
                if (isDefinition()) {
                    makeTree(1);
                } 
                while (! nextTokenEquals(".") && rulesTokenizer.hasNext()) {
                    if (nextTokenEquals("::=", TokenType.METASYMBOL)) error("Misplaced '::='");
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
                    } else if (nextTokenEquals("::=", TokenType.METASYMBOL)) { 
                        error("Misplaced '::='");
//                    } else {
//                        if (isDefinition()) {
//                            // what TODO
//                        }
                    }
                }
                if (stack.peek().getValue().getValue().equals(".")) stack.pop();
                rules.put(currentKey, stack.pop());
                return true;
            }
            error("Rule doesn't contain a '::=' in the second position.");
        }
        return false;
    }
    
    /**
     * Checks a set of tokens in sequence to determine whether they comprise 
     * a valid BNF Definition.
     * 
     * Each Definition must contain one or more valid Terms.
     * 
     * If any condition is not met, an error is thrown with an appropriate 
     * error message.
     * 
     * If a valid Definition is found, an appropriate Tree is constructed and
     * held to be added to the set of rules by the isRule() function.
     * 
     * Inspiration from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @return true if the tokens comprise a valid Definition.
     */
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
    
    /**
     * Checks a set of tokens in sequence to determine whether they comprise 
     * a valid BNF Term.
     * 
     * Each Term must be either a valid Optional, Any Number Of,
     * Terminal, or Nonterminal.
     * 
     * Inspiration from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @return true if the tokens comprise a valid Term.
     */
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
    
    /**
     * Checks a set of tokens in sequence to determine whether they comprise 
     * a valid BNF Optional.
     * 
     * Each Optional must contain a valid definition contained in brackets.

     * If the first bracket is not found, nothing is done and the tokenizer is 
     * set to return the first token again. If any other condition is not met,
     * an error is thrown with an appropriate error message.
     * 
     * If a valid Optional is found, an appropriate Tree is constructed and
     * held to be added to the set of rules by the isRule() function.
     * 
     * Inspiration from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @return true if the tokens comprise a valid Optional.
     */
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
    
    /**
     * Checks a set of tokens in sequence to determine whether they comprise 
     * a valid BNF Any Number Of.
     * 
     * Each Any Number Of must contain a valid definition contained in braces.
     * 
     * If the first brace is not found, nothing is done and the tokenizer is 
     * set to return the first token again. If any other condition is not met,
     * an error is thrown with an appropriate error message.
     * 
     * If a valid Any Number Of is found, an appropriate Tree is constructed
     * and held to be added to the set of rules by the isRule() function.
     * 
     * Inspiration from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @return true if the tokens comprise a valid Any Number Of.
     */
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
    
    /**
     * Checks a Token to determine whether it is a valid BNF Terminal.
     *
     * If it is, it is added to the stack to assist in constructing the rule.
     * Otherwise, nothing is done, and the tokenizer is set to return that 
     * token again.
     * 
     * Inspiration from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @return true if the token is a Terminal.
     */
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
    
    /**
     * Checks a Token to determine whether it is a valid BNF Nonterminal.
     *
     * If it is, it is added to the stack to assist in constructing the rule.
     * Otherwise, nothing is done, and the tokenizer is set to return that 
     * token again.
     * 
     * Inspiration from slides and example code written provided by 
     * Prof. Dave Matuszek in CIT594.
     * 
     * @return true if the token is a Nonterminal.
     */
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
     * Writes the entire stored BNF grammar to a specified Writer object. 
     * 
     * Each rule is written to the Writer in valid BNF of a form that it can
     * be read in to the parser again. The rules are not written in a specific
     * order, so there is no way to ensure output order, but all rules are 
     * written each time.
     * 
     * @param writer The Writer object to write the grammar to.
     */
    public void write(Writer writer) {
        string = new StringBuilder();
        Iterator<Entry<Token, Tree<Token>>> entries = rules.entrySet().iterator();
        Entry<Token, Tree<Token>> entry = null;
        while (entries.hasNext()) {
            entry = entries.next();
            string.append(entry.getKey().getValue() + " ::= ");
            writeHelper(entry.getValue());
            string.append(".\n");
        }
        try {
            writer.append(string.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(string.toString());
    }
    
    /**
     * Helper function for write(Writer). Works recursively through the rule
     * tree expanding each type of BNF token appropriately and adding correct
     * metasymbols to the token values so they are valid BNF rules when
     * written.
     * 
     * @param tree The tree to read and expand for writing.
     */
    void writeHelper(Tree<Token> tree) {
        if (tree.getValue().getType() == TokenType.SEQUENCE) {
            for (int i = 0; i < tree.getNumberOfChildren(); i++) {
                writeHelper(tree.getChild(i));
            }
        } else if (tree.getValue().getType() == TokenType.OR) {
            for (int i = 0; i < tree.getNumberOfChildren(); i++) {
                writeHelper(tree.getChild(i));
                string.append("| ");
            }
            string.deleteCharAt(string.length() - 1);
            string.deleteCharAt(string.length() - 1);
        } else if (tree.getValue().getType() == TokenType.ANYNUM) {
            string.append("{ ");
            for (int i = 0; i < tree.getNumberOfChildren(); i++) {
                writeHelper(tree.getChild(i));
            }
            string.append("} ");
        } else if (tree.getValue().getType() == TokenType.OPTION) {
            string.append("[ ");
            for (int i = 0; i < tree.getNumberOfChildren(); i++) {
                writeHelper(tree.getChild(i));
            }
            string.append("] ");
        } else {
            String val = tree.getValue().getValue();
            if (".|[]{}".contains(val)) {
                string.append("\\" + val + " ");
            } else if ("::=".equals(val)) {
                string.append("\\::= ");
            } else {
                string.append(val + " ");
            }
        }
    }
    
    /**
     * Looks up a rule by its nonterminal key and returns the Tree containing
     * the rule's definition.
     * 
     * Angle brackets are optional when specifying the nonterminal: they will
     * be added if they are not present when calling the function.
     * 
     * Returns null if the nonterminal is not found in the list of keys.
     * 
     * @param nonterminal The nonterminal key to look up.
     * @return The value corresponding to that key.
     */
    public Tree<Token> lookUp(String nonterminal) {
        if (!nonterminal.startsWith("<")) nonterminal = "<" + nonterminal;
        if (!nonterminal.endsWith(">")) nonterminal += ">";
        return rules.get(new Token(TokenType.NONTERMINAL, nonterminal));
    }
}
