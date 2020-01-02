package Editor;

import java.util.HashMap;
import java.util.Set;

public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private String text;
    private boolean isWord;

    public TrieNode() {
        this.children = new HashMap<Character, TrieNode>();
        this.text = "";
        this.isWord = false;
    }

    public TrieNode(String text) {
        this();
        this.text = text;
    }

    public TrieNode getChild(Character c) {
        return children.get(c);
    }

    public TrieNode insert(Character c) {
        if (children.containsKey(c))
            return null;

        TrieNode next = new TrieNode(text + c.toString());
        children.put(c, next);
        return next;
    }

    public String getText() {
        return text;
    }

    public void setEndsWord(boolean b) {
        isWord = b;
    }

    public boolean endsWord() {
        return isWord;
    }

    public Set<Character> getValidNextCharacters() {
        return children.keySet();
    }
}
