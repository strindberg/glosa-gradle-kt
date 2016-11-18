package se.jh.glosa.vo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Map;

public interface IWord {
    void writeToHistoryWriter(Writer writer) throws IOException;

    int readFromReader(BufferedReader reader, int lineCount, Map<String, IWordHistory> historyMap) throws IOException;

    int printQuestion(PrintStream out, boolean inverse);

    String getQuestion(boolean inverse);

    int printAnswer(PrintStream out, boolean inverse);

    public String getAnswer(boolean inverse);

    boolean isCorrect(String answer, boolean inverse);

    int getNoOfUsed(boolean inverse);

    int getNoOfCorrect(boolean inverse);

}
