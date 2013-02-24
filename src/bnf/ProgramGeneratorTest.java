package bnf;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ProgramGeneratorTest {

    @Test
    public final void testMain() {
        String[] source = new String[] {"src/maximilian_language.txt"};
        ProgramGenerator.main(source);
    }

    @Test
    public final void testExpand() {
        assertEquals("one", ProgramGenerator.expand("one").get(0));
        assertEquals(1, ProgramGenerator.expand("one").size());
        
        ProgramGenerator.readString("<BNF> ::= trees");
        assertEquals("trees", ProgramGenerator.expand("<BNF>").get(0));
        assertEquals(1, ProgramGenerator.expand("<BNF>").size());
        
        ProgramGenerator.readString("<nt> ::= <trees> <leaves>. <trees> ::= bark . <leaves> ::= twig | berry .");
        List<String> nt = ProgramGenerator.expand("nt");
        assertEquals(2, nt.size());
        assertEquals("bark", nt.get(0));
        assertTrue(nt.get(1).equals("twig") || nt.get(1).equals("berry"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testExpandTree() {
        Tree<Token> root1 = new Tree<Token>(new Token(TokenType.OR, "OR"));
        Tree<Token> child11 = new Tree<Token>(new Token(TokenType.TERMINAL, "one"));
        Tree<Token> child12 = new Tree<Token>(new Token(TokenType.TERMINAL, "two"));
        Tree<Token> child13 = new Tree<Token>(new Token(TokenType.TERMINAL, "three"));
        Tree<Token> child14 = new Tree<Token>(new Token(TokenType.TERMINAL, "four"));
        root1.addChildren(child11, child12, child13, child14);
        assertTrue("one two three four".contains(ProgramGenerator.expandTree(root1).get(0)));
        
        Tree<Token> root2 = new Tree<Token>(new Token(TokenType.NONTERMINAL, "<foo>"));
        assertEquals("<foo>", ProgramGenerator.expandTree(root2).get(0));
        
        Tree<Token> root3 = new Tree<Token>(new Token(TokenType.TERMINAL, "bar"));
        assertEquals("bar", ProgramGenerator.expandTree(root3).get(0));
       
        Tree<Token> root4 = new Tree<Token>(new Token(TokenType.SEQUENCE, "SEQUENCE"));
        Tree<Token> child41 = new Tree<Token>(new Token(TokenType.TERMINAL, "one"));
        Tree<Token> child42 = new Tree<Token>(new Token(TokenType.TERMINAL, "two"));
        Tree<Token> child43 = new Tree<Token>(new Token(TokenType.TERMINAL, "three"));
        root4.addChildren(child41, child42, child43);
        List<String> list4 = ProgramGenerator.expandTree(root4);
        String string4 = "";
        for (int i = 0; i < list4.size(); i++) {
            string4 = string4 + list4.get(i) + " ";
        }        
        assertEquals("one two three ", string4);
        
        Tree<Token> root5 = new Tree<Token>(new Token(TokenType.OPTION, "OPTION"));
        Tree<Token> child51 = new Tree<Token>(new Token(TokenType.TERMINAL, "one"));
        root5.addChild(0, child51);
        List<String> list5 = ProgramGenerator.expandTree(root5);
        String string5 = "";
        for (int i = 0; i < list5.size(); i++) {
            string5 = string5 + list5.get(i);
        }
        assertTrue(string5.equals("one") || string5.equals(""));
        
        Tree<Token> root6 = new Tree<Token>(new Token(TokenType.ANYNUM, "ANYNUM"));
        Tree<Token> child61 = new Tree<Token>(new Token(TokenType.TERMINAL, "one"));
        root6.addChild(0, child61);
        List<String> list6 = ProgramGenerator.expandTree(root6);
        String string6 = "";
        for (int i = 0; i < list6.size(); i++) {
            string6 = string6 + list6.get(i);
        }
        assertTrue(string6.contains("one") || string6.equals(""));
        assertTrue(string6.length() % 3 == 0 || string6.length() == 0);
    }

    @Test
    public final void testPrintList() {
        /* 
         * These test methods are commented out to not pollute the console
         * during unit testing (this is an output-only method) but can be 
         * uncommented as seen fit for checking.
         */
//        ProgramGenerator.readString("<nt> ::= <trees> <leaves>. <trees> ::= bark . <leaves> ::= twig | berry .");
//        List<String> nt = ProgramGenerator.expand("nt");
//        ProgramGenerator.printList(nt);
//        System.out.println("\n");
//        
//        ProgramGenerator.readString("<program> ::= <input> <processing> <output> ;; .\n" + 
//        		"<input> ::= read <var> { , <var> } ; .\n" + 
//        		"<output> ::= write <var> { & <var> } .\n" + 
//        		"<processing> ::= <action> { <action> } .\n" + 
//        		"<action> ::= <loop> | <operation> .\n" + 
//        		"<loop> ::= <if statement> | <while loop> .\n" + 
//        		"<if statement> ::= if <condition> then <operation> { elif <condition> then <operation> } [ else <operation> ] ; .\n" + 
//        		"<while loop> ::= while <condition> do <action> ; .\n" + 
//        		"<operation> ::= <assignment> | <calc> .\n" + 
//        		"<calc> ::= <term> <operator> <term> ; .\n" + 
//        		"<assignment> ::= <var> := <term>; . \n" + 
//        		"<condition> ::= [ not ] <term> = <term> .\n" + 
//        		"<operator> ::= + | - | * | / | % .\n" + 
//        		"<term> ::= <var> | <int> .\n" + 
//        		"<var> ::= a | b | c | d | e | f | g | h | i | j .\n" + 
//        		"<int> ::= 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 0 .");
//        List<String> max = ProgramGenerator.expand("<program>");
//        ProgramGenerator.printList(max);
    }
}
