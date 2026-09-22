package ${package};

/**
 * A value this plugin owns: who to greet, and how many times.
 *
 * <p>A bot holds one the way it holds any other value — as a {@code @Param} field, which Studio's
 * Parameters window draws with {@code GreetingEditor}:
 *
 * <pre>{@code
 * @Param public static Greeting welcome = new Greeting("world", 1);
 * }</pre>
 *
 * <p>That line is the whole of how a greeting is stored. It is Java in the bot's own source, so a rename
 * javac can see fails the build naming the file, and there is no id and no text format to keep stable.
 * What must stay stable is what the host reads back: the constructor {@code GreetingType} declares.
 */
public record Greeting(String who, int times) {
}
