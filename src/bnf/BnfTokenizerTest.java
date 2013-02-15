package bnf;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class BnfTokenizerTest {
    BnfTokenizer tokenizerOne;
    BnfTokenizer tokenizerTerminal;
    BnfTokenizer tokenizerEmpty;
    BnfTokenizer tokenizerNonterminal;
    BnfTokenizer tokenizerNotClosedNonterminal;

    @Before
    public void setUp() throws Exception {
        tokenizerEmpty = new BnfTokenizer(new StringReader(""));
        tokenizerOne = new BnfTokenizer(new StringReader("<nonterminal> <some nonterminal> ::= terminal"));
        tokenizerTerminal = new BnfTokenizer(new StringReader("terminal"));
        tokenizerNonterminal = new BnfTokenizer(new StringReader("<non term-inal>"));
        tokenizerNotClosedNonterminal = new BnfTokenizer(new StringReader("<nottermina lwhat"));
    }

    @Test
    public void testBnfTokenizer() {
        fail("Not yet implemented");
    }

    @Test
    public void testHasNext() {
        assertFalse(tokenizerEmpty.hasNext());
        assertTrue(tokenizerOne.hasNext());
    }

    @Test
    public void testNext() {
        assertTrue(tokenizerTerminal.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "terminal"), tokenizerTerminal.next());
        assertFalse(tokenizerTerminal.hasNext());
        assertTrue(tokenizerNonterminal.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<non term-inal>"), tokenizerNonterminal.next());
        assertFalse(tokenizerNonterminal.hasNext());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nonterminal>"), tokenizerOne.next());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<some nonterminal>"), tokenizerOne.next());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), tokenizerOne.next());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "terminal"), tokenizerOne.next()); //XXX this is where it fails
        assertFalse(tokenizerOne.hasNext());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNextNotClosedNT() {
        assertTrue(tokenizerNotClosedNonterminal.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nottermina lwhat"), tokenizerNotClosedNonterminal.next());
        assertFalse(tokenizerNotClosedNonterminal.hasNext());
    }
    
    @Test
    public void testBack() {
        fail("Not yet implemented");
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testRemove() {
        tokenizerOne.remove();
    }

}