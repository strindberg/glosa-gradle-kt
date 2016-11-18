package se.jh.glosa.vo;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WordTestCase {
    private Word word;

    @Before
    public void initialize() {
        word = new Word();
    }

    @Test
    public void printQuestion() {
        assertNotNull(word);
    }

    @Test(expected = IOException.class)
    public void readFromReader() throws IOException {
        word.readFromReader(new BufferedReader(new FileReader("doesnotexist")), 0, null);
    }

    @After
    public void clean() {

    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(WordTestCase.class);
    }
    
}
