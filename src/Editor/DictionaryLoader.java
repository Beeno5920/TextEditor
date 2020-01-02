package Editor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryLoader {
    public static void loadDictionary(Dictionary d, String filename) {
        // Editor.Dictionary files have 1 word per line
        BufferedReader reader = null;
        try {
            String nextWord;
            reader = new BufferedReader(new FileReader(filename));
            while ((nextWord = reader.readLine()) != null) {
                d.addWord(nextWord);
            }
        }catch (IOException e) {
            System.err.println("Problem loading dictionary file: " + filename);
            e.printStackTrace();
        }
    }

    public static void loadDictionary(Dictionary d, String filename, int nWords) {
        // Editor.Dictionary files have 1 word per line
        BufferedReader reader = null;
        try {
            String nextWord;
            reader = new BufferedReader(new FileReader(filename));
            int numLoaded = 0;
            while ((nextWord = reader.readLine()) != null && numLoaded < nWords) {
                d.addWord(nextWord);
                numLoaded++;
            }
            if (numLoaded < nWords) {
                System.out.print("loadDicitonary Warning: End of dictionary file reached.  ");
                System.out.println(nWords + " requested, but only " + numLoaded + " words loaded.");
            }
        }catch (IOException e) {
            System.err.println("Problem loading dictionary file: " + filename);
            e.printStackTrace();
        }
    }
}
