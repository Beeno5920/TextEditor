package Editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class NearbyWords implements SpellingSuggest{
    private static final int THRESHOLD = 1000;
    Dictionary dict;

    public NearbyWords(Dictionary dict) {
        this.dict = dict;
    }

    public List<String> getSimilarWords(String str, boolean wordsOnly) {
        List<String> similarWords = new ArrayList<String>();
        insertions(str, similarWords, wordsOnly);
        deletions(str, similarWords, wordsOnly);
        subsititution(str, similarWords, wordsOnly);

        return similarWords;
    }

    public void insertions(String str, List<String> similarWords, boolean wordsOnly) {
        if (str == null || str.isEmpty())
            return;

        for (int i = 0; i <= str.length(); i++) {
            for (int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
                StringBuffer strBuf = new StringBuffer(str);
                strBuf.insert(i, (char)charCode);
                String newStr = strBuf.toString();
                if (!similarWords.contains(newStr) && (!wordsOnly || dict.isWord(newStr))
                    && !str.equals(newStr)) {
                    similarWords.add(newStr);
                }
            }
        }
    }

    public void deletions(String str, List<String> similarWords, boolean wordsOnly) {
        if (str == null || str.isEmpty())
            return;

        for (int i = 0; i < str.length(); i++) {
            StringBuffer strBuf = new StringBuffer(str);
            strBuf.deleteCharAt(i);
            String newStr = strBuf.toString();
            if (!similarWords.contains(newStr) && (!wordsOnly || dict.isWord(newStr)) && !str.equals(newStr)) {
                similarWords.add(newStr);
            }
        }
    }

    public void subsititution(String str, List<String> similarWords, boolean wordsOnly) {
        if (str == null || str.isEmpty())
            return;

        for (int i = 0; i < str.length(); i++) {
            for (int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
                StringBuffer strBuf = new StringBuffer(str);
                strBuf.setCharAt(i, (char)charCode);
                String newStr = strBuf.toString();
                if (!similarWords.contains(newStr) && (!wordsOnly || dict.isWord(newStr))
                        && !str.equals(newStr)) {
                    similarWords.add(newStr);
                }
            }
        }
    }

    @Override
    public List<String> suggestions(String word, int numOfSuggestions) {
        List<String> queue = new LinkedList<String>();
        HashSet<String> visited = new HashSet<String>();
        List<String> retList = new LinkedList<String>();

        queue.add(word);
        visited.add(word);
        queue.add(word);
        visited.add(word);

        int count = 0;
        while (!queue.isEmpty() && retList.size() < numOfSuggestions && count < THRESHOLD) {
            String curr = queue.remove(0);
            List<String> neighbors = getSimilarWords(curr, true);
            for (String n : neighbors) {
                if (!visited.contains(n) && retList.size() < numOfSuggestions) {
                    visited.add(n);
                    queue.add(n);
                    if (dict.isWord(n) && retList.size() < numOfSuggestions) {
                        retList.add(n);
                    }
                }
            }
            count++;
        }

        return retList;
    }
}
