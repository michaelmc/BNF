package bnf;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

public class BNFTest {
    BNF parser;
    
    @Test
    public final void testBNF() {
        parser = new BNF();
        assertEquals(0, parser.rules.size());
        assertEquals(0,  parser.stack.size());
    }

    @Test
    public final void testRead() {
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= foo. <ont> ::= bar."));
        assertEquals(2, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<ont>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<ont>")).print();
        
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= foo | bar | what."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
        
        parser = new BNF();
        parser.read(new StringReader("<BNF> ::= { <rule> }. <rule> ::= <nonterminal> \\::= <definition> { \\| <definition> } \\. . <definition> ::= { <term> }.<term> ::= <terminal> | <nonterminal> | <option> | <any number of>. <option> ::= \\[ <definition> \\]. <any number of> ::= \\{ <definition> \\}."));
        assertEquals(6, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<BNF>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<rule>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<definition>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<term>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<option>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<any number of>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<BNF>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<rule>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<definition>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<term>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<option>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<any number of>")).print();
    }

    @Test
    public final void testMakeTree() {
        fail("Not yet implemented");
    }
    
    @Test
    public final void testGetStackItem() {
        fail("Not yet implemented");        
    }
    
    @Test
    public final void testNextTokenEquals() {
        fail("Not yet implemented");
    }

    @Test
    public final void testError() {
        fail("Not yet implemented");
    }
    
    @Test
    public final void testIsRule() {
        parser = new BNF();
        parser.read(new StringReader("<BNF> ::= { <rule> }. <rule> ::= <nonterminal> \\::= <definition> { \\| <definition> } \\. . <definition> ::= { <term> }.<term> ::= <terminal> | <nonterminal> | <option> | <any number of>. <option> ::= \\[ <definition> \\]. <any number of> ::= \\{ <definition> \\}."));
        assertEquals(6, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<BNF>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<rule>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<definition>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<term>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<option>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<any number of>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<BNF>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<rule>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<definition>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<term>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<option>")).print();
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<any number of>")).print();
    }

    /*
     * isDefinition() and isTerm() are tested implicitly in the above and below
     * tests. Because on their reliance on a stack and the rest of the
     * surrounding methods, it is impractical to test them directly. Term only
     * tests for one of isOption, isAnyNum, isTerminal, or isNonterminal, all
     * tested extensively below.
     * 
     * isDefinition tests for one or more terms, which are also extensively
     * implicitly tested above and below.
     * 
     */

    @Test
    public final void testIsOption() {
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= [ optional ]."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
        
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= [ { optional } ]."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
        
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= [ \\{ optional \\} ]."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();

        parser = new BNF();
        parser.read(new StringReader("<nt> ::= [ \\{ optional \\} ]"));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
    }

    @Test
    public final void testIsAnyNum() {
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= { anynum }."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();

        parser = new BNF();
        parser.read(new StringReader("<nt> ::= { { anynum } }."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();

    }

    @Test
    public final void testIsTerminal() {
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= foo."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
    }

    @Test
    public final void testIsNonterminal() {
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= <foo>."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
    }

    @Test
    public final void testWrite() {
        StringWriter stringy;
        
        parser = new BNF();
        parser.read(new StringReader("<BNF> ::= <rule>."));
        stringy = new StringWriter();
        parser.write(stringy);
        assertEquals("<BNF> ::= <rule> .\n", stringy.toString());
        
        parser = new BNF();
        parser.read(new StringReader("<BNF> ::= { <rule> }. <rule> ::= <nonterminal> \\::= <definition> { \\| <definition> } \\. . <definition> ::= { <term> }.<term> ::= <terminal> | <nonterminal> | <option> | <any number of>. <option> ::= \\[ <definition> \\]. <any number of> ::= \\{ <definition> \\}."));
        stringy = new StringWriter();
        parser.write(stringy);
        String shouldBe = "<definition> ::= { <term> } .\n<term> ::= <terminal> | <nonterminal> | <option> | <any number of> .\n<rule> ::= <nonterminal> \\::= <definition> { \\| <definition> } \\. .\n<BNF> ::= { <rule> } .\n<any number of> ::= \\{ <definition> \\} .\n<option> ::= \\[ <definition> \\] .\n";
        assertEquals(shouldBe, stringy.toString());
    }
    
    @Test
    public final void testWriteHelper() {
        
    }

    @Test
    public final void testLookUp() {
        parser = new BNF();
        parser.read(new StringReader("<BNF> ::= <rule>."));
        assertEquals(new Tree<Token>(new Token(TokenType.NONTERMINAL, "<rule>")), 
                parser.lookUp("BNF"));
        assertEquals(new Tree<Token>(new Token(TokenType.NONTERMINAL, "<rule>")), 
                parser.lookUp("BNF>"));
        assertEquals(new Tree<Token>(new Token(TokenType.NONTERMINAL, "<rule>")), 
                parser.lookUp("<BNF"));
        assertEquals(new Tree<Token>(new Token(TokenType.NONTERMINAL, "<rule>")), 
                parser.lookUp("<BNF>"));
        
        parser = new BNF();
        parser.read(new StringReader("<BNF> ::= { <rule> }. <rule> ::= <nonterminal> \\::= <definition> { \\| <definition> } \\. . <definition> ::= { <term> }.<term> ::= <terminal> | <nonterminal> | <option> | <any number of>. <option> ::= \\[ <definition> \\]. <any number of> ::= \\{ <definition> \\}."));
        assertEquals(1, parser.lookUp("<BNF>").getNumberOfChildren());
        assertEquals(5, parser.lookUp("rule").getNumberOfChildren());
        assertEquals(1, parser.lookUp("<definition>").getNumberOfChildren());
        assertEquals(4, parser.lookUp("<term").getNumberOfChildren());
        assertEquals(3, parser.lookUp("option>").getNumberOfChildren());
        assertEquals(3, parser.lookUp("any number of").getNumberOfChildren());
        assertEquals(null, parser.lookUp("foobar"));
    }
    
}
