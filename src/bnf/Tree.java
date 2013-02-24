package bnf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * The <code>Tree<V></code> class represents a tree data structure that allows
 * shared structure (nodes may have multiple parents in the tree as long as no
 * looping occurs). This is a Generic class that allows any type of Object as
 * the values of the nodes. 
 * 
 * Getters and setters for the node values and children are provided 
 * (<code>getValue()</code>, <code>setValue(V)</code>, 
 * <code>addChild(int, Tree<V></code>, <code>addChildren(Tree<V>...)</code>, 
 * <code>getChild(int)</code>, and <code>getNumberOfChildren()</code>).
 * 
 * In addition, methods are provided to output the tree as a String 
 * (<code>toString()</code>) or via print (<code>print()</code>), and to parse
 * a String description of the tree to a <code>Tree<V></code> object
 * (<code>parse()</code>).  
 * 
 * @author Michael McLaughlin, mvm
 * @version 2/2/2013
 */
public class Tree<V> {
    V value;
    ArrayList<Tree<V>> children;
    private static StringTokenizer parsingTokens;

    /**
     * Constructor for the <code>Tree<V></code>. Creates a new tree with 
     * the specified value and children. Checks for potential loops in 
     * creating the tree as specified and returns an Exception if one is found.
     *  
     * @param value The value of the new tree.
     * @param children Children of the new tree in l-to-r order.
     * @throws IllegalArgumentException Thrown if a loop would be created by this tree.
     */
    @SafeVarargs
    public Tree(V value, Tree<V>... children) throws IllegalArgumentException {
        this.value = value;
        this.children = new ArrayList<Tree<V>>();
        this.addChildren(children);
    }
    
    /**
     * Sets the value of the given tree.
     * 
     * @param value The value to give to the tree.
     */
    public void setValue(V value) {
        this.value = value;
    }
    
    /**
     * Gets the value of the given tree.
     * 
     * @return The value of the tree.
     */
    public V getValue() {
        return value;
    }
    
    /**
     * Adds a single child tree to the tree at the given index.
     * 
     * Checks for potential loops in adding the child to the tree as specified
     * and returns an Exception if one is found.
     *  
     * @param index The index at which to add the child.
     * @param child The tree to add as the child.
     * 
     * @throws IllegalArgumentException Thrown if the child is null or would create a loop.
     * @throws IndexOutOfBoundsException Thrown if the given index is less than zero or greater than the number of children.
     */
    public void addChild(int index, Tree<V> child) 
        throws IllegalArgumentException, IndexOutOfBoundsException {
        
        if (index < 0) {
            throw new IndexOutOfBoundsException("The index that you input is less than zero.");
        } else if (index > this.children.size()) {
            throw new IndexOutOfBoundsException("The index that you input is larger than the number of current children.");
        } else if (child == null) {
            throw new IllegalArgumentException("You cannot add null children.");
        } else if (child.contains(this)) {
            throw new IllegalArgumentException("This would create a circular tree.");
        }
        
        children.add(index, child);
    }
    
    /**
     * Adds one or more child trees to the tree in l-to-r order to the right
     * of any existing children.
     * 
     * Checks for potential loops in adding children to the tree as specified and 
     * returns an Exception if one is found.
     * 
     * @param children The child trees to add to the parent tree.
     * @throws IllegalArgumentException If thrown due to a null child or potential loop, no children are added.
     */
    @SuppressWarnings("unchecked")
    public void addChildren(Tree<V>... children) throws IllegalArgumentException {
        for (int i = 0; i < children.length; i++) {
            if (children[i] == null) {
                throw new IllegalArgumentException("You cannot add null children.");
            }
            if (children[i].contains(this)) {
                throw new IllegalArgumentException("This would create a circular tree.");
            }
        }
        for (int i = 0; i < children.length; i++) {
            this.addChild(this.getNumberOfChildren(), children[i]);
        }
    }
    
    /**
     * Gets the number of children of the given tree.
     * 
     * @return The number of children.
     */
    public int getNumberOfChildren() {
        return children.size();
    }
    
    /**
     * Gets the child tree object located at the given index.
     * 
     * @param index The index of the child to get.
     * @return The child tree object at that index.
     * @throws IndexOutOfBoundsException Thrown if the given index is less than zero or greater than the number of children.
     */
    public Tree<V> getChild(int index) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException("The index that you input is less than zero - there is no child there.");
        } else if (index >= this.children.size()) {
            throw new IndexOutOfBoundsException("The index that you input is larger than the number of current children - there is no child there.");
        } else {
            return children.get(index);
        }
    }
    
    /**
     * Returns the iterator object for a tree's children.
     * 
     * @return An iterator for the tree's children.
     */
    public Iterator<Tree<V>> iterator() {
        return children.iterator();
    }
    
    /**
     * Checks if a tree already contains the given tree.
     * 
     * @param node The tree to check for. 
     * @return True if <code>node</code> is found in this tree.
     */
    boolean contains(Tree<V> node) {
        if (this == node) {
            return true;
        }
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).contains(node)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Parses a String describing a tree into a <code>Tree<String></code> object.
     * 
     * The String must start with the value of the root node of the tree. 
     * The children of each node are contained within parentheses. 
     * 
     * Example input:
     * "value1(value2 value3(value4) value5(value6 value7))"
     * 
     * becomes:
     * <pre>
     *         value1
     *       /    |    \ 
     * value2  value3  value5
     *            |     |    \
     *         value4 value6 value7</pre>
     *          
     * @param treeDescription The String describing the desired tree.
     * @return The Tree<String> object described by the String.
     */
    public static Tree<String> parse(String treeDescription) {
        // initialize the tokens
        parsingTokens = new StringTokenizer(treeDescription.trim(), " ()", true);
        // declare and initialize the token variable and the tree
        String nextToken = parsingTokens.nextToken();
        Tree<String> tree = null;
        // ensure the tree description doesn't start with "(" or ")"
        if (nextToken.equals("(") || nextToken.equals(")")) {
            throw new IllegalArgumentException("Tree description must start with a value.");
        }
        // create the first tree
        tree = new Tree<String>(nextToken);
        if (parsingTokens.hasMoreTokens()) {
            // iterate through spaces
            do {
                nextToken = parsingTokens.nextToken();
            } while (nextToken.equals(" "));
            // if there's a "(", start recursing
            if (nextToken.equals("(")) {
                tree = parseHelper(tree);
            } else {
                // at the top level, anything else is an exception
                throw new IllegalArgumentException("Tree description must start with a single valid tree.");
            }
            
        }
        return tree;
    }
    
    /**
     * Generates a <code>Tree<String></code> from a set of String Tokens 
     * created by the <code>parse</code> function using an input <code>tree</code>
     * as the head of the tree.
     * 
     * Works recursively so children of children etc. are added appropriately.
     * 
     * Acts as a helper function for <code>parse</code>.
     *       
     * Declared here as <code>package</code> to allow unit testing but would 
     * be declared private before code release.
     * 
     * @param headTree The tree to use as the root of the tree structure. 
     * @return The completed tree with descendants.
     */
    @SuppressWarnings("unchecked")
    static Tree<String> parseHelper(Tree<String> headTree) {
        // declare a new Tree and a token String
        Tree<String> newTree = null;
        String currentToken = parsingTokens.nextToken();
        // get rid of spaces
        while (currentToken.equals(" ")) {
            currentToken = parsingTokens.nextToken();
        }
        // check for illegal parentheses
        if (currentToken.equals(")")) {
            throw new IllegalArgumentException("Parentheses must not be empty in tree descriptions.");
        } else if (currentToken.equals("(")) {
            throw new IllegalArgumentException("An open parenthesis must be followed by a value.");
        }
        // create the new tree
        newTree = new Tree<String>(currentToken);
        currentToken = parsingTokens.nextToken();
        // loop through the coming tokens
        while (parsingTokens.hasMoreTokens()) {
            // iterate through trailing spaces
            while (currentToken.equals(" ")) {
                currentToken = parsingTokens.nextToken();
            }
            if (currentToken.equals(")")) {
                headTree.addChildren(newTree);
                return headTree;
            } else if (currentToken.equals("(")) {
                newTree = parseHelper(newTree);
                headTree.addChildren(newTree);
                // look at the next token and iterate through spaces
                do {
                    if (!(parsingTokens.hasMoreTokens())) {
                        throw new IllegalArgumentException(
                                "Tree description must have equal numbers of open and close parentheses.");
                    }
                    currentToken = parsingTokens.nextToken();
                } while (currentToken.equals(" "));
                if (currentToken.equals("(")) {
                    throw new IllegalArgumentException("An open parenthesis must be preceded by a value.");
                } else if (currentToken.equals(")")) {
                    return headTree;
                } else {
                    newTree = new Tree<String>(currentToken);
                    currentToken = parsingTokens.nextToken();
                    continue;
                }
            } else {
                headTree.addChildren(newTree);
                newTree = new Tree<String>(currentToken);
                currentToken = parsingTokens.nextToken();
            }            
        }
        if (newTree != null) {
            headTree.addChildren(newTree);
        }
        return headTree;
    }
    
    /**
     * Prints a representation of the tree to the standard output stream.
     * 
     * Trees are output in this format, where nodes at the same level in the
     * tree are at the same level l-to-r:
     * <pre>
     * value1
     * |  value2
     * |  value3
     * |  |  value4
     * |  |  |  value5
     * |  value6
     * |  |  value7
     * |  |  value8
     * </pre>
     */
    public void print() {
        this.printHelper(1);
    }
    
    /**
     * A helper method for <code>print</code> that runs recursively through
     * the tree and generates the printed output in the correct format.
     * 
     * Declared here as <code>package</code> to access during testing but would 
     * be declared private before code release.
     * 
     * @param level An integer to keep track of levels below the root at the current point of recursion. 
     */
    void printHelper(int level) {
        System.out.println(this.value + "");
        if (this.getNumberOfChildren() > 0) {
            for (int i = 0; i < this.getNumberOfChildren(); i++) {
                for (int j = 0; j < level; j++) {
                    System.out.print("|  ");
                }
                this.getChild(i).printHelper(level + 1);
            }
        }
    }
    
    /** 
     * Helper function for <code>toString()</code>. Turns the tree into a
     * String representation. Works recursively throughout the tree.
     * 
     * Declared here as <code>package</code> to access during testing but would 
     * be declared private before code release.
     * 
     * @param child The next tree to convert.
     * @param lastItem True if this is the rightmost child of a parent. 
     * @return The String as it exists at the end of the function.
     */
    String stringHelper(Tree<V> child, boolean lastItem) {
        String output = new String("");
        if (child.getNumberOfChildren() > 0) {
            output += child.getValue() + "(";
            for (int i = 0; i < child.getNumberOfChildren(); i++) {
                if (i == child.getNumberOfChildren() - 1) {
                    output += stringHelper(child.getChild(i), true);
                } else {
                    output += stringHelper(child.getChild(i), false);
                }
            }
            output = output + ")";
        } else {
            if (lastItem) {
                output += child.getValue();
            } else {
                output += child.getValue() + " ";
            }
        }
        return output;
    }
    
    /**
     * Generates a String representation of a tree.
     * 
     * Example - this tree generates the following String:
     * 
     * Tree:
     * <pre>
     *          one
     *        /     \
     *      two     three
     *            /       \  
     *         four       five
     *                  /  |  \
     *               six seven eight</pre>
     *               
     * Output String: <pre>one(two three(four five(six seven eight)))</pre>
     */
    @Override
    public String toString() {
        return stringHelper(this, true);
    }
    
    /**
     * Tests for equality between this tree and another object. 
     * 
     * Returns true if:
     * -The object is a <code>Tree</code with the same value as this tree.
     * -The values of the two nodes are equal.
     * -The two nodes have equal numbers of children.
     * -The above is true for all descendants of the root nodes.
     * 
     * @param obj The object to test for equality.
     * @return True if the trees are identical.
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tree<?>)) {
            return false;
        }
        Tree<V> objTree = (Tree<V>)obj;
        if (!(this.value.equals(objTree.getValue()))) {
            return false;
        }
        if (this.getNumberOfChildren() != objTree.getNumberOfChildren()) {
            return false;
        }
        for (int i = 0; i < this.getNumberOfChildren(); i++) {
            if (!(this.getChild(i).equals(objTree.getChild(i)))) {
                return false;
            }
        }
        return true;
    }
    
}
