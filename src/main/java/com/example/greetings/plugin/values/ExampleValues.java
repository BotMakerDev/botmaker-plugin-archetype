package com.example.greetings.plugin.values;

import com.example.greetings.api.GreetValue;
import com.example.greetings.api.Greeting;
import com.botmaker.plugin.api.params.Param;
import com.botmaker.plugin.api.source.ManagedParam;
import com.botmaker.plugin.api.source.ManagedValue;

/**
 * <b>Level 3, a managed value:</b> a value this plugin keeps in the bot's own Java, here a parameter.
 *
 * <p>When a bot adds this plugin, Studio writes the field {@code WELCOME} declares into the bot's class
 * {@code Greetings}, marked {@code @GreetValue(GreetValue.Id.WELCOME)}. A bot without that class gets one, in its
 * package {@code plugins.<the last segment of this plugin's id>}.
 * The Parameters window then edits it with the greeting's own editor, and the bot reads it as plain Java:
 *
 * <pre>{@code
 * ExampleApi.greet(Greetings.welcome.who());
 * }</pre>
 *
 * <p>The steps, in order: the mark's id, the class the field goes in, its first value (a method reference, so
 * its full type is read off the method), where the Parameters window lists it, and the sentence a user reads
 * when Studio keeps them from renaming or deleting the field. The plugin names this class once,
 * {@code .values(ExampleValues.class)}, and the host takes every {@code ManagedValue} field in it.
 *
 * <p>Nothing converts a bot's old values when a release of this plugin changes one: Studio repairs them like
 * the rest of the bot's code (see the README).
 */
public final class ExampleValues {

    private ExampleValues() {}

    /** Who the bot greets first: a {@code @Param} field in the bot's {@code Greetings} class. */
    public static final ManagedParam<Greeting> WELCOME = ManagedValue.param(GreetValue.Id.WELCOME)
            .in("Greetings")
            .startsAs(ExampleValues::welcome)
            .shown("Greetings", "Who the bot greets first", Param.Visibility.PUBLIC)
            .because("This plugin reads this parameter. Change its value in the Runner or Project ▸ Parameters.");

    /** The welcome a bot starts with. */
    public static Greeting welcome() {
        return new Greeting("world", 1);
    }
}
