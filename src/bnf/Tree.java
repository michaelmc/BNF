package bnf;

import java.util.ArrayList;

/**
 * @author mvm
 *
 * @param <T>
 */
public class Tree<T> {
    T value;
    ArrayList<Tree<T>> children;
    
    public Tree(T token) {
        value = token;
        children = new ArrayList<Tree<T>>();
    }
    
    public T getValue() {
        return value;
    }
    
    public void addChild(int index, Tree<T> child) {
        children.add(index, child);
    }
    public void addChild(Tree<T> child) {
        children.add(children.size(), child);
    }
    
    @SuppressWarnings("unchecked")
    public void addChildren(Tree<T>... children) {
        for (int i = 0; i < children.length; i++) {
            addChild(i + children.length, children[0]);
        }
    }
    
    @Override
    public String toString() {
        String s = value.toString();
        if (children.size() > 0) {
            s += "(";
            for (Tree<T> child : children) {
                s += child.toString() + " ";
            }
            s = s.substring(0, s.length() - 1) + ")";
        }
        return s;
    }
    
    public void print() {
        this.printHelper(1);
    }

    void printHelper(int level) {
        System.out.println(this.value + "");
        if (this.children.size() > 0) {
            for (int i = 0; i < this.children.size(); i++) {
                for (int j = 0; j < level; j++) {
                    System.out.print("|  ");
                }
                children.get(i).printHelper(level + 1);
            }
        }
    }
    
    public int getNumberOfChildren() {
        return children.size();
    }
    
    public Tree<T> getChild(int i) {
        return children.get(i);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tree<?>)) {
            return false;
        }
        Tree<T> objTree = (Tree<T>)obj;
        if (!(this.value.equals(objTree.getValue()))) {
            return false;
        }
        if (this.children.size() != objTree.getNumberOfChildren()) {
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
