package playground;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import playground.antlr.generated.PythonLexer;
import playground.antlr.generated.PythonParser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class PythonValidator {

    public static void main(String[] args) {
        List<String> snippets = List.of(
            // Hello world as a function
            """
            def foo():
                print("hello world.")
            foo()
            """,

            // Program to generate a random number between 0 and 0
            """
            # Program to generate a random number between 0 and 9
            import random
                        
            print(random.randint(0,9))
            """,

            // Reverse a number
            """
            num = 1234
            reversed_num = 0
                        
            while num != 0:
                digit = num % 10
                reversed_num = reversed_num * 10 + digit
                num //= 10
                        
            print("Reversed Number: " + str(reversed_num))    
            """
        );
        PythonValidator validator = new PythonValidator();
        for (String snippet : snippets) {
            boolean valid = validator.isValidSyntax(snippet);
            System.out.println("Valid? " + valid);
        }
    }

    public boolean isValidSyntax(String code) {
        PythonParser parser = getParser(code);

        // Redirect standard error stream
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errStream));

        try {
            parser.file_input();
        } finally {
            // Restore the original standard error stream
            System.setErr(originalErr);
        }

        // Check if there were any errors in the error stream
        String errorOutput = errStream.toString();
        if (!errorOutput.isEmpty()) {
            System.out.println(errorOutput);
            return false;
        } else {
            return true;
        }
    }

    private PythonParser getParser(String code) {
        // Create a CharStream from the Python code
        CharStream charStream = CharStreams.fromString(code);

        // Create the lexer
        PythonLexer lexer = new PythonLexer(charStream);

        // Create a token stream from the lexer
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        // Create the parser
        return new PythonParser(tokenStream);
    }
}
