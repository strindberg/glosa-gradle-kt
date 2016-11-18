package se.jh.glosa.vo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class Verb implements IWord {

    private static final String COMMENT_CHAR = "#";

    private static final String SEPARATOR_REGEXP = " +";

    private String readLine;

    private Map<Tempi, String[]> tempiMap = new HashMap<Tempi, String[]>();

    public void writeToHistoryWriter(Writer writer) throws IOException {
        // TODO Auto-generated method stub
    }

    public int readFromReader(BufferedReader reader, int lineCount, Map<String, IWordHistory> historyMap)
        throws IOException {
        int linesRead = 0;
        String[] elements;

        linesRead += readValidLine(reader);        
        elements = readLine.split(SEPARATOR_REGEXP);
        tempiMap.put(Tempi.INFINITIV, new String[] { elements[0] });
        tempiMap.put(Tempi.GERUNDIUM, new String[] { elements[1] });
        tempiMap.put(Tempi.PERFEKTPARTICIP, new String[] { elements[1] });

        linesRead += readValidLine(reader);
        elements = readLine.split(SEPARATOR_REGEXP);
        tempiMap.put(Tempi.INDIKATIVPRESENS, elements);
        
        linesRead += readValidLine(reader);
        elements = readLine.split(SEPARATOR_REGEXP);
        tempiMap.put(Tempi.INDIKATIVPRETERITUM, elements);
        
        linesRead += readValidLine(reader);
        elements = readLine.split(SEPARATOR_REGEXP);
        tempiMap.put(Tempi.INDIKATIVIMPERFEKT, elements);
        
        linesRead += readValidLine(reader);
        elements = readLine.split(SEPARATOR_REGEXP);
        tempiMap.put(Tempi.INDIKATIVFUTURUM, elements);
        
        linesRead += readValidLine(reader);
        elements = readLine.split(SEPARATOR_REGEXP);
        tempiMap.put(Tempi.INDIKATIVKONDITIONALIS, elements);
        
        linesRead += readValidLine(reader);
        elements = readLine.split(SEPARATOR_REGEXP);
        tempiMap.put(Tempi.KONJUNKTIVPRESENS, elements);
        
        linesRead += readValidLine(reader);
        elements = readLine.split(SEPARATOR_REGEXP);
        tempiMap.put(Tempi.KONJUNKTIVIMPERFEKT, elements);
        
        linesRead += readValidLine(reader);
        elements = readLine.split(SEPARATOR_REGEXP);
        tempiMap.put(Tempi.IMPERATIVJAKANDE, elements);
        
        return linesRead;
    }

    private int readValidLine(BufferedReader reader) throws IOException {
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
        return linesRead;
    }

    public String[] getTempus(Tempi tempus) {
        return tempiMap.get(tempus);
    }
    
    public int printQuestion(PrintStream out, boolean inverse) {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getQuestion(boolean inverse) {
        // TODO Auto-generated method stub
        return null;
    }

    public int printAnswer(PrintStream out, boolean inverse) {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getAnswer(boolean inverse) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isCorrect(String answer, boolean inverse) {
        // TODO Auto-generated method stub
        return false;
    }

    public int getNoOfUsed(boolean inverse) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getNoOfCorrect(boolean inverse) {
        // TODO Auto-generated method stub
        return 0;
    }

}
