package Editor;

import java.util.HashSet;

public class DictionaryHashSet implements Dictionary{
    private HashSet<String> words;

    public DictionaryHashSet() {
        this.words = new HashSet<String>();
    }

    @Override
    public boolean addWord(String word) {
        return words.add(word.toLowerCase());
    }

    @Override
    public boolean isWord(String str) {
        return words.contains(str.toLowerCase());
    }

    @Override
    public int size() {
        return words.size();
    }
}
