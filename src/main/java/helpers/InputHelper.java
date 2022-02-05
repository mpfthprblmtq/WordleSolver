package helpers;

import java.io.*;
import java.util.Locale;

public class InputHelper {

    private BufferedReader in;
    private PrintStream out;
    private PrintStream err;

    public InputHelper() {
        this.in = new BufferedReader(new InputStreamReader(System.in));;
        this.out = System.out;
        this.err = System.err;
    }

    public InputHelper(InputStream in, PrintStream out, PrintStream err) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = out;
        this.err = err;
    }

    public void setInputStream(InputStream in) {
        this.in = new BufferedReader(new InputStreamReader(in));
    }

    public BufferedReader getInputStream() {
        return this.in;
    }

    public void setOutputStream(PrintStream out) {
        this.out = out;
    }

    public PrintStream getOutputStream() {
        return this.out;
    }

    public void setErrorStream(PrintStream err) {
        this.err = err;
    }

    public PrintStream getErrorStream() {
        return this.err;
    }

    public String getInput(String query, String regexVerifier, String invalidInputString) {
        boolean validInput = false;
        String input = "";

        // loop while the input isn't valid
        while (!validInput) {
            // ask the question
            out.println(query);
            out.print("\t: ");

            // get the answer
            try {
                input = in.readLine();
                if (input.toLowerCase(Locale.ROOT).equals("exit")) {
                    System.out.println();
                    System.exit(0);
                }
                if (input.matches(regexVerifier)) {
                    validInput = true;
                } else {
                    out.println(invalidInputString);
                    out.println();
                }
            } catch (IOException e) {
                // TODO handle exception logic here
            }
        }
        return input;
    }
}
