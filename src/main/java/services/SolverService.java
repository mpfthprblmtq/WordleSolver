package services;

import utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SolverService {

    List<String> words = new ArrayList<>();

    public SolverService() {
        readAllWords();
    }

    public List<String> getWords() {
        return words;
    }

    /**
     * Reads all words in the text file in the repo and stores them in the words ivar
     */
    private void readAllWords() {
        System.out.print("Reading word file...");
        try {
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/actual_wordle.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                line = line.trim().toUpperCase(Locale.ROOT);
                // ignore all non-5-letter words
                if (line.length() == 5) {
                    words.add(line);
                }
            }
            reader.close();
            System.out.print(" Done\n");
            System.out.println(words.size() + " words read in!");
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't find file!\n\nExiting...");
            System.exit(0);
        }
    }

    public void reduceWithMatchedRegex(String regex) {
        if (StringUtils.isNotEmpty(regex)) {
            String[] arr = regex.split("");
            int index = 0;
            for (String letter : arr) {
                if (!letter.equals("_")) {
                    List<String> reducedWords = getAllMatchingWords(letter, index);
                    words.retainAll(reducedWords);
                }
                index++;
            }
        }
    }

    public void reduceWithFoundLettersRegex(String regex) {
        if (StringUtils.isNotEmpty(regex)) {
            String[] arr = regex.split("");
            List<String> matchedWords = new ArrayList<>();

            // get rid of any words that don't have the letter in it
            for (String letter : arr) {
                if (!letter.equals("_")) {
                    List<String> reducedWords = getAllMatchingWords(letter, -1);
                    words.retainAll(reducedWords);
                }
            }

            // get rid of any words that don't have that letter in that specific slot
            int index = 0;
            for (String letter : arr) {
                if (!letter.equals("_")) {
                    List<String> reducedWords = getAllNonMatchingWords(letter, index);
                    words.retainAll(reducedWords);
                }
                index++;
            }
        }
    }

    public void eliminateLetters(String notFoundLetters) {
        if (StringUtils.isNotEmpty(notFoundLetters)) {
            String[] arr = notFoundLetters.split("");
            for (String letter : arr) {
                List<String> reducedWords = getAllNonMatchingWords(letter, -1);
                words.retainAll(reducedWords);
            }
        }
    }

    private List<String> getAllMatchingWords(String letter, int index) {
        List<String> matchedWords = new ArrayList<>();
        letter = letter.toUpperCase(Locale.ROOT);
        char letterChar = letter.charAt(0);
        for (String word : words) {
            if (index == -1) {
                if (word.contains(letter)) {
                    matchedWords.add(word);
                }
            } else {
                if (word.charAt(index) == letterChar) {
                    matchedWords.add(word);
                }
            }
        }
        return matchedWords;
    }

    private List<String> getAllNonMatchingWords(String letter, int index) {
        List<String> matchedWords = new ArrayList<>();
        letter = letter.toUpperCase(Locale.ROOT);
        char letterChar = letter.charAt(0);
        for (String word : words) {
            if (index == -1) {
                if (!word.contains(letter)) {
                    matchedWords.add(word);
                }
            } else {
                if (word.charAt(index) != letterChar) {
                    matchedWords.add(word);
                }
            }
        }
        return matchedWords;
    }
}
