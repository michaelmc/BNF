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
    BnfTokenizer tokenizerSplitTerminal;
    BnfTokenizer tokenizerSplitTerminal2;
    BnfTokenizer tokenizerSlashed;
    BnfTokenizer tokenizerUnslashed;
    BnfTokenizer tokenizerDefinedLoop;
    BnfTokenizer tokenizerDef1;
    BnfTokenizer tokenizerMeta1;
    BnfTokenizer tokenizerMeta2;

    @Before
    public void setUp() throws Exception {
        tokenizerEmpty = new BnfTokenizer(new StringReader(""));
        tokenizerOne = new BnfTokenizer(new StringReader("<nonterminal> <some nonterminal> ::= terminal"));
        tokenizerTerminal = new BnfTokenizer(new StringReader("terminal"));
        tokenizerNonterminal = new BnfTokenizer(new StringReader("<non term-inal>"));
        tokenizerNotClosedNonterminal = new BnfTokenizer(new StringReader("<nottermina lwhat"));
        tokenizerSplitTerminal = new BnfTokenizer(new StringReader("abc\\ abc"));
        tokenizerSplitTerminal2 = new BnfTokenizer(new StringReader("<answer>::=ye\ns|no."));
        tokenizerSlashed = new BnfTokenizer(new StringReader("<nonterminal>::=\\f\\o\\o\\|\\b\\a\\r"));
        tokenizerUnslashed = new BnfTokenizer(new StringReader("<nont\\erminal>::=foo|b\\::=ar. "));
        tokenizerDefinedLoop = new BnfTokenizer(new StringReader("foo::\\=bar"));
        tokenizerDef1 = new BnfTokenizer(new StringReader("foo::=bar"));
        tokenizerMeta1 = new BnfTokenizer(new StringReader("\\{foobar\\}"));
        tokenizerMeta2 = new BnfTokenizer(new StringReader("\\{ foobar \\}"));
    }

    @Test
    public void testBnfTokenizer() {
        tokenizerEmpty = new BnfTokenizer(new StringReader(""));
        tokenizerOne = new BnfTokenizer(new StringReader("<nonterminal> <some nonterminal> ::= terminal"));
        tokenizerTerminal = new BnfTokenizer(new StringReader("terminal"));
        tokenizerNonterminal = new BnfTokenizer(new StringReader("<non term-inal>"));
        tokenizerNotClosedNonterminal = new BnfTokenizer(new StringReader("<nottermina lwhat"));
        tokenizerSplitTerminal = new BnfTokenizer(new StringReader("abc\\ abc"));
        tokenizerSplitTerminal2 = new BnfTokenizer(new StringReader("<answer>::=ye\ns|no."));
        tokenizerSlashed = new BnfTokenizer(new StringReader("<nonterminal>::=\\f\\o\\o\\|\\b\\a\\r"));
        tokenizerUnslashed = new BnfTokenizer(new StringReader("<nont\\erminal>::=foo|b\\::=ar. "));
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
        
        assertTrue(tokenizerSplitTerminal.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "abc abc"), tokenizerSplitTerminal.next());
        assertFalse(tokenizerSplitTerminal.hasNext());
        
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nonterminal>"), tokenizerOne.next());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<some nonterminal>"), tokenizerOne.next());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), tokenizerOne.next());
        assertTrue(tokenizerOne.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "terminal"), tokenizerOne.next());
        assertFalse(tokenizerOne.hasNext());
        
        assertTrue(tokenizerSplitTerminal2.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<answer>"), tokenizerSplitTerminal2.next());       
        assertTrue(tokenizerSplitTerminal2.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), tokenizerSplitTerminal2.next());
        assertTrue(tokenizerSplitTerminal2.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "ye"), tokenizerSplitTerminal2.next());
        assertTrue(tokenizerSplitTerminal2.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "s"), tokenizerSplitTerminal2.next());
        assertTrue(tokenizerSplitTerminal2.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "|"), tokenizerSplitTerminal2.next());
        assertTrue(tokenizerSplitTerminal2.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "no"), tokenizerSplitTerminal2.next());
        assertTrue(tokenizerSplitTerminal2.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "."), tokenizerSplitTerminal2.next());
        assertFalse(tokenizerSplitTerminal2.hasNext());
        
        assertTrue(tokenizerSlashed.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nonterminal>"), tokenizerSlashed.next());
        assertTrue(tokenizerSlashed.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), tokenizerSlashed.next());
        assertTrue(tokenizerSlashed.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "foo|bar"), tokenizerSlashed.next());
        assertFalse(tokenizerSlashed.hasNext());
        
        assertTrue(tokenizerUnslashed.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nont\\erminal>"), tokenizerUnslashed.next());
        assertTrue(tokenizerUnslashed.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), tokenizerUnslashed.next());
        assertTrue(tokenizerUnslashed.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "foo"), tokenizerUnslashed.next());
        assertTrue(tokenizerUnslashed.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "|"), tokenizerUnslashed.next());
        assertTrue(tokenizerUnslashed.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "b::=ar"), tokenizerUnslashed.next());
        assertTrue(tokenizerUnslashed.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "."), tokenizerUnslashed.next());
        assertTrue(tokenizerUnslashed.hasNext());
        assertEquals(null, tokenizerUnslashed.next());
        assertFalse(tokenizerUnslashed.hasNext());
        
        assertEquals(new Token(TokenType.TERMINAL, "foo::=bar"), tokenizerDefinedLoop.next());
        assertFalse(tokenizerDefinedLoop.hasNext());
        
        assertTrue(tokenizerDef1.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "foo"), tokenizerDef1.next());
        assertTrue(tokenizerDef1.hasNext());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), tokenizerDef1.next());
        assertTrue(tokenizerDef1.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "bar"), tokenizerDef1.next());
        assertFalse(tokenizerDef1.hasNext());
        
        assertTrue(tokenizerMeta1.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "{foobar}"), tokenizerMeta1.next());
        assertFalse(tokenizerMeta1.hasNext());

        assertTrue(tokenizerMeta2.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "{"), tokenizerMeta2.next());
        assertTrue(tokenizerMeta2.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "foobar"), tokenizerMeta2.next());
        assertTrue(tokenizerMeta2.hasNext());
        assertEquals(new Token(TokenType.TERMINAL, "}"), tokenizerMeta2.next());
        assertFalse(tokenizerMeta2.hasNext());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNextNotClosedNT() {
        assertTrue(tokenizerNotClosedNonterminal.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nottermina lwhat"), tokenizerNotClosedNonterminal.next());
        assertFalse(tokenizerNotClosedNonterminal.hasNext());
    }
    
    @Test
    public void testBack() {
        for (int i = 0; i < 100; i++) {
            assertTrue(tokenizerUnslashed.hasNext());
            assertEquals(new Token(TokenType.NONTERMINAL, "<nont\\erminal>"), tokenizerUnslashed.next());
            tokenizerUnslashed.back();
        }
        assertEquals(new Token(TokenType.NONTERMINAL, "<nont\\erminal>"), tokenizerUnslashed.next());
        for (int i = 0; i < 100; i++) {
            assertTrue(tokenizerUnslashed.hasNext());
            assertEquals(new Token(TokenType.METASYMBOL, "::="), tokenizerUnslashed.next());
            tokenizerUnslashed.back();
        }
        assertEquals(new Token(TokenType.METASYMBOL, "::="), tokenizerUnslashed.next());
        for (int i = 0; i < 100; i++) {
            assertTrue(tokenizerUnslashed.hasNext());
            assertEquals(new Token(TokenType.TERMINAL, "foo"), tokenizerUnslashed.next());
            tokenizerUnslashed.back();
        }
        assertEquals(new Token(TokenType.TERMINAL, "foo"), tokenizerUnslashed.next());
        for (int i = 0; i < 100; i++) {
            assertTrue(tokenizerUnslashed.hasNext());
            assertEquals(new Token(TokenType.METASYMBOL, "|"), tokenizerUnslashed.next());
            tokenizerUnslashed.back();
        }
        assertEquals(new Token(TokenType.METASYMBOL, "|"), tokenizerUnslashed.next());
        for (int i = 0; i < 100; i++) {
            assertTrue(tokenizerUnslashed.hasNext());
            assertEquals(new Token(TokenType.TERMINAL, "b::=ar"), tokenizerUnslashed.next());
            tokenizerUnslashed.back();
        }
        assertEquals(new Token(TokenType.TERMINAL, "b::=ar"), tokenizerUnslashed.next());
        for (int i = 0; i < 100; i++) {
            assertTrue(tokenizerUnslashed.hasNext());
            assertEquals(new Token(TokenType.METASYMBOL, "."), tokenizerUnslashed.next());
            tokenizerUnslashed.back();
        }
        assertEquals(new Token(TokenType.METASYMBOL, "."), tokenizerUnslashed.next());
        assertEquals(null, tokenizerUnslashed.next());
        assertFalse(tokenizerUnslashed.hasNext());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testRemove() {
        tokenizerOne.remove();
    }

}
