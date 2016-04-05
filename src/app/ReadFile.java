package app;

import java.io.*;

/**
 * Simple file reader providing nextLine method for retrieving sequential lines of text
 * and peekNextLine for checking before actually moving to
 * class uses BufferedReader over InputStreamReader with UTF-8 encoding
 *
 * @author Miguel Chambel
 */
public class ReadFile {

    private InputStream in;
    private String filename;
    private int currentLineNumber;


    /**
     * Initializes a newly created object of ReadFile.
     *
     * @param filename The file path to be red
     */
    public ReadFile(String filename) {
        this.filename = filename;

        currentLineNumber = 0;
    }

    /**
     * Reads the next line of the file as a string and saves the lineNumber for
     * future calls of this method. next call will return the next line and so on.
     * when end-of-file is reached null will be returned and the index of lineNumber
     * will reset to 0
     *
     * @return the next line of text in the file
     */
    public String nextLine() {

        String content = "";

        try {
            in = new FileInputStream(filename);
            Reader      inputStreamReader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inputStreamReader);

            int lineIndex = 0;

            String rl;
            for (; ; ) {
                rl = br.readLine();
                if (rl == null) {
                    content = rl;
                    currentLineNumber = 0;
                    break;
                }

                if (lineIndex == currentLineNumber) {
                    content = rl;
                    currentLineNumber++;
                    break;
                }
                lineIndex++;
            }

            br.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;

    }


    /**
     * Reads the next line of the file as a string but maintains the position of
     * current line number so that if nextLine is called after it will get this same line
     *
     * @return the next line of text in the file
     */
    public String peekNextLine() {

        String content = "";

        try {
            in = new FileInputStream(filename);
            Reader      inputStreamReader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inputStreamReader);

            int lineIndex = 0;

            String rl;
            for (; ; ) {
                rl = br.readLine();
                if (rl == null) {
                    content = rl;
                    break;
                }

                if (lineIndex == currentLineNumber) {
                    content = rl;
                    break;
                }
                lineIndex++;
            }

            br.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;

    }


}