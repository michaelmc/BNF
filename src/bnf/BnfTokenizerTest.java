package bnf;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class BnfTokenizerTest {
    BnfTokenizer tokenizerOne;
    BnfTokenizer tokenizerEmpty;

    @Before
    public void setUp() throws Exception {
        tokenizerEmpty = new BnfTokenizer(new StringReader(""));
        tokenizerOne = new BnfTokenizer(new StringReader("<nonterminal> <some nonterminal> ::= terminal"));
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
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nonterminal>"), tokenizerOne.next());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<some nonterminal>"), tokenizerOne.next());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), tokenizerOne.next());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "terminal"), tokenizerOne.next()); //XXX this is where it fails
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nonterminal>"), tokenizerOne.next());
        assertFalse(tokenizerOne.hasNext());
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
