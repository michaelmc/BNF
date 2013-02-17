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
    BnfTokenizer newLines;
    BnfTokenizer slasher1;
    BnfTokenizer slasher2;
    BnfTokenizer slasher3;
    BnfTokenizer slasher4;
    BnfTokenizer bnf1;
    BnfTokenizer bnf2;
    BnfTokenizer bnf3;
    BnfTokenizer bnf4;
    BnfTokenizer bnf5;
    BnfTokenizer bnf6;
    BnfTokenizer moreTests1;
    BnfTokenizer moreTests2;
    BnfTokenizer moreTests3;
    BnfTokenizer moreTests4;
    
    @Before
    public void setUp() throws Exception {
        tokenizerEmpty = new BnfTokenizer(new StringReader(""));
        tokenizerOne = new BnfTokenizer(new StringReader("<nonterminal> <some nonterminal> ::= terminal"));
        tokenizerTerminal = new BnfTokenizer(new StringReader("terminal"));
        tokenizerNonterminal = new BnfTokenizer(new StringReader("<non term-inal>"));
        tokenizerNotClosedNonterminal = new BnfTokenizer(new StringReader("<nottermina lwhat"));
        tokenizerSplitTerminal = new BnfTokenizer(new StringReader("abc\\ abc"));
        tokenizerSplitTerminal2 = new BnfTokenizer(new StringReader("<answer>::=ye\ns|no."));
        tokenizerSlashed = new BnfTokenizer(new StringReader("<nonterminal>::=\\f\\o\\o\\|\\b\\ar"));
        tokenizerUnslashed = new BnfTokenizer(new StringReader("<nont\\erminal>::=foo|b\\::=ar. "));
        tokenizerDefinedLoop = new BnfTokenizer(new StringReader("foo::\\=bar"));
        tokenizerDef1 = new BnfTokenizer(new StringReader("foo::=bar"));
        tokenizerMeta1 = new BnfTokenizer(new StringReader("\\{foobar\\}"));
        tokenizerMeta2 = new BnfTokenizer(new StringReader("\\{ foobar \\}"));
        moreTests1 = new BnfTokenizer(new StringReader("::=|{}\\{\\}..."));
        moreTests2 = new BnfTokenizer(new StringReader("<::=|{}\\{\\}...>")); 
        moreTests3 = new BnfTokenizer(new StringReader("<nonterminal> ::= \\<= \\<\\<."));
        moreTests4 = new BnfTokenizer(new StringReader("<nonterminal> ::= \\>= \\>\\<.\\<\\>"));
        newLines = new BnfTokenizer(new StringReader("\n\n\n\n\n\n\n\n\n\n\n\n\n"));
        slasher1 = new BnfTokenizer(new StringReader("foo\tbar"));
        slasher2 = new BnfTokenizer(new StringReader("foo\\tbar"));
        slasher3 = new BnfTokenizer(new StringReader("foo\\\tbar"));
        slasher4 = new BnfTokenizer(new StringReader("foo\\\\tbar"));
        bnf1 = new BnfTokenizer(new StringReader("<BNF> ::= { <rule> }."));
        bnf2 = new BnfTokenizer(new StringReader("<rule> ::= <nonterminal> \\::= <definition> { \\| <definition> } \\. ."));
        bnf3 = new BnfTokenizer(new StringReader("<definition> ::= { <term> }."));
        bnf4 = new BnfTokenizer(new StringReader("<term> ::= <terminal> | <nonterminal> | <option> | <any number of>."));
        bnf5 = new BnfTokenizer(new StringReader("<option> ::= \\[ <definition> \\]."));
        bnf6 = new BnfTokenizer(new StringReader("<any number of> ::= \\{ <definition> \\}."));
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
        
        assertTrue(newLines.hasNext());
        assertEquals(null, newLines.next());
        
        assertEquals(new Token(TokenType.TERMINAL, "foo"), slasher1.next());
        assertEquals(new Token(TokenType.TERMINAL, "bar"), slasher1.next());
        assertEquals(new Token(TokenType.TERMINAL, "foo"), slasher2.next());
        assertEquals(new Token(TokenType.TERMINAL, "bar"), slasher2.next());
        assertEquals(new Token(TokenType.TERMINAL, "foo\tbar"), slasher3.next());
        assertEquals(new Token(TokenType.TERMINAL, "foo\\tbar"), slasher4.next());
        
        assertEquals(new Token(TokenType.NONTERMINAL, "<BNF>"), bnf1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), bnf1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "{"), bnf1.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<rule>"), bnf1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "}"), bnf1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), bnf1.next());
        assertFalse(bnf1.hasNext());
        
        assertEquals(new Token(TokenType.NONTERMINAL, "<rule>"), bnf2.next());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), bnf2.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nonterminal>"), bnf2.next());
        assertEquals(new Token(TokenType.TERMINAL, "::="), bnf2.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<definition>"), bnf2.next());
        assertEquals(new Token(TokenType.METASYMBOL, "{"), bnf2.next());
        assertEquals(new Token(TokenType.TERMINAL, "|"), bnf2.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<definition>"), bnf2.next());
        assertEquals(new Token(TokenType.METASYMBOL, "}"), bnf2.next());
        assertEquals(new Token(TokenType.TERMINAL, "."), bnf2.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), bnf2.next());
        
        assertEquals(new Token(TokenType.NONTERMINAL, "<definition>"), bnf3.next());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), bnf3.next());
        assertEquals(new Token(TokenType.METASYMBOL, "{"), bnf3.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<term>"), bnf3.next());
        assertEquals(new Token(TokenType.METASYMBOL, "}"), bnf3.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), bnf3.next());
        
        assertEquals(new Token(TokenType.NONTERMINAL, "<term>"), bnf4.next());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), bnf4.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<terminal>"), bnf4.next());
        assertEquals(new Token(TokenType.METASYMBOL, "|"), bnf4.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nonterminal>"), bnf4.next());
        assertEquals(new Token(TokenType.METASYMBOL, "|"), bnf4.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<option>"), bnf4.next());
        assertEquals(new Token(TokenType.METASYMBOL, "|"), bnf4.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<any number of>"), bnf4.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), bnf4.next());
        
        assertEquals(new Token(TokenType.NONTERMINAL, "<option>"), bnf5.next());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), bnf5.next());
        assertEquals(new Token(TokenType.TERMINAL, "["), bnf5.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<definition>"), bnf5.next());
        assertEquals(new Token(TokenType.TERMINAL, "]"), bnf5.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), bnf5.next());
        
        assertEquals(new Token(TokenType.NONTERMINAL, "<any number of>"), bnf6.next());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), bnf6.next());
        assertEquals(new Token(TokenType.TERMINAL, "{"), bnf6.next());
        assertEquals(new Token(TokenType.NONTERMINAL, "<definition>"), bnf6.next());
        assertEquals(new Token(TokenType.TERMINAL, "}"), bnf6.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), bnf6.next());
        
        assertEquals(new Token(TokenType.METASYMBOL, "::="), moreTests1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "|"), moreTests1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "{"), moreTests1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "}"), moreTests1.next());
        assertEquals(new Token(TokenType.TERMINAL, "{}"), moreTests1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), moreTests1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), moreTests1.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), moreTests1.next());
        
        assertEquals(new Token(TokenType.NONTERMINAL, "<::=|{}\\{\\}...>"), moreTests2.next());
        
        assertEquals(new Token(TokenType.NONTERMINAL, "<nonterminal>"), moreTests3.next());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), moreTests3.next());
        assertEquals(new Token(TokenType.TERMINAL, "<="), moreTests3.next());
        assertEquals(new Token(TokenType.TERMINAL, "<<"), moreTests3.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), moreTests3.next());
        
        assertEquals(new Token(TokenType.NONTERMINAL, "<nonterminal>"), moreTests4.next());
        assertEquals(new Token(TokenType.METASYMBOL, "::="), moreTests4.next());
        assertEquals(new Token(TokenType.TERMINAL, ">="), moreTests4.next());
        assertEquals(new Token(TokenType.TERMINAL, "><"), moreTests4.next());
        assertEquals(new Token(TokenType.METASYMBOL, "."), moreTests4.next());
        assertEquals(new Token(TokenType.TERMINAL, "<>"), moreTests4.next());

    }

    @Test(expected=IllegalStateException.class)
    public void testNextNotClosedNT() {
        assertTrue(tokenizerNotClosedNonterminal.hasNext());
        assertEquals(new Token(TokenType.NONTERMINAL, "<nottermina lwhat"), tokenizerNotClosedNonterminal.next());
        assertFalse(tokenizerNotClosedNonterminal.hasNext());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testNextOpenBracket() {
        BnfTokenizer newTokenizer = new BnfTokenizer(new StringReader(">some stuff")); 
        newTokenizer.next();
    }
    
    @Test(expected=IllegalStateException.class)
    public void testNextNewLineNonterminal() {
        BnfTokenizer newTokenizer = new BnfTokenizer(new StringReader("<some\nstuff>")); 
        newTokenizer.next();
    }
    
    @Test(expected=IllegalStateException.class)
    public void testNextAngleBrackets() {
        BnfTokenizer newTokenizer = new BnfTokenizer(new StringReader("<some<stuff")); 
        newTokenizer.next();
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
