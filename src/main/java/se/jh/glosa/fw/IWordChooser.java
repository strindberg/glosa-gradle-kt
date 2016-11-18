package se.jh.glosa.fw;

import se.jh.glosa.vo.IWord;

public interface IWordChooser {
    
    IWord nextIWord(boolean inverse);
    
    int noToChooseAmong();
    
}
