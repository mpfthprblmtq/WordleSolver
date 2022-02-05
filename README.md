# WordleSolver
A somewhat simple java command line app that helps you solve the pandemic-induced game [Wordle](https://www.powerlanguage.co.uk/wordle/).

### How to run the app (Command Line)

Run the following commands from your favorite git and java enabled terminal:
```shell
git clone https://github.com/mpfthprblmtq/WordleSolver.git
cd WordleSolver
javac src/main/java/Main.java src/main/java/helpers/InputHelper.java src/main/java/services/SolverService.java src/main/java/utils/StringUtils.java
java -cp src/main/java Main
```

### How to use the app

After running the commands shown above, you should load up into the solver.  If the word read-in was successful, it should ask you if you want a starter word, which needs a "y" or "n" from you!

After that, it'll start going through the passes.  Let's say that the word of the day was "PLEAT" for example.

The first question you'll be asked is if there were any letters in their correct location (Green).  If the first word you guessed was "SPOKE," you can either just press Enter to signify no matches or input the string `_____` (five underscores, not case-sensitive).  
The next question you'll get is if there were any letters found that weren't in their correct location (Yellow).  So, since the "P" and "E" were in the word, but not in the right spot, you'll need to provide it the string `_P__E` (not case-sensitive).  
The last question is if any letters were not used in the word (Grey).  For this example, the letters "S", "O", and "K" were grey, so you can just provide the app with `SOK` (not case-sensitive).

After these three questions, you should have noticed that the words list has been reduced.  You'll be asked how many words to show to use on your next guess.  You can either provide the app with the word `all` to get all the words, or a number of words.

Wash, Rinse, and Repeat until you solve the puzzle!

---

### Resources

`words.txt` (all words, including words with numbers in them) and `words_alpha.txt` (a more dictionary type word list) are sourced from the Github user [dwyl's english-words repository](https://github.com/dwyl/english-words).  

`actual_wordle.txt` is sourced from the javascript source code from Wordle.