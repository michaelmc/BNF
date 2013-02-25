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
        
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= foo | bar | what."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
        
        parser = new BNF();
        parser.read(new StringReader("<BNF> ::= { <rule> }. <rule> ::= <nonterminal> \\::= <definition> { \\| <definition> } \\. . <definition> ::= { <term> }.<term> ::= <terminal> | <nonterminal> | <option> | <any number of>. <option> ::= \\[ <definition> \\]. <any number of> ::= \\{ <definition> \\}."));
        assertEquals(6, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<BNF>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<rule>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<definition>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<term>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<option>")));
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<any number of>")));
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<BNF>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<rule>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<definition>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<term>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<option>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<any number of>")).print();
    }

    @Test
    public final void testMakeTree() {
        parser = new BNF();
        parser.stack.push(new Tree<Token>(new Token(TokenType.NONTERMINAL, "parent")));
        parser.stack.push(new Tree<Token>(new Token(TokenType.NONTERMINAL, "<child1>")));
        parser.stack.push(new Tree<Token>(new Token(TokenType.NONTERMINAL, "<child2>")));
        parser.makeTree(3, 2, 1);
        assertEquals(1, parser.stack.size());
        assertEquals(2, parser.stack.peek().getNumberOfChildren());
        assertEquals("parent", parser.stack.peek().getValue().getValue());
        assertEquals("<child1>", parser.stack.peek().getChild(0).getValue().getValue());
        assertEquals("<child2>", parser.stack.peek().getChild(1).getValue().getValue());

        parser = new BNF();
        parser.stack.push(new Tree<Token>(new Token(TokenType.NONTERMINAL, "parent")));
        parser.makeTree(1);
        assertEquals(1, parser.stack.size());
        assertEquals(0, parser.stack.peek().getNumberOfChildren());
    }
    
    @Test
    public final void testGetStackItem() {
        parser = new BNF();
        parser.stack.push(new Tree<Token>(new Token(TokenType.NONTERMINAL, "parent")));
        parser.stack.push(new Tree<Token>(new Token(TokenType.NONTERMINAL, "<child1>")));
        parser.stack.push(new Tree<Token>(new Token(TokenType.NONTERMINAL, "<child2>")));
        assertEquals(3, parser.stack.size());
        assertEquals("parent", parser.getStackItem(3).getValue().getValue());
        assertEquals(3, parser.stack.size());
        assertEquals("<child2>", parser.getStackItem(1).getValue().getValue());
        assertEquals(3, parser.stack.size());
    }
    
    @Test
    public final void testNextTokenEquals() {
        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("term ::= otherterm"));
        assertTrue(parser.nextTokenEquals("term"));
        assertFalse(parser.nextTokenEquals("term"));
        assertTrue(parser.nextTokenEquals("::="));
        assertFalse(parser.nextTokenEquals("term"));
        assertFalse(parser.rulesTokenizer.hasNext());
        assertTrue(parser.nextTokenEquals("otherterm"));
        assertFalse(parser.rulesTokenizer.hasNext());
        
        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("term ::= otherterm"));
        assertFalse(parser.nextTokenEquals("term", TokenType.NONTERMINAL));
        assertTrue(parser.nextTokenEquals("term", TokenType.TERMINAL));
        assertTrue(parser.nextTokenEquals("::=", TokenType.METASYMBOL));
        assertFalse(parser.nextTokenEquals("term", TokenType.OR));
        assertTrue(parser.nextTokenEquals("otherterm", TokenType.TERMINAL));
        assertFalse(parser.rulesTokenizer.hasNext());
    }

    @Test(expected=RuntimeException.class)
    public final void testError() {
        parser = new BNF();
        parser.error("EEK!");
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
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<BNF>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<rule>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<definition>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<term>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<option>")).print();
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<any number of>")).print();
    }
    
    @Test
    public final void testIsDefinition() {
        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("term"));
        assertTrue(parser.isDefinition());
        assertEquals(1, parser.stack.size());
        assertEquals(0, parser.stack.peek().getNumberOfChildren());
        assertEquals("term", parser.stack.peek().getValue().getValue());
        
        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("term otherterm"));
        assertTrue(parser.isDefinition());
        assertEquals(1, parser.stack.size());
        assertEquals(2, parser.stack.peek().getNumberOfChildren());
        assertEquals("SEQUENCE", parser.stack.peek().getValue().getValue());
        assertEquals("term", parser.stack.peek().getChild(0).getValue().getValue());
        assertEquals("otherterm", parser.stack.peek().getChild(1).getValue().getValue());
        
        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("term otherterm thirdterm"));
        assertTrue(parser.isDefinition());
        assertEquals(1, parser.stack.size());
        assertEquals(3, parser.stack.peek().getNumberOfChildren());
        assertEquals("SEQUENCE", parser.stack.peek().getValue().getValue());
        assertEquals("term", parser.stack.peek().getChild(0).getValue().getValue());
        assertEquals("otherterm", parser.stack.peek().getChild(1).getValue().getValue());
        assertEquals("thirdterm", parser.stack.peek().getChild(2).getValue().getValue());
    }
    
    @Test
    public final void testIsTerm() {
        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("term"));
        assertTrue(parser.isTerm());

        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("<term>"));
        assertTrue(parser.isTerm());

        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("[ term ]"));
        assertTrue(parser.isTerm());

        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("{ term }"));
        assertTrue(parser.isTerm());

        parser = new BNF();
        parser.rulesTokenizer = new BnfTokenizer(new StringReader("::="));
        assertFalse(parser.isTerm());
    }

    @Test
    public final void testIsOption() {
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= [ optional ]."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
        
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= [ { optional } ]."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
        
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= [ \\{ optional \\} ]."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();

        parser = new BNF();
        parser.read(new StringReader("<nt> ::= [ \\{ optional \\} ]"));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
    }

    @Test
    public final void testIsAnyNum() {
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= { anynum }."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();

        parser = new BNF();
        parser.read(new StringReader("<nt> ::= { { anynum } }."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();

    }

    @Test
    public final void testIsTerminal() {
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= foo."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
//        parser.rules.get(new Token(TokenType.NONTERMINAL, "<nt>")).print();
    }

    @Test
    public final void testIsNonterminal() {
        parser = new BNF();
        parser.read(new StringReader("<nt> ::= <foo>."));
        assertEquals(1, parser.rules.size());
        assertTrue(parser.rules.containsKey(new Token(TokenType.NONTERMINAL, "<nt>")));
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
    
    @SuppressWarnings("unchecked")
    @Test
    public final void testWriteHelper() {
        Tree<Token> sequence = new Tree<Token>(new Token(TokenType.SEQUENCE, "sequence"));
        Tree<Token> or = new Tree<Token>(new Token(TokenType.OR, "or"));
        Tree<Token> anynum = new Tree<Token>(new Token(TokenType.ANYNUM, "anynum"));
        Tree<Token> option = new Tree<Token>(new Token(TokenType.OPTION, "option"));
        Tree<Token> terminal = new Tree<Token>(new Token(TokenType.TERMINAL, "terminal"));
        Tree<Token> nonterminal = new Tree<Token>(new Token(TokenType.NONTERMINAL, "nonterminal"));
        Tree<Token> bracket = new Tree<Token>(new Token(TokenType.TERMINAL, "["));
        Tree<Token> defined = new Tree<Token>(new Token(TokenType.NONTERMINAL, "::="));
        sequence.addChildren(terminal, nonterminal);
        or.addChildren(terminal, nonterminal);
        option.addChild(0, sequence);
        anynum.addChild(0, sequence);
        
        parser = new BNF();
        parser.string = new StringBuilder();
        parser.writeHelper(sequence);
        assertEquals(parser.string.toString(), "terminal nonterminal ");
        
        parser = new BNF();
        parser.string = new StringBuilder();
        parser.writeHelper(or);
        assertEquals(parser.string.toString(), "terminal | nonterminal ");
        
        parser = new BNF();
        parser.string = new StringBuilder();
        parser.writeHelper(anynum);
        assertEquals(parser.string.toString(), "{ terminal nonterminal } ");

        parser = new BNF();
        parser.string = new StringBuilder();
        parser.writeHelper(option);
        assertEquals(parser.string.toString(), "[ terminal nonterminal ] ");

        parser = new BNF();
        parser.string = new StringBuilder();
        parser.writeHelper(bracket);
        assertEquals(parser.string.toString(), "\\[ ");

        parser = new BNF();
        parser.string = new StringBuilder();
        parser.writeHelper(defined);
        assertEquals(parser.string.toString(), "\\::= ");
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
