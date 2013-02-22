package bnf;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BNFTest {
//    Token term1 = new Token(TokenType.TERMINAL, "terminal");
//    Token term2 = new Token(TokenType.TERMINAL, "otherterminal");
//    Token term3 = new Token(TokenType.TERMINAL, "<terminal>");
//    Token nonterm1 = new Token(TokenType.NONTERMINAL, "<nonterminal>");
//    Token nonterm2 = new Token(TokenType.NONTERMINAL, "<othernonterminal>");
    String terminalString = "terminal";
    String optionalString = "\\[ <nonterminal> \\]";
    String anyNumString = "\\{ <nonterminal> \\}";
    String terms = "terminal \\{ anynum \\} \\[ <option> \\] <nonterminal>";
    String mostlyTerms = "terminal \\{ anynum \\} | \\[ option \\] <nonterminal>";
    String tntTokens = "terminal <nonterminal> otherterminal <othernonterminal> \\<terminal\\>";
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
        fail("Not yet implemented");
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
        fail("Not yet implemented");
    }

    @Test
    public final void testIsAnyNum() {
        fail("Not yet implemented");
    }

    @Test
    public final void testIsTerminal() {
        parser.read(new StringReader(terminalString));
        assertTrue(parser.isTerminal());
    }

    @Test
    public final void testIsNonterminal() {
        parser.read(new StringReader(tntTokens));
        assertEquals(null, parser.currentToken);
        assertTrue(parser.isTerminal());
        assertEquals("terminal", parser.currentToken.getValue());
        assertFalse(parser.isTerminal());
        assertEquals(null, parser.currentToken);
        assertTrue(parser.isNonterminal());
        assertEquals("<nonterminal>", parser.currentToken.getValue());
        assertFalse(parser.isNonterminal());
        assertEquals(null, parser.currentToken);
        assertTrue(parser.isTerminal());
        assertEquals("otherterminal", parser.currentToken.getValue());
        assertFalse(parser.isTerminal());
        assertEquals(null, parser.currentToken);
        assertTrue(parser.isNonterminal());
        assertEquals("<othernonterminal>", parser.currentToken.getValue());
        assertTrue(parser.isTerminal());
        assertEquals("<terminal>", parser.currentToken.getValue());
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
