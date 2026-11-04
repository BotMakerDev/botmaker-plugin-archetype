package com.example.greetings.api;

import com.botmaker.plugin.api.value.Link;

/**
 * <b>Level 1, a value type:</b> a value this plugin owns, who to greet and how many times.
 *
 * <p>A bot holds one the way it holds any other value — as a parameter, a method of a {@code @Param} class,
 * which Studio's Parameters window draws with {@code GreetingEditor}:
 *
 * <pre>{@code
 * public Greeting welcome() {
 *     return Greeting.to("world").times(2);
 * }
 * }</pre>
 *
 * <p>That line is the whole of how a greeting is stored. It is Java in the bot's own source, so a rename
 * javac can see fails the build naming the file, and there is no id and no text format to keep stable.
 * What must stay stable is what the host reads back: the factory {@link #to} and the link {@link #times(int)},
 * which {@code ExampleTypes.GREETING} declares with {@code writtenAs(Greeting::to)}.
 */
public record Greeting(String who, int times) {

    /**
     * A greeting to {@code who}, once. The host reads its parameter by name: {@code who} is given by
     * {@link #who()}, so keep the two names equal.
     */
    public static Greeting to(String who) {
        return new Greeting(who, 1);
    }

    /**
     * This greeting, said {@code times} times. {@link Link} tells the host to chain it after the factory, paired
     * with {@link #times()} by name and type; the host leaves it out while it is the factory's own 1.
     */
    @Link
    public Greeting times(int times) {
        return new Greeting(who, times);
    }
}
