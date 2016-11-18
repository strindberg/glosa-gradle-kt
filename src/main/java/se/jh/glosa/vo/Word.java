package se.jh.glosa.vo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.jh.glosa.fw.WordFileReader;

public class Word implements IWord {

    private static final String SEPARATOR = "|";

    private static final String COMMENT_CHAR = "#";

    private static final String ALT_SEPARATOR = ";";

    private String foreignWord;

    private String swedishWord;
    
    private List<String> foreignAlternatives;
    
    private List<String> swedishAlternatives;

    private int noOfUsed = 0;

    private int noOfCorrect = 0;

    private int noOfUsedInverse = 0;

    private int noOfCorrectInverse = 0;

    public Word() {
        super();
    }

    public int printQuestion(PrintStream out, boolean inverse) {
        increaseUse(inverse);
        int length;
        if (!inverse) {
            out.print(swedishWord.trim());
            length = swedishWord.trim().length();
        } else {
            out.print(foreignWord.trim());
            length = foreignWord.trim().length();
        }
        return length;
    }

    public String getQuestion(boolean inverse) {
        increaseUse(inverse);
        if (!inverse) {
            return swedishWord.trim();
        } else {
            return foreignWord.trim();
        }
    }

    public int printAnswer(PrintStream out, boolean inverse) {
        int length;
        if (!inverse) {
            out.print(foreignWord.trim());
            length = foreignWord.trim().length();
        } else {
            out.print(swedishWord.trim());
            length = swedishWord.trim().length();
        }
        return length;
    }

    public String getAnswer(boolean inverse) {
        if (!inverse) {
            return foreignWord.trim();
        } else {
            return swedishWord.trim();
        }
    }

    public boolean isCorrect(String answer, boolean inverse) {
        boolean isCorrect = false;
        if (!inverse) {
            isCorrect = answer != null && foreignAlternatives.contains(answer.trim());
            if (isCorrect) {
                increaseCorrect(inverse);
            }
        } else {
            isCorrect = answer != null && swedishAlternatives.contains(answer.trim());
            if (isCorrect) {
                increaseCorrect(inverse);
            }
        }
        return isCorrect;
    }

    private List<String> getAnswerAlternatives(String answer) {
        List<String> returnList = new ArrayList<String>();
        returnList.add(answer);
        // Remove everything within parentheses (and the parentheses themselves)
        String cleanAnswer = answer.replaceAll("\\(.*\\)", "");
        returnList.add(cleanAnswer);
        // Split on the ";", these constitute independent answers
        String[] alternatives = cleanAnswer.split(Pattern.quote(ALT_SEPARATOR));
        for (int i = 0; i < alternatives.length; i++) {
            String alternative = alternatives[i];
            if (alternative.contains("[")) {
                // Remove brackets (but not what's inside the brackets)
                String optional = alternative.replaceAll("\\[", "").replaceAll("\\]", "");
                Pattern p = Pattern.compile("\\[.*\\]");
                Matcher m = p.matcher(alternative);
                // Remove everything within brackets, as well as the brackets
                String mandatory = m.replaceAll("");
                returnList.add(mandatory.trim());
                returnList.add(optional.trim());
            } else {
                returnList.add(alternative.trim());
            }
        }
        return returnList;
    }

    private void increaseUse(boolean inverse) {
        if (!inverse) {
            noOfUsed++;
        } else {
            noOfUsedInverse++;
        }
    }

    private void increaseCorrect(boolean inverse) {
        if (!inverse) {
            noOfCorrect++;
        } else {
            noOfCorrectInverse++;
        }
    }

    public int readFromReader(BufferedReader reader, int lineCount, Map<String, IWordHistory> historyMap)
        throws IOException {
        ReadObject readObject = readValidLine(reader);
        String readLine = readObject.getReadLine();
        int linesRead = readObject.getNoOfLines();   
        if (readLine != null) {
            if (!readLine.contains(SEPARATOR)) {
                throw new IOException("Wrong format in file, line " + (lineCount + linesRead) + ": " + readLine);
            }
            IWordHistory history = historyMap.get(readLine);
            foreignWord = readLine.split(Pattern.quote(SEPARATOR))[0];
            swedishWord = readLine.split(Pattern.quote(SEPARATOR))[1];
            foreignAlternatives = getAnswerAlternatives(foreignWord);
            swedishAlternatives = getAnswerAlternatives(swedishWord);
            if (history != null) {
                noOfUsed = history.getFirst().intValue();
                noOfCorrect = history.getSecond().intValue();
                noOfUsedInverse = history.getThird().intValue();
                noOfCorrectInverse = history.getFourth().intValue();
            }
        } else {
            // If we don't have a valid line, the readValidLine has reached end of file or has only
            // read comment lines.
            linesRead = 0;
        }
        return linesRead;
    }

    private ReadObject readValidLine(BufferedReader reader) throws IOException {
        String readLine = null;
        int linesRead = 0;
        boolean goOn = true;
        while (goOn) {
            readLine = reader.readLine();
            if (readLine == null) {
                goOn = false;
            } else {
                linesRead++;
                // Ignore comment lines starting with comment char
                if (!readLine.startsWith(COMMENT_CHAR) && readLine.length() != 0) {
                    goOn = false;
                }
            }
        }
        return new ReadObject(readLine, linesRead);
    }

    public void writeToHistoryWriter(Writer historyWriter) throws IOException {
        historyWriter.write(foreignWord + SEPARATOR + swedishWord + WordFileReader.HISTORY_SEPARATOR + noOfUsed
                + WordFileReader.HISTORY_SEPARATOR + noOfCorrect + WordFileReader.HISTORY_SEPARATOR + noOfUsedInverse
                + WordFileReader.HISTORY_SEPARATOR + noOfCorrectInverse + "\n");
    }

    public int getNoOfCorrect(boolean inverse) {
        if (!inverse) {
            return noOfCorrect;
        } else {
            return noOfCorrectInverse;
        }
    }

    public int getNoOfUsed(boolean inverse) {
        if (!inverse) {
            return noOfUsed;
        } else {
            return noOfUsedInverse;
        }
    }

    public boolean equals(Object object) {
        if (!(object instanceof Word)) {
            return false;
        }
        Word compareWord = (Word) object;
        return compareWord.swedishWord != null && swedishWord != null && compareWord.swedishWord.equals(swedishWord)
                && compareWord.foreignWord != null && foreignWord != null
                && compareWord.foreignWord.equals(foreignWord);
    }

    public int hashCode() {
        int returnHash = 17;
        returnHash = returnHash + 37 * foreignWord.hashCode();
        returnHash = returnHash + 37 * swedishWord.hashCode();
        return returnHash;
    }
    
    private class ReadObject {
        private String readLine;
        private int noOfLines;

        public ReadObject(String readLine, int noOfLines) {
            this.readLine = readLine;
            this.noOfLines = noOfLines;
        }

        public int getNoOfLines() {
            return noOfLines;
        }

        public String getReadLine() {
            return readLine;
        }
    }
}
