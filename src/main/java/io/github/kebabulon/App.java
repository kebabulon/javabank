package io.github.kebabulon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    public static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        AccountRepository repository = new TxtAccountRepository(Path.of("accounts.txt"));
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        CommandProcessor commandProcessor = new CommandProcessor(repository, out);

        try {
            while(true) {
                out.print("> ");
                out.flush();
                String line = in.readLine();
                // try {
                if(!commandProcessor.execute(line)) {
                    return;
                }
                // }
                // catch(IllegalArgumentException e) {
                //     out.println(e.getMessage());
                // }
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
