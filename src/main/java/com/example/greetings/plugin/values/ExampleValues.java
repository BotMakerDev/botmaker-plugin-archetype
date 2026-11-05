package com.example.greetings.plugin.values;

import com.example.greetings.api.GreetValue;
import com.example.greetings.api.Greeting;
import com.botmaker.plugin.api.ValueHolder;
import com.botmaker.plugin.api.params.Param;
import com.botmaker.plugin.api.source.ManagedParam;
import com.botmaker.plugin.api.source.ManagedValue;

import java.util.List;

/**
 * <b>Level 3, a managed value:</b> a value this plugin keeps in the bot's own Java, here a parameter.
 *
 * <p>When a bot adds this plugin, Studio writes the parameter {@code WELCOME} declares as a method of the bot's
 * group {@code Greetings}, marked {@code @GreetValue(GreetValue.Id.WELCOME)}, in its package
 * {@code params.<groupId>.<artifactId>} — private to each computer, with a committed starting copy under
 * {@code starting-params/}. The Parameters window then edits it with the greeting's own editor, and the bot reads
 * it as plain Java:
 *
 * <pre>{@code
 * ExampleApi.greet(new Greetings().welcome().who());
 * }</pre>
 *
 * <p>The steps, in order: the mark's id, the class the field goes in, its first value (a method reference, so
 * its full type is read off the method), the line the Parameters window shows under it (the window lists it under
 * its group, {@code Greetings}: there is no other heading), and the sentence a user reads
 * when Studio keeps them from renaming or deleting the field. The plugin names this class once,
 * {@code .values(ExampleValues.class)}, and the host takes what {@link #values()} lists.
 *
 * <p>Nothing converts a bot's old values when a release of this plugin changes one: Studio repairs them like
 * the rest of the bot's code (see the README).
 */
public final class ExampleValues implements ValueHolder {

    /** Who the bot greets first: a method of the bot's {@code Greetings} group in {@code params/}. */
    public static final ManagedParam<Greeting> WELCOME = ManagedValue.param(GreetValue.Id.WELCOME)
            .in(GreetValue.Holder.GREETINGS)
            .startsAs(ExampleValues::welcome)
            .shown("Who the bot greets first", Param.Visibility.PUBLIC)
            .because("This plugin reads this parameter. Change its value in the Runner or Project ▸ Parameters.");

    @Override
    public List<ManagedValue<?>> values() {
        return List.of(WELCOME);
    }

    /** The welcome a bot starts with. */
    public static Greeting welcome() {
        return Greeting.to("world");
    }
}
