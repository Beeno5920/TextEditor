package Editor;

import java.util.TreeSet;

/**
 * @author UC San Diego Intermediate MOOC team
 *
 */
public class DictionaryBST implements Dictionary {
    private TreeSet<String> dict;

    public DictionaryBST() {
        dict = new TreeSet<String>();
    }

    public boolean addWord(String word) {
        if (!isWord(word.toLowerCase())) {
            dict.add(word.toLowerCase());
            return true;
        }
        return false;
    }

    public int size() {
        return dict.size();
    }

    public boolean isWord(String s) {
        return dict.contains(s.toLowerCase());
    }

}