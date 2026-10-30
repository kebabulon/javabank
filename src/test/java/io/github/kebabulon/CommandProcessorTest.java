package io.github.kebabulon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CommandProcessorTest {
    @TempDir
    Path dir;

    private Path file;
    private ByteArrayOutputStream buffer;
    private CommandProcessor processor;

    @BeforeEach
    void setup() {
        file = dir.resolve("accounts.txt");
        buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        processor = new CommandProcessor(new TxtAccountRepository(file), out);
    }

    private String output() {
        String text = buffer.toString(StandardCharsets.UTF_8);
        buffer.reset();
        return text;
    }

    @Test
    void listWithoutFilePrintsNoAccounts() {
        processor.execute("list");
        assertEquals("No accounts", output().trim());
        assertFalse(Files.exists(file));
    }
}
