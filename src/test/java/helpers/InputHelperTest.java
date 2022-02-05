package java.helpers;

import helpers.InputHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Permission;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.*;

public class InputHelperTest {

    private InputHelper inputHelper;

    @BeforeEach
    public void beforeEach() {
        // create our inputHelper with blank print stream output, so we can ignore console output by default
        // any place where we're asserting on console output, we can just set the output streams there
        inputHelper = new InputHelper(System.in, new PrintStream(new ByteArrayOutputStream()), new PrintStream(new ByteArrayOutputStream()));
    }

    @Test
    public void testGetInputWithYesNo() {
        // create an input stream with just "y" or "n"
        ByteArrayInputStream inputStreamYes = new ByteArrayInputStream("y".getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inputStreamNo = new ByteArrayInputStream("n".getBytes(StandardCharsets.UTF_8));

        // yes
        inputHelper.setInputStream(inputStreamYes);
        assertEquals("y", inputHelper.getInput("query", "[yYnN]", "invalid string"));

        // no
        inputHelper.setInputStream(inputStreamNo);
        assertEquals("n", inputHelper.getInput("query", "[yn]", "invalid string"));
    }

    @Test
    public void testGetInputWithCustomTextRegex() {
        // create input streams with custom user input
        ByteArrayInputStream inputStreamUppercaseText = new ByteArrayInputStream("CHEESE".getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inputStreamLowercaseText = new ByteArrayInputStream("cheese".getBytes(StandardCharsets.UTF_8));

        // uppercase
        inputHelper.setInputStream(inputStreamUppercaseText);
        assertEquals("CHEESE", inputHelper.getInput("query", "[A-Z]+", "invalid string"));

        // lowercase
        inputHelper.setInputStream(inputStreamLowercaseText);
        assertEquals("cheese", inputHelper.getInput("query", "[a-z]+", "invalid string"));
    }

    @Test
    public void testGetInputWithInvalidString() {
        // create input streams with custom user input, first two are invalid
        ByteArrayInputStream inputStreamUppercaseText = new ByteArrayInputStream("CHEESE\nCheese\ncheese".getBytes(StandardCharsets.UTF_8));
        inputHelper.setInputStream(inputStreamUppercaseText);

        // create output stream so we can assert on the output
        ByteArrayOutputStream outputStreamContent = new ByteArrayOutputStream();
        PrintStream outputStream = new PrintStream(outputStreamContent);
        inputHelper.setOutputStream(outputStream);

        // get the user input
        String input = inputHelper.getInput("query", "[a-z]+", "invalid string");
        assertEquals("cheese", input);

        // assert that we showed the invalid string
        assertTrue(outputStreamContent.toString().contains("invalid string"));

        // count the number of times we asked the user, should be 3
        int numberOfOccurrences = 0;
        Pattern p = Pattern.compile("query");
        Matcher m = p.matcher(outputStreamContent.toString());
        while (m.find()) {
            numberOfOccurrences++;
        }
        assertEquals(3, numberOfOccurrences);
    }

    @Test
    public void testGetInputWithExit() {
        // create input streams with exit user input
        ByteArrayInputStream inputStreamExit = new ByteArrayInputStream("EXIT".getBytes(StandardCharsets.UTF_8));
        inputHelper.setInputStream(inputStreamExit);

        ExitAssertions.assertExits(0, () -> inputHelper.getInput("query", "", "invalid string"));
    }

    @Test
    public void testExit() {
        ExitAssertions.assertExits(0, () -> System.exit(0)); // succeeds
        ExitAssertions.assertExits(1, () -> System.exit(1)); // succeeds
    }
}

/**
 * Shamelessly stolen from the internet, and modified to my liking
 */
enum ExitAssertions {
    ; // why is this here?  why are we still here?  just to suffer?
    public static <E extends Throwable> void assertExits(final int expectedStatus, final ThrowingExecutable<E> executable) throws E {
        final SecurityManager originalSecurityManager = getSecurityManager();
        setSecurityManager(new SecurityManager() {
            @Override
            public void checkPermission(final Permission perm) {
                if (originalSecurityManager != null)
                    originalSecurityManager.checkPermission(perm);
            }

            @Override
            public void checkPermission(final Permission perm, final Object context) {
                if (originalSecurityManager != null)
                    originalSecurityManager.checkPermission(perm, context);
            }

            @Override
            public void checkExit(final int status) {
                super.checkExit(status);
                throw new ExitException(status);
            }
        });
        try {
            executable.run();
            fail("Expected System.exit(" + expectedStatus + ") to be called, but it wasn't called.");
        } catch (final ExitException e) {
            assertEquals(expectedStatus, e.status, "Wrong System.exit() status.");
        } finally {
            setSecurityManager(originalSecurityManager);
        }
    }

    public interface ThrowingExecutable<E extends Throwable> {
        void run() throws E;
    }

    private static class ExitException extends SecurityException {
        final int status;
        private ExitException(final int status) {
            this.status = status;
        }
    }
}
