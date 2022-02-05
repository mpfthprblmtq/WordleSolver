import helpers.InputHelper;
import services.SolverService;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Main {

    // service
    static SolverService solverService;

    // main.java.java.helpers
    static InputHelper inputHelper;

    public static void main(String[]args) {
        System.out.println("\nWelcome to a Wordle Solver thing I made because I was bored!");
        System.out.println("At any point, type \"exit\" to quit.\n");
        solverService = new SolverService();
        inputHelper = new InputHelper();
        System.out.println();
        askForStarterWord();
        System.out.println();

        for (int i = 0; i < 5; i++) {
            System.out.println("================== Pass " + (i + 1) + " ==================");
            getMatchingLetters();
            getFoundLetters();
            eliminateLetters();
            getWords();

            // check if we're done
            if (solverService.getWords().size() == 1) {
                break;
            }
        }
        System.out.println("Thanks for playing!");
    }

    /**
     * Asks the user for a starter word
     */
    private static void askForStarterWord() {
        // ask if we want a starter word
        boolean starterWord = inputHelper.getInput(
                "Do you want a starter word? (y/n)",
                "[yn]",
                "Invalid input, must by y/n format!").equals("y");
        if (starterWord) {
            System.out.println("Starter word: " + solverService.getWords().get(ThreadLocalRandom.current().nextInt(0, solverService.getWords().size() - 1)));
            boolean anotherStarterWord = true;
            while (anotherStarterWord) {
                anotherStarterWord = inputHelper.getInput("Another? (y/n)", "[yn]", "Invalid input, must by y/n format!").equals("y");
                if (anotherStarterWord) {
                    System.out.println("Starter word: " + solverService.getWords().get(ThreadLocalRandom.current().nextInt(0, solverService.getWords().size() - 1)));
                }
            }
        }
    }

    private static void getMatchingLetters() {
        // get letters that matched
        System.out.println();
        String matchedLetters = inputHelper.getInput(
                "Any letters in their correct location? (Green)\n(Format like C_EE_, leave blank for no matches)",
                "^$|[A-Za-z_]{5}",
                "Invalid input, must match \"C_EE_\" format!");

        // reduce the number of words
        int originalWordsSize = solverService.getWords().size();
        solverService.reduceWithMatchedRegex(matchedLetters);

        // inform the user
        if (solverService.getWords().size() == originalWordsSize) {
            System.out.println("No change!");
        } else {
            System.out.println("Words size down to " + solverService.getWords().size() + " words!");
        }
    }

    private static void getFoundLetters() {
        // get letters that were found
        System.out.println();
        String foundLetters = inputHelper.getInput(
                "Any letters found, but not in their correct location? (Yellow)\n(Format like C_EE_, leave blank for no matches)",
                "^$|[A-Za-z_]{5}",
                "Invalid input, must match \"C_EE_\" format!");

        // reduce the number of words again
        int originalWordsSize = solverService.getWords().size();
        solverService.reduceWithFoundLettersRegex(foundLetters);

        // inform the user
        if (solverService.getWords().size() == originalWordsSize) {
            System.out.println("No change!");
        } else {
            System.out.println("Words size down to " + solverService.getWords().size() + " words!");
        }
    }

    private static void eliminateLetters() {
        // eliminate any extra letters we don't need
        System.out.println();
        String notFoundLetters = inputHelper.getInput(
                "Any letters not found? (Grey)\n(Type all the letters, no spaces, so like \"abc\" and leave blank if none)",
                "[A-Za-z]{1,5}",
                "Invalid input, must match abc format!");

        // reduce the number of words yet again
        int originalWordsSize = solverService.getWords().size();
        solverService.eliminateLetters(notFoundLetters);

        // inform the user
        if (solverService.getWords().size() == originalWordsSize) {
            System.out.println("No change!");
        } else {
            System.out.println("Words size down to " + solverService.getWords().size() + " words!");
        }
    }

    private static void getWords() {
        // keep track of the words we already printed
        List<Integer> alreadyPrintedIndices = new ArrayList<>();

        System.out.println();
        System.out.println("Words size: " + solverService.getWords().size() + " words");

        // figure out the number of words to print
        // if it's less than 50, just print them, else ask
        int numberToPrint;
        if (solverService.getWords().size() <= 50) {
            numberToPrint = solverService.getWords().size();
        } else {
            String numberOfWords = inputHelper.getInput(
                    "How many words would you like to print out?\nType \"all\" for all words in the list, or a number of words to print.",
                    "all|\\d{1,}",
                    "Invalid input, must be \"all\" or number format!");

            if (numberOfWords.equals("all")) {
                numberToPrint = solverService.getWords().size();
            } else {
                numberToPrint = Integer.parseInt(numberOfWords);
            }
        }

        // figure out how many columns to print
        // if word count is less than 50, do 5
        // if word count is greater than 50 and less than 100, do 10
        // if word count is greater than 100, do 15
        int columns = numberToPrint < 50 ? 5 : numberToPrint > 100 ? 15 : 10;

        // print em
        for (int i = 0; i < numberToPrint; i++) {
            // check to see if we can break (all words have been printed)
            if (alreadyPrintedIndices.size() == solverService.getWords().size()) {
                break;
            }

            // let's print some words, but check to see if we've already printed it first
            System.out.print(solverService.getWords().get(i) + " ");
            alreadyPrintedIndices.add(i);
            if ((i + 1) % columns == 0) {
                System.out.print("\n");
            }
        }
        if (numberToPrint % 5 != 0) {
            System.out.println();
        }
        System.out.println();
    }
}