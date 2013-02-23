package bnf;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BNFTest {
    BNF parser;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        parser = new BNF();
    }

    @Test
    public final void testBNF() {
        fail("Not yet implemented");
    }

    @Test
    public final void testRead() {
//        fail("Not yet implemented");
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
    }

    @Test
    public final void testMakeTree() {
        fail("Not yet implemented");
    }

    @Test
    public final void testIsRule() {
        fail("Not yet implemented");
    }

    @Test
    public final void testIsDefinition() {
        fail("Not yet implemented");
    }

    @Test
    public final void testIsTerm() {
        fail("Not yet implemented");
    }

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
        fail("Not yet implemented");
    }

    @Test
    public final void testLookUp() {
        fail("Not yet implemented");
    }
    
}
