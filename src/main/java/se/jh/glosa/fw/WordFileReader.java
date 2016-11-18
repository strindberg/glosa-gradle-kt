package se.jh.glosa.fw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import se.jh.glosa.vo.IWord;
import se.jh.glosa.vo.IWordHistory;
import se.jh.glosa.vo.Word;

public class WordFileReader {
    public static final String WORD_FILE_SUFFIX = ".txt";

    public static final String HISTORY_FILE_SUFFIX = ".his";

    public static final String HISTORY_SEPARATOR = "^";

    private static final String COMMENT_CHAR = "#";

    private static final int HISTORY_FILE_VERSION = 2;

    @SuppressWarnings("unused")
    private int historyFileVersion;

    private String fileName;

    private String historyFileName;

    Map<String, IWordHistory> historyMap;

    public WordFileReader(String fileName) throws IOException {
        this.fileName = fileName;
        if (!fileName.endsWith(WORD_FILE_SUFFIX)) {
            throw new IOException("Input file must end with " + WORD_FILE_SUFFIX + ".");
        }
        int indexOfDot = fileName.lastIndexOf(".");
        historyFileName = fileName.substring(0, indexOfDot) + HISTORY_FILE_SUFFIX;
    }

    public List<IWord> getWords() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        List<IWord> returnWords = new ArrayList<IWord>();
        fillHistoryMap();
        boolean goOn = true;
        int readLines = 0;
        while (goOn) {
            IWord word = new Word();
            int lineResult = word.readFromReader(in, readLines, historyMap);
            if (lineResult > 0) {
                readLines += lineResult;
                returnWords.add(word);
            } else {
                goOn = false;
            }
        }
        in.close();
        return returnWords;
    }

    private void fillHistoryMap() throws IOException {
        historyMap = new HashMap<String, IWordHistory>();
        File historyFile = new File(historyFileName);
        if (historyFile.exists()) {
            BufferedReader historyIn = new BufferedReader(new FileReader(historyFile));
            String inLine = historyIn.readLine();
            try {
                historyFileVersion = Integer.parseInt(inLine.substring(1, inLine.indexOf(" ")));
            } catch (Exception e) {
                throw new IOException("History file must begin with version number!");
            }
            boolean goOn = true;
            while (goOn) {
                inLine = historyIn.readLine();
                if (inLine == null) {
                    goOn = false;
                } else {
                    String[] lineParts = inLine.split(Pattern.quote(HISTORY_SEPARATOR));
                    String wordLine = lineParts[0];
                    int noOfUsed = Integer.parseInt(lineParts[1]);
                    int noOfCorrect = Integer.parseInt(lineParts[2]);
                    int noOfUsedInverse = Integer.parseInt(lineParts[3]);
                    int noOfCorrectInverse = Integer.parseInt(lineParts[4]);
                    WordHistory history = new WordHistory(noOfUsed, noOfCorrect, noOfUsedInverse, noOfCorrectInverse);
                    historyMap.put(wordLine, history);
                }
            }
            historyIn.close();
        }
    }

    public void saveHistory(List<IWord> words) throws IOException {
        BufferedWriter historyWriter = new BufferedWriter(new FileWriter(historyFileName));
        historyWriter.write(COMMENT_CHAR + HISTORY_FILE_VERSION + " \n");
        for (IWord word : words) {
            word.writeToHistoryWriter(historyWriter);
        }
        historyWriter.close();
    }

    private static class WordHistory implements IWordHistory {

        private int noOfUsed;

        private int noOfCorrect;

        private int noOfUsedInverse;

        private int noOfCorrectInverse;

        public WordHistory(int argNoOfUsed, int argNoOfCorrect, int argNoOfUsedInverse, int argNoOfCorrectInverse) {
            noOfUsed = argNoOfUsed;
            noOfCorrect = argNoOfCorrect;
            noOfUsedInverse = argNoOfUsedInverse;
            noOfCorrectInverse = argNoOfCorrectInverse;
        }

        public Number getFirst() {
            return noOfUsed;
        }

        public Number getSecond() {
            return noOfCorrect;
        }

        public Number getThird() {
            return noOfUsedInverse;
        }

        public Number getFourth() {
            return noOfCorrectInverse;
        }

    }

}
