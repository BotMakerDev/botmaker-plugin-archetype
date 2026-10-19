package com.example.greetings;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@code DECLARE.md} quotes this plugin's source: each {@code ```java} block must appear, line for line, in one
 * file under {@code src/main/java}, so an edit to the example that the page does not follow fails here. Lines
 * are compared stripped and blank lines skipped, so a quote may be dedented. A block fenced {@code ```java sdk}
 * quotes another project and is not checked.
 *
 * <p>Not part of the template a new plugin gets: the assembly leaves this file out, because a plugin's author
 * edits the example and owes the page nothing.
 */
class DeclareDocTest {

    private static final Path DOC = Path.of("src/template/DECLARE.md");
    private static final Path SOURCES = Path.of("src/main/java");

    @Test
    void every_java_quote_is_in_the_source() throws IOException {
        List<List<String>> quotes = quotes(Files.readAllLines(DOC));
        assertFalse(quotes.isEmpty(), DOC + " quotes no ```java block");
        List<List<String>> sources = new ArrayList<>();
        try (Stream<Path> files = Files.walk(SOURCES)) {
            for (Path file : files.filter(f -> f.toString().endsWith(".java")).toList()) {
                sources.add(significant(Files.readAllLines(file)));
            }
        }
        for (List<String> quote : quotes) {
            assertFalse(quote.isEmpty(), DOC + " has an empty ```java block");
            assertTrue(sources.stream().anyMatch(source -> Collections.indexOfSubList(source, quote) >= 0),
                    DOC + " quotes code no file under " + SOURCES + " has:\n" + String.join("\n", quote));
        }
    }

    private static List<List<String>> quotes(List<String> lines) {
        List<List<String>> quotes = new ArrayList<>();
        List<String> open = null;
        boolean fenced = false;
        for (String line : lines) {
            String fence = line.strip();
            if (!fenced && fence.startsWith("```")) {
                fenced = true;
                if (fence.startsWith("```java") && !fence.equals("```java sdk")) open = new ArrayList<>();
            } else if (fenced && fence.equals("```")) {
                if (open != null) quotes.add(significant(open));
                open = null;
                fenced = false;
            } else if (open != null) {
                open.add(line);
            }
        }
        assertFalse(fenced, DOC + " ends inside a fenced block");
        return quotes;
    }

    private static List<String> significant(List<String> lines) {
        return lines.stream().map(String::strip).filter(line -> !line.isEmpty()).toList();
    }
}
