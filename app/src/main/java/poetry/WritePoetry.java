
package poetry;

import java.io.File;
import java.util.Scanner;
import java.util.Random;

public class WritePoetry {
    private HashTable<String, WordFreqInfo> wordCounts = new HashTable<>();

    public String writePoem(String filename, String startWord, int length, boolean printHashtable) {
        File file = new File(filename);
        WordFreqInfo prevWordFreq = null;

        try (Scanner input = new Scanner(file)) {
            // Start by reading all the words into memory.
            while (input.hasNextLine()) {
                var line = input.nextLine();
                var wordArray = line.split("\\s+|(?=\\p{Punct})|(?<=\\p{Punct})");
                for (var word : wordArray) {
                    if (word.length() > 0) {
                        word = word.toLowerCase();
                        if (prevWordFreq != null) {
                            prevWordFreq.updateFollows(word);
                        }
                        var wordInfo = wordCounts.find(word);
                        if (wordInfo != null) {
                            prevWordFreq = wordInfo;
                            wordInfo.incrementOccurCount();
                        } else {
                            prevWordFreq = new WordFreqInfo(word, 1);
                            wordCounts.insert(word, prevWordFreq);
                        }
                    }
                }
            }

        } catch (java.io.IOException ex) {
            System.out.println("An error occurred trying to read the dictionary: " + ex);
            return "";
        }
        if (wordCounts.find(startWord) == null) {
            System.out.println("The start word must be in the dictionary");
            return "";
        }
        var rand = new Random();
        var nextWord = startWord;
        System.out.print(nextWord + " ");
        for (int i = 0; i < length; i++) {
            var nextFreq = wordCounts.find(nextWord);
            var count = nextFreq.getOccurCount();
            var randNum = rand.nextInt(count);
            nextWord = nextFreq.getFollowWord(randNum);
            String postChar;
            String preChar;
            if (nextWord.equals(".") || nextWord.equals(",") || nextWord.equals("?") || nextWord.equals("!")) {
                preChar = "\b";
                postChar = "\n";
            } else {
                preChar = "";
                postChar = " ";
            }
            System.out.print(preChar + nextWord + postChar);
        }
        return "";
    }

    // @Override
    // public String toString() {
    // return wordCounts.toString();
    // }
}
