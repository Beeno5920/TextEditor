package Editor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {
    private String text;
    private int numWords;
    private int numSentences;
    private int numSyllables;

    public Document(String t) {
        this.text = t;
        processText();
    }

    private List<String> getTokens(String pattern) {
        ArrayList<String> tokens = new ArrayList<String>();
        Pattern tokSplitter = Pattern.compile(pattern);
        Matcher m = tokSplitter.matcher(this.text);

        while (m.find()) {
            tokens.add(m.group());
        }

        return tokens;
    }

    private boolean isWord(String token) {
        return !(token.indexOf('!') >= 0 || token.indexOf('?') >= 0 || token.indexOf('.') >= 0);
    }

    private void processText() {
        List<String> tokens = getTokens("[!?.]+|[a-zA-Z]+");

        for (String token : tokens) {
            if (isWord(token)) {
                numWords++;
                numSyllables += countSyllables(token);
                if (token.equals(tokens.get(tokens.size() - 1)))
                    numSentences++;
            }else {
                numSentences++;
            }
        }
    }

    private int countSyllables(String word) {
        int numSyllables = 0;
        boolean newSyllable = true;
        String vowels = "aeiou";
        char[] cArray = word.toCharArray();

        for (int i = 0; i < cArray.length; i++) {
            if (i == cArray.length - 1 && Character.toLowerCase(cArray[i]) == 'e' && newSyllable && numSyllables > 0) {
                numSyllables--;
            }

            int idx = vowels.indexOf(Character.toLowerCase(cArray[i]));
            if (newSyllable && idx >= 0) {
                newSyllable = false;
                numSyllables++;
            } else if (idx < 0) {
                newSyllable = true;
            }
        }

        return numSyllables;
    }

    public int getNumWords() {
        return this.numWords;
    }

    public int getNumSentences() {
        return this.numSentences;
    }

    public int getNumSyllables() {
        return this.numSyllables;
    }

    public String getText() {
        return this.text;
    }

    public double getFleschScore() {
        int numWords = getNumWords();
        int numSentences = getNumSentences();
        int numSyllables = getNumSyllables();

        if (numWords == 0 || numSentences == 0)
            return 0;

        return 206.835 - 1.015 * ((double)numWords / numSentences) - 84.6 * ((double)numSyllables / numWords);
    }
}
