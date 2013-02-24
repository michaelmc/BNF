package bnf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;

/**
 * A class to read a BNF grammar and generate 'programs' that comply with that
 * grammar. ProgramGenerator reads a file containing a complete BNF grammar and
 * then tokenizes and parses the grammar. Once complete, it can generate
 * the 'programs' by expanding the BNF grammar rules from a specified starting
 * point, and has the ability to print those 'programs' to the System.out 
 * channel.
 * 
 * Assignment 5, Part 2 in Prof. Dave Matuszek's CIT594 course, Spring 2013
 * 
 * Note that BNF is context-free, so the programs only comply with the grammar
 * but are likely pointless and unusable.
 * 
 * @author Michael McLaughlin, mvm@cis.upenn.edu
 * @version CIT594 Spring 2013, 2/25/2013
 */
/**
 * @author mvm
 *
 */
/**
 * @author mvm
 *
 */
/**
 * @author mvm
 *
 */
/**
 * @author mvm
 *
 */
/**
 * @author mvm
 *
 */
/**
 * @author mvm
 *
 */
public class ProgramGenerator {
    static BNF bnfParser = new BNF();
    static JFileChooser fileChooser = new JFileChooser();
    List<String> program;

    /**
     * Runs the ProgramGenerator. Either uses the specified path to open a file
     * containing a BNF grammar or will prompt the user to select a file if the
     * path is left blank.
     * 
     * Once a file is selected, will parse the file, print the entire grammar
     * to the Console, and print four randomly-generated 'programs' to the
     * console as well.
     * 
     * @param args Contains the path to the BNF grammar file or left blank.
     */
    public static void main(String[] args) {
        bnfParser = new BNF();
        if (args == null) {
            readFile();
        } else {
            readFile(args[0]);
        }
        StringWriter output = new StringWriter();
        bnfParser.write(output);
        System.out.println("Maximilian BNF Grammar:");
        System.out.print(output.toString());
        for (int i = 0; i < 4; i++) {
            System.out.println("\n\nRandomly Generated Program " + (i + 1) + ":");
            printList(expand("<program>"));
            System.out.println("\n");
        }
    }
   
    /**
     * Expands a given term to a set of terminals. If the term is the 
     * nonterminal of the highest-level rule of a BNF grammar (usually 
     * <program>), this will create a grammar-compliant 'program'.
     * 
     * If the given term is not found in the keys of nonterminals of the
     * current BNF grammar, it is treated as a terminal; terminals are simply
     * returned as a list of themselves.
     * 
     * Otherwise, works recursively to expand all not-terminals into terminals. 
     * 
     * @param term The term expand to a list of terminals.
     * @return A list of expanded terms.
     */
    public static List<String> expand(String term) {
        List<String> strings = new ArrayList<String>();
        List<String> newStrings = new ArrayList<String>();
        Tree<Token> rule = null;
        rule = bnfParser.lookUp(term);
        if (rule == null) {
            newStrings.add(term);
        } else {
            strings = expandTree(rule);
            for (int i = 0; i < strings.size(); i++) {
                String expander = strings.get(i);
                List<String> expanded = expand(expander);
                newStrings.addAll(expanded);
            }
        }
        return newStrings;
    }
    
    /**
     * Helper method for expand. Takes Tree objects and expands them 
     * appropriately based on their token type (e.g. one child of an OR is
     * selected; random number generation determines if an optional will be
     * used, etc). 
     * 
     * @param tree The Tree object to expand.
     * @return A list of expanded items.
     */
    public static List<String> expandTree(Tree<Token> tree) {
        Random rand = new Random();
        int roll;
        List<String> strings = new ArrayList<String>();
        if (tree.getValue().getType().equals(TokenType.TERMINAL)) {
            strings.add(tree.getValue().getValue());
        } else if (tree.getValue().getType().equals(TokenType.NONTERMINAL)) {
            strings.add(tree.getValue().getValue());
        } else if (tree.getValue().getType().equals(TokenType.ANYNUM)) {
            roll = rand.nextInt(5);
            if (roll > 0) {
                for (int i = 0; i < roll; i++) {
                    for (int j = 0; j < tree.getNumberOfChildren(); j++) {
                        strings.addAll(expandTree(tree.getChild(j)));
                    }
                }
            }
        } else if (tree.getValue().getType().equals(TokenType.OPTION)) {
            roll = rand.nextInt(1);
            if (roll % 2 == 1) {
                for (int j = 0; j < tree.getNumberOfChildren(); j++) {
                    strings.addAll(expandTree(tree.getChild(j)));
                }
            }
        } else if (tree.getValue().getType().equals(TokenType.OR)) {
            roll = rand.nextInt(tree.getNumberOfChildren());
            strings.addAll(expandTree(tree.getChild(roll)));
        } else if (tree.getValue().getType().equals(TokenType.SEQUENCE)) {
            for (int j = 0; j < tree.getNumberOfChildren(); j++) {
                strings.addAll(expandTree(tree.getChild(j)));
            }
        } else {
            throw new RuntimeException("Invalid token found.");
        }
        return strings;
    }
    
    /**
     * Prints a list to the Console. Meant to be used in conjunction with the
     * lists generated by the expand method, to allow the generated programs
     * to be viewed. 
     * 
     * Note that the method is tuned to my grammar (Maximilian... it's my dog),
     * so it inserts newlines when it finds end-of-statement semicolons. This
     * may not be desired behavior with other BNF grammars.
     * 
     * @param list The List object to print.
     */
    public static void printList(List<String> list) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(";")) {
                if (! list.get(i + 1).equals(";")) {
                    string.append(list.get(i) + "\n");
                }
            } else {
                string.append(list.get(i) + " ");
            }
        }
        System.out.print(string.toString());
    }
    
    /**
     * Handles reading a BNF grammar from a file. Prompts the user to choose a
     * file from a dialog box and puts the contents of the file into a new BNF
     * instance.
     * 
     * Generally used as a helper method for ProgramGenerator.main.
     * 
     */
    public static void readFile() {
        bnfParser = new BNF();
        FileReader fileContents = null;
        BufferedReader fileContentsB = null;
        int fileReturn = fileChooser.showOpenDialog(fileChooser);
        if (fileReturn == JFileChooser.APPROVE_OPTION) {
            File openFile = fileChooser.getSelectedFile();
            try {
                fileContents = new FileReader(openFile);
                fileContentsB = new BufferedReader(fileContents);
                bnfParser.read(fileContentsB);
                fileContents.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Handles reading a BNF grammar from a file. Uses the file specified in
     * the argument and puts the contents of the file into a new BNF instance.
     * 
     * Generally used as a helper method for ProgramGenerator.main.
     * 
     */
    public static void readFile(String path) {
        bnfParser = new BNF();
        FileReader fileContents = null;
        BufferedReader fileContentsB = null;
        try {
            fileContents = new FileReader(path);
            fileContentsB = new BufferedReader(fileContents);
            bnfParser.read(fileContentsB);
            fileContents.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new BNF instance from a string; only meant to be used during
     * JUnit testing and would be removed or made private otherwise.
     * 
     * @param rule The BNF rule string to parse.
     */
    static void readString(String rule) {
        bnfParser = new BNF();
        bnfParser.read(new StringReader(rule));
    }
}
