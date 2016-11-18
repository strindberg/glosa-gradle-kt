package se.jh.glosa.txtui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import se.jh.glosa.fw.Glosa;
import se.jh.glosa.vo.IWord;

public class TextController {
    private Glosa logic;

    private boolean inverse;

    BufferedReader in;

    private int printWidth = 60;

    public TextController(Glosa glosa, String fileName, boolean inverse) {
        logic = glosa;
        try {
            logic.newFile(new File(fileName));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
        this.inverse = inverse;
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public void go(boolean oneShot) {
        try {
            do {
                System.out.println();
                IWord currentWord = logic.getNewWord(inverse);

                int questionWidth = currentWord.printQuestion(System.out, inverse);
                for (int i = 0; i < printWidth - questionWidth; i++) {
                    System.out.print(" ");
                }
                System.out.print(logic.noToChooseAmong());
                System.out.println();

                String answer = in.readLine();
                if (answer.equalsIgnoreCase("xxx")) {
                    logic.quit(true);
                } else {
                    int wordWidth = currentWord.printAnswer(System.out, inverse);

                    if (!currentWord.isCorrect(answer, inverse)) {
                        for (int i = 0; i < printWidth - wordWidth; i++) {
                            System.out.print(" ");
                        }
                        System.out.print("INCORRECT!");
                    }
                    System.out.println("\n");
                }
            } while (!oneShot);
            logic.quit(true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }

    }

}
