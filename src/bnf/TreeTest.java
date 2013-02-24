package bnf;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for tree.Tree
 * 
 * @author Michael McLaughlin, mvm
 * @version 2/2/2013
 */
public class TreeTest {
    Tree<Integer> tree1;
    Tree<String> tree2;
    Tree<String> tree2b;
    Tree<String> tree2c;
    Tree<String> tree2d;
    Tree<Integer> tree3;
    Tree<Integer> tree4;
    Tree<Integer> tree5;
    Tree<Integer> tree6;
    Tree<Integer> tree7;
    Tree<Integer> bigTree10;
    Tree<Integer> bigTree9;
    Tree<Integer> bigTree8;
    Tree<Integer> bigTree7;
    Tree<Integer> bigTree6;
    Tree<Integer> bigTree5;
    Tree<Integer> bigTree4;
    Tree<Integer> bigTree3;
    Tree<Integer> bigTree2;
    Tree<Integer> bigTree1;
    Tree<String> exampleTree0;
    Tree<String> exampleTree1;
    Tree<String> exampleTree2;
    Tree<String> exampleTree3;
    Tree<String> exampleTree4;
    Tree<String> exampleTree5;
    Tree<String> exampleTree6;
    Tree<String> exampleTree7;
    Tree<String> exampleTree8;
    Tree<String> funTree1;
    Tree<String> funTree2;
    Tree<String> funTree3;
    Tree<String> funTree4;
    Tree<String> funTree5;
    Tree<String> funTree6;
    Tree<String> funTree7;
    Tree<String> funTree8;
    Tree<String> leftTree1;
    Tree<String> leftTree2;
    Tree<String> leftTree3;
    Tree<String> leftTree4;
    Tree<String> leftTree5;
    Tree<String> leftTree6;
    Tree<String> leftTree7;
    Tree<String> leftTree8;
    Tree<String> rightTree1;
    Tree<String> rightTree2;
    Tree<String> rightTree3;
    Tree<String> rightTree4;
    Tree<String> rightTree5;
    Tree<String> rightTree6;
    Tree<String> rightTree7;
    Tree<String> rightTree8;
    Tree<String> loopyTree1;
    Tree<String> loopyTree2;
    Tree<String> loopyTree3;
    Tree<String> loopyTree4;
    Tree<String> nullValTree1;
    Tree<String> nullValTree2;
    
    @Before
    public void setUp() throws Exception {
        tree1 = new Tree<Integer>(1);
        tree2 = new Tree<String>("Two");
        tree2b = new Tree<String>("TwoB");
        tree2c = new Tree<String>("TwoC");
        tree2d = new Tree<String>("Two");
        tree3 = new Tree<Integer>(3);
        tree4 = new Tree<Integer>(4, tree1, tree3);
        tree5 = new Tree<Integer>(5);
        tree6 = new Tree<Integer>(6);
        tree7 = new Tree<Integer>(7);
        bigTree10 = new Tree<Integer>(10);
        bigTree9 = new Tree<Integer>(9);
        bigTree8 = new Tree<Integer>(8, bigTree9, bigTree10);
        bigTree7 = new Tree<Integer>(7, bigTree8);
        bigTree6 = new Tree<Integer>(6);
        bigTree5 = new Tree<Integer>(5);
        bigTree4 = new Tree<Integer>(4, bigTree6);
        bigTree3 = new Tree<Integer>(3);
        bigTree2 = new Tree<Integer>(2, bigTree3, bigTree4, bigTree5);
        bigTree1 = new Tree<Integer>(1, bigTree2, bigTree7);
        exampleTree8 = new Tree<String>("eight");
        exampleTree7 = new Tree<String>("seven");
        exampleTree6 = new Tree<String>("six");
        exampleTree5 = new Tree<String>("five", exampleTree6, exampleTree7, exampleTree8);
        exampleTree4 = new Tree<String>("four");
        exampleTree3 = new Tree<String>("three", exampleTree4, exampleTree5);
        exampleTree2 = new Tree<String>("two");
        exampleTree1 = new Tree<String>("one", exampleTree2, exampleTree3);
        exampleTree0 = new Tree<String>("one", exampleTree2, exampleTree4);
        funTree8 = new Tree<String>("eight");
        funTree7 = new Tree<String>("seven");
        funTree6 = new Tree<String>("six");
        funTree5 = new Tree<String>("five");
        funTree4 = new Tree<String>("four", funTree8);
        funTree3 = new Tree<String>("three", funTree6, funTree7);
        funTree2 = new Tree<String>("two", funTree4, funTree5);
        funTree1 = new Tree<String>("one", funTree2, funTree3);
        leftTree8 = new Tree<String>("eight");
        leftTree7 = new Tree<String>("seven", leftTree8);
        leftTree6 = new Tree<String>("six", leftTree7);
        leftTree5 = new Tree<String>("five", leftTree6);
        leftTree4 = new Tree<String>("four", leftTree5);
        leftTree3 = new Tree<String>("three", leftTree4);
        leftTree2 = new Tree<String>("two", leftTree3);
        leftTree1 = new Tree<String>("one", leftTree2);
        rightTree8 = new Tree<String>("eight");
        rightTree7 = new Tree<String>("seven");
        rightTree6 = new Tree<String>("six");
        rightTree5 = new Tree<String>("five", rightTree6);
        rightTree4 = new Tree<String>("four", rightTree5);
        rightTree3 = new Tree<String>("three");
        rightTree2 = new Tree<String>("two", rightTree3);
        rightTree1 = new Tree<String>("one", rightTree2, rightTree4, rightTree7, rightTree8);
        loopyTree2 = new Tree<String>("two");
        loopyTree1 = new Tree<String>("one", loopyTree2);
        loopyTree4 = new Tree<String>("four", loopyTree1);
        loopyTree3 = new Tree<String>("three", loopyTree4);
        nullValTree2 = new Tree<String>(null);
        nullValTree1 = new Tree<String>(null);
    }

