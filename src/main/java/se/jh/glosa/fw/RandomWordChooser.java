package se.jh.glosa.fw;

import java.util.List;
import java.util.Random;

import se.jh.glosa.vo.IWord;

public class RandomWordChooser implements IWordChooser {
    private List<IWord> words;
    
    private Random random;
    
    private int noToChooseAmong;
    
    public RandomWordChooser(List<IWord> argWords) {
        words= argWords;
        random = new Random();
        noToChooseAmong = argWords.size();
    }
    
    public IWord nextIWord(boolean inverse) {
        return words.get(random.nextInt(words.size()));
    }

    public int noToChooseAmong() {
        return noToChooseAmong;
    }
}
