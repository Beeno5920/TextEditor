package Editor;

import java.util.*;

public class AutoCompleteTrie implements Dictionary, AutoComplete {
    private TrieNode root;
    private int size;

    public AutoCompleteTrie() {
        this.root = new TrieNode();
    }

    @Override
    public List<String> predictCompletions(String prefix, int numOfCompletions) {
        TrieNode stem = this.root;
        String prefixToCheck = prefix.toLowerCase();
        List<String> predictions = new ArrayList<>();
        int count = 0;

        for (char ch : prefixToCheck.toCharArray()) {
            if (stem.getValidNextCharacters().contains(ch)) {
                stem = stem.getChild(ch);
            }else {
                return predictions;
            }
        }

        if (stem.endsWord()) {
            predictions.add(stem.getText());
            count++;
        }

        List<TrieNode> queue = new LinkedList<>();
        List<Character> children = new LinkedList<>(stem.getValidNextCharacters());
        for (int i = 0; i < children.size(); i++) {
            char ch = children.get(i);
            queue.add(stem.getChild(ch));
        }
        while (!queue.isEmpty() && count < numOfCompletions) {
            TrieNode curr = queue.remove(0);
            if (curr.endsWord()) {
                predictions.add(curr.getText());
                count++;
            }
            children = new LinkedList<>(curr.getValidNextCharacters());
            for (int i = 0; i < children.size(); i++) {
                char ch = children.get(i);
                queue.add(curr.getChild(ch));
            }
        }

        return predictions;
    }

    @Override
    public boolean addWord(String word) {
        TrieNode curr = root;
        String wordToAdd = word.toLowerCase();

        for (char ch : wordToAdd.toCharArray()) {
            if (curr.getValidNextCharacters().contains(ch)) {
                curr = curr.getChild(ch);
            }else {
                curr = curr.insert(ch);
            }
        }

        if (!curr.endsWord()) {
            curr.setEndsWord(true);
            size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean isWord(String s) {
        TrieNode curr = root;
        String wordToCheck = s.toLowerCase();

        for (char ch : wordToCheck.toCharArray()) {
            if (curr.getValidNextCharacters().contains(ch)) {
                curr = curr.getChild(ch);
            }else {
                return false;
            }
        }

        if (curr.endsWord())
            return true;
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }
}
