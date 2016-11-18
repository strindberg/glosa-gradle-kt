package se.jh.glosa.fw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import se.jh.glosa.vo.IWord;

public class SuccessiveRandomWordChooser implements IWordChooser {
    private List<IWord> words;

    private Map<IWord, Integer>shownWordsCounter = new HashMap<IWord, Integer>();
    
    private Random random;

    private IWord previousWord;
    
    private int noToChooseAmong;

    public SuccessiveRandomWordChooser(List<IWord> argWords) {
        words = argWords;
        random = new Random();
    }

    public IWord nextIWord(boolean inverse) {
        IWord returnWord;

        //Choose the word(s) which have been given correctly the least number of times
        int min = Integer.MAX_VALUE;
        List<IWord> correctWords = new ArrayList<IWord>();
        for (IWord word : words) {
            if (word.getNoOfCorrect(inverse) < min) {
                min = word.getNoOfCorrect(inverse);
                correctWords = new ArrayList<IWord>();
                correctWords.add(word);
            } else if (word.getNoOfCorrect(inverse) == min) {
                correctWords.add(word);
            }
        }

        noToChooseAmong = correctWords.size();
        
        // Among the words with the same number of correct answers, choose the one(s) which have
        // been shown (in this run) the least number of times.
        int showMin = Integer.MAX_VALUE;
        List<IWord> shownWords = new ArrayList<IWord>();
        for (IWord word : correctWords) {
            Integer shownNo = shownWordsCounter.get(word);
            if (shownNo == null) {
                shownNo = 0;
            }
            if (shownNo < showMin) {
                showMin = shownNo;
                shownWords = new ArrayList<IWord>();
                shownWords.add(word);
            } else if (shownNo == showMin) {
                shownWords.add(word);
            }
        }
        
        // Among the words with the same number of times shown (in this run), choose the one(s) which have
        // been shown historically the least number of times.
        min = Integer.MAX_VALUE;
        List<IWord> historyShownWords = new ArrayList<IWord>();
        for (IWord word : shownWords) {
            int shownNo = word.getNoOfUsed(inverse);
            if (shownNo < min) {
                min = shownNo;
                historyShownWords = new ArrayList<IWord>();
                historyShownWords.add(word);
            } else if (shownNo == min) {
                historyShownWords.add(word);
            }
        }
        
        // Make sure we never show the same word twice in a row, unless it's the last word
        if (previousWord != null && historyShownWords.size() > 1) {
            historyShownWords.remove(previousWord);
        }

        // Randomly choose one word from the list of candidates
        returnWord = historyShownWords.get(random.nextInt(historyShownWords.size()));
        // Update the number of times the result word has been shown
        shownWordsCounter.put(returnWord, ++showMin);
        previousWord = returnWord;
        return returnWord;
    }

    public int noToChooseAmong() {
        return noToChooseAmong;
    }
    
}
