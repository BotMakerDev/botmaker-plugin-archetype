package com.example.greetings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * {@code src/template/minimal} is what {@code botmaker plugin new --minimal} writes in place of {@code src/main} and
 * {@code src/test}. Nothing else in this build compiles it, so this does, against the contract and the toolkit the
 * example builds with: the minimal skeleton is a project that compiled, as the full one is.
 *
 * <p>Not part of the template a new plugin gets: the assembly leaves this file out.
 */
class MinimalTemplateTest {

    private static final Path MINIMAL = Path.of("src/template/minimal/src");

    @Test
    void the_minimal_skeleton_compiles(@TempDir Path out) throws IOException {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        assumeTrue(javac != null, "no javac in this JRE");
        List<String> arguments = new ArrayList<>(List.of(
                "-cp", System.getProperty("java.class.path"), "-d", out.toString()));
        try (Stream<Path> files = Files.walk(MINIMAL)) {
            files.filter(f -> f.toString().endsWith(".java")).map(Path::toString).forEach(arguments::add);
        }
        assertEquals(0, javac.run(null, null, null, arguments.toArray(String[]::new)),
                MINIMAL + " no longer compiles against this contract and toolkit");
    }
}