    @Test
    public final void testTree() {
        Tree<Integer> treeA;
        Tree<String> treeB;
        treeA = new Tree<Integer>(3);
        assertEquals((Integer)3, treeA.getValue());
        treeB = new Tree<String>("Some value.");
        assertEquals("Some value.", treeB.getValue());
        // FIXME add more tests here
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public final void testConstructorLoop() {
        bigTree10.addChildren(new Tree<Integer>(10, bigTree8));
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public final void testConstructorLoop2() {
        loopyTree2.addChildren(loopyTree3);
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public final void testConstructorLoopSelf() {
        loopyTree2.addChildren(loopyTree2);
    }

    @Test
    public final void testSetValue() {
        tree1 = new Tree<Integer>(3);
        assertEquals((Integer)3, tree1.getValue());
        tree1.setValue(213);
        assertEquals((Integer)213, tree1.getValue());
    }

    @Test
    public final void testGetValue() {
        tree1 = new Tree<Integer>(3);
        assertEquals((Integer)3, tree1.getValue());
        tree1.setValue(213);
        assertEquals((Integer)213, tree1.getValue());
    }

    @Test
    public final void testAddChild() {
        tree3.addChild(0, tree5);
        tree4.addChild(2, tree6);
        tree4.addChild(1, tree7);
        nullValTree1.addChild(0, nullValTree2);
    }
    
    @Test(expected=IndexOutOfBoundsException.class) 
    public final void testAddChildIndexErrorLow() {
        tree3.addChild(-1, tree5);
    }
    
    @Test(expected=IndexOutOfBoundsException.class) 
    public final void testAddChildIndexErrorHigh() {
        tree4.addChild(4, tree6);
    }

    @Test(expected=IllegalArgumentException.class) 
    public final void testAddChildNull() {
        tree4.addChild(0, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public final void testAddChildLoop() {
        bigTree10.addChild(0, bigTree8);
    }


    @SuppressWarnings("unchecked")
    @Test
    public final void testAddChildren() {
        tree4.addChildren(tree5, tree6, tree7);
        assertEquals(5, tree4.getNumberOfChildren());
        tree1.addChildren(tree5, tree3);
        assertEquals(2, tree1.getNumberOfChildren());
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class) 
    public final void testAddChildrenNull() {
        tree4.addChildren(tree5, null, tree6);
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testGetNumberOfChildren() {
        assertEquals(2, tree4.getNumberOfChildren());
        assertEquals(0, tree5.getNumberOfChildren());
        assertEquals(0, tree6.getNumberOfChildren());
        tree4.addChildren(tree5, tree6);
        assertEquals(4, tree4.getNumberOfChildren());
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public final void testAddChildrenLoop() {
        bigTree10.addChildren(bigTree9, bigTree8);
    }

    @Test
    public final void testGetChild() {
        assertEquals(tree1, tree4.getChild(0));
        assertEquals(tree3, tree4.getChild(1));
    }

    @Test(expected=IndexOutOfBoundsException.class) 
    public final void testGetChildIndexErrorLow() {
        tree3.getChild(-1);
    }
    
    @Test(expected=IndexOutOfBoundsException.class) 
    public final void testGetChildIndexErrorHigh() {
        tree4.getChild(2);
    }

    @Test
    public final void testIterator() {
        assertTrue(tree4.iterator() instanceof Iterator);
        Iterator<Tree<Integer>> iter = tree4.iterator();
        assertTrue(iter.hasNext());
        assertEquals(tree1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(tree3, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public final void testContains() {
        assertTrue(tree4.contains(tree4));
        assertTrue(tree4.contains(tree1));
        assertTrue(tree4.contains(tree3));
        assertFalse(tree4.contains(tree7));
        assertFalse(tree1.contains(tree4));
        
        assertTrue(bigTree1.contains(bigTree1));
        assertTrue(bigTree1.contains(bigTree2));
        assertTrue(bigTree1.contains(bigTree3));
        assertTrue(bigTree1.contains(bigTree4));
        assertTrue(bigTree1.contains(bigTree5));
        assertTrue(bigTree1.contains(bigTree6));
        assertTrue(bigTree1.contains(bigTree7));
        assertTrue(bigTree1.contains(bigTree8));
        assertTrue(bigTree1.contains(bigTree9));
        assertTrue(bigTree1.contains(bigTree10));
        
        assertFalse(bigTree2.contains(bigTree1));
        assertTrue(bigTree2.contains(bigTree2));
        assertTrue(bigTree2.contains(bigTree3));
        assertTrue(bigTree2.contains(bigTree4));
        assertTrue(bigTree2.contains(bigTree5));
        assertTrue(bigTree2.contains(bigTree6));
        assertFalse(bigTree2.contains(bigTree7));
        assertFalse(bigTree2.contains(bigTree8));
        assertFalse(bigTree2.contains(bigTree9));
        assertFalse(bigTree2.contains(bigTree10));

        assertFalse(bigTree3.contains(bigTree1));
        assertFalse(bigTree3.contains(bigTree2));
        assertTrue(bigTree3.contains(bigTree3));
        assertFalse(bigTree3.contains(bigTree4));
        assertFalse(bigTree3.contains(bigTree5));
        assertFalse(bigTree3.contains(bigTree6));
        assertFalse(bigTree3.contains(bigTree7));
        assertFalse(bigTree3.contains(bigTree8));
        assertFalse(bigTree3.contains(bigTree9));
        assertFalse(bigTree3.contains(bigTree10));

        assertFalse(bigTree4.contains(bigTree1));
        assertFalse(bigTree4.contains(bigTree2));
        assertFalse(bigTree4.contains(bigTree3));
        assertTrue(bigTree4.contains(bigTree4));
        assertFalse(bigTree4.contains(bigTree5));
        assertTrue(bigTree4.contains(bigTree6));
        assertFalse(bigTree4.contains(bigTree7));
        assertFalse(bigTree4.contains(bigTree8));
        assertFalse(bigTree4.contains(bigTree9));
        assertFalse(bigTree4.contains(bigTree10));

        assertFalse(bigTree5.contains(bigTree1));
        assertFalse(bigTree5.contains(bigTree2));
        assertFalse(bigTree5.contains(bigTree3));
        assertFalse(bigTree5.contains(bigTree4));
        assertTrue(bigTree5.contains(bigTree5));
        assertFalse(bigTree5.contains(bigTree6));
        assertFalse(bigTree5.contains(bigTree7));
        assertFalse(bigTree5.contains(bigTree8));
        assertFalse(bigTree5.contains(bigTree9));
        assertFalse(bigTree5.contains(bigTree10));

        assertFalse(bigTree6.contains(bigTree1));
        assertFalse(bigTree6.contains(bigTree2));
        assertFalse(bigTree6.contains(bigTree3));
        assertFalse(bigTree6.contains(bigTree4));
        assertFalse(bigTree6.contains(bigTree5));
        assertTrue(bigTree6.contains(bigTree6));
        assertFalse(bigTree6.contains(bigTree7));
        assertFalse(bigTree6.contains(bigTree8));
        assertFalse(bigTree6.contains(bigTree9));
        assertFalse(bigTree6.contains(bigTree10));

        assertFalse(bigTree7.contains(bigTree1));
        assertFalse(bigTree7.contains(bigTree2));
        assertFalse(bigTree7.contains(bigTree3));
        assertFalse(bigTree7.contains(bigTree4));
        assertFalse(bigTree7.contains(bigTree5));
        assertFalse(bigTree7.contains(bigTree6));
        assertTrue(bigTree7.contains(bigTree7));
        assertTrue(bigTree7.contains(bigTree8));
        assertTrue(bigTree7.contains(bigTree9));
        assertTrue(bigTree7.contains(bigTree10));

        assertFalse(bigTree8.contains(bigTree1));
        assertFalse(bigTree8.contains(bigTree2));
        assertFalse(bigTree8.contains(bigTree3));
        assertFalse(bigTree8.contains(bigTree4));
        assertFalse(bigTree8.contains(bigTree5));
        assertFalse(bigTree8.contains(bigTree6));
        assertFalse(bigTree8.contains(bigTree7));
        assertTrue(bigTree8.contains(bigTree8));
        assertTrue(bigTree8.contains(bigTree9));
        assertTrue(bigTree8.contains(bigTree10));

        assertFalse(bigTree9.contains(bigTree1));
        assertFalse(bigTree9.contains(bigTree2));
        assertFalse(bigTree9.contains(bigTree3));
        assertFalse(bigTree9.contains(bigTree4));
        assertFalse(bigTree9.contains(bigTree5));
        assertFalse(bigTree9.contains(bigTree6));
        assertFalse(bigTree9.contains(bigTree7));
        assertFalse(bigTree9.contains(bigTree8));
        assertTrue(bigTree9.contains(bigTree9));
        assertFalse(bigTree9.contains(bigTree10));

        assertFalse(bigTree10.contains(bigTree1));
        assertFalse(bigTree10.contains(bigTree2));
        assertFalse(bigTree10.contains(bigTree3));
        assertFalse(bigTree10.contains(bigTree4));
        assertFalse(bigTree10.contains(bigTree5));
        assertFalse(bigTree10.contains(bigTree6));
        assertFalse(bigTree10.contains(bigTree7));
        assertFalse(bigTree10.contains(bigTree8));
        assertFalse(bigTree10.contains(bigTree9));
        assertTrue(bigTree10.contains(bigTree10));

    }

    @Test
    public final void testParse() {
        assertEquals(tree2, Tree.parse("Two"));
        assertEquals(exampleTree0, Tree.parse("one(two four)"));
        assertEquals(exampleTree1, Tree.parse("one(two three(four five(six seven eight)))"));
        // trees are the same but parse renders Tree<String> rather than Tree<Integer>
        assertFalse(bigTree1.equals(Tree.parse("1(2(3 4(6)5)7(8(9 10)))")));
        assertTrue(funTree1.equals(Tree.parse("one(two(four(eight)five)three(six seven))")));
        assertTrue(funTree1.equals(Tree.parse("one(two(four(eight)five)three(six seven) ) ")));
        assertEquals(leftTree1, Tree.parse("one(two(three(four(five(six(seven(eight)))))))"));
        assertEquals(rightTree1, Tree.parse("one (two (three) four (five (six)) seven eight)"));
        assertTrue(funTree1.equals(Tree.parse("            one(two(four(eight)five)three(six seven))")));
        assertTrue(funTree1.equals(Tree.parse("one(two(four(eight)five)three(six seven) )       ")));
        assertEquals(leftTree1, Tree.parse("one  (  two  (three  (four   (five(six    (seven (eight  ) ) )    ))  ) )"));
        assertEquals(rightTree1, Tree.parse("one(two (three)   four (          five(six))seven eight)  "));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public final void testParseInvalidTree() {
        Tree.parse("one two");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public final void testParseUnequalParens() {
        assertTrue(funTree1.equals(Tree.parse("one(two(four(eight)five)three(six seven)")));        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public final void testParseStartWithOpenParen() {
        assertTrue(funTree1.equals(Tree.parse("(one(two(four(eight)five)three(six seven)))")));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public final void testParseStartWithCloseParen() {
        assertTrue(funTree1.equals(Tree.parse(")one(two(four(eight)five)three(six seven))")));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public final void testParseTwoValues() {
        assertTrue(funTree1.equals(Tree.parse("one two three")));
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testParseEmptyParens() {
        assertTrue(funTree1.equals(Tree.parse("one two(  ) three()")));
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testParseDoubleOpenParens() {
        assertTrue(funTree1.equals(Tree.parse("one two((three))")));
    }

    @Test
    public final void testPrint() {
//        exampleTree1.print();
//        bigTree1.print();
    }
    
    @Test
    public final void testToString() {
        tree1.addChild(0, tree3);
        assertEquals("1(3)", tree1.toString());
        assertEquals("one(two three(four five(six seven eight)))", exampleTree1.toString());
        assertEquals("1(2(3 4(6)5)7(8(9 10)))", bigTree1.toString());
    }
    
    @Test
    public final void testStringHelper() {
        tree1.addChild(0, tree3);
        assertEquals("1(3)", tree1.stringHelper(tree1, true));
        assertEquals("one(two three(four five(six seven eight)))", exampleTree1.stringHelper(exampleTree1, true));
        assertEquals("1(2(3 4(6)5)7(8(9 10)))", bigTree1.stringHelper(bigTree1, true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testEqualsObject() {
        assertTrue(tree3.equals(bigTree3));
        assertTrue(tree2.equals(tree2));
        assertTrue(tree2.equals(tree2d));
        tree2.addChildren(tree2b, tree2c);
        assertFalse(tree2.equals(tree2d));
        tree2d.addChildren(tree2b, tree2c);
        assertTrue(tree2.equals(tree2d));
    }

}
