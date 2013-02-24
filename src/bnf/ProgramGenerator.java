/**
 * 
 */
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
 * @author mvm
 *
 */
public class ProgramGenerator {
    static BNF bnfParser = new BNF();
    static JFileChooser fileChooser = new JFileChooser();
    List<String> program;

    /**
     * @param args
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
   
    public static void run() {
        bnfParser = new BNF();
        readFile();
    }

    public static List<String> expand(String term) {
//        List<String> strings = new ArrayList<String>();
//        rule = bnfParser.lookUp(term);
//        if (rule == null) {
//            strings.add(term);
//        } else {
//
//        }
//        return strings
//        
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
//                for (int j = 0; j < expanded.size(); j++) {
//                    newStrings.addAll(expand(expanded.get(j)));
//                }
            }
        }
        return newStrings;
    }
    
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

    static void readString(String rule) {
        bnfParser = new BNF();
        bnfParser.read(new StringReader(rule));
    }
}
