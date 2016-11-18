//TODO: flera filer på samma gång
//  -- kunna spara ner historia per fil
//TODO: strategi som väljer ord från ratio visad/korrekt
//TODO: kunna välja strategi från kommandoraden
//TODO: i18n
//TODO: modell till listan via byggaren (advanced -> post init)
//TODO: finslipa clearscreen till rätt storlek
//TODO: kunna ställa in textfärger, textstorlek i GUIt
//TODO: drag and drop, eller snarare: kunna klippa ut text från historiefältet
//TODO: ctrl+l för clearscreen
//TODO: ordfilseditor
//  -- starta en extern editor (med default notepad under windows, vi under linux)
//TODO: plugin-system (med reflection) för word och wordchooser

package se.jh.glosa.fw;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

import se.jh.glosa.txtui.TextController;
import se.jh.glosa.vo.IWord;

public class Glosa {

	/** Property specifying the last directory we chose a file from. */
	private static final String FILE_DIR_PROPERTY = "pepes.file.dir";

	/** Property specifying the last file we used. */
	private static final String FILE_FILE_PROPERTY = "pepes.file.file";

	private static final String DEFAULT_FILE_DIR = System.getProperty("user.home");

	private static final String ICON_FILE = "icons/face2.png";

	private String fileDir = DEFAULT_FILE_DIR;

	private String fileFile = "";

	private WordFileReader fileReader;

	private List<IWord> words;

	private IWordChooser wordChooser;

	public Glosa() {
		readProperties();
	}

	public IWord getNewWord(boolean inverse) {
		return wordChooser.nextIWord(inverse);
	}

	public int noToChooseAmong() {
		return wordChooser.noToChooseAmong();
	}

	public void quit(boolean save) throws IOException {
		if (save) {
			fileReader.saveHistory(words);
			writeProperties();
		}
		System.exit(0);
	}

	public String newFile(File file) throws IOException {
		if (fileReader != null && words != null) {
			fileReader.saveHistory(words);
		}

		fileReader = new WordFileReader(file.getPath());
		words = fileReader.getWords();
		wordChooser = new SuccessiveRandomWordChooser(words);
		if (file.getParentFile() != null) {
			fileDir = file.getParentFile().getAbsolutePath();
			fileFile = file.getName();
		}

		return file.getName();
	}

	/**
	 * Read preferences from storage.
	 */
	private void readProperties() {
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		fileDir = prefs.get(FILE_DIR_PROPERTY, DEFAULT_FILE_DIR);
		fileFile = prefs.get(FILE_FILE_PROPERTY, "");
	}

	/**
	 * Write preferences to storage.
	 */
	private void writeProperties() {
		if (fileDir != null) {
			Preferences prefs = Preferences.userNodeForPackage(this.getClass());
			prefs.put(FILE_DIR_PROPERTY, fileDir);
			prefs.put(FILE_FILE_PROPERTY, fileFile);
		}
	}

	public static void main(String[] args) {
		boolean inverse = false;
		boolean oneShot = false;
		String fileName = null;

		for (String arg : args) {
			if (arg.equalsIgnoreCase("-i")) {
				inverse = true;
			} else if (arg.equalsIgnoreCase("-o")) {
				oneShot = true;
			} else {
				fileName = arg;
			}
		}

		Glosa glosa = new Glosa();

		if (fileName == null) {
			System.out.println("Usage: glosa <-i> <-o> [ordfil]");
			System.exit(1);
		}

		TextController controller = new TextController(glosa, fileName, inverse);
		controller.go(oneShot);
	}

}
