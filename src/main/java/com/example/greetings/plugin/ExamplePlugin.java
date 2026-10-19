package com.example.greetings.plugin;

import com.example.greetings.plugin.editors.ExampleEditors;
import com.example.greetings.plugin.types.ExampleTypes;
import com.example.greetings.plugin.values.ExampleValues;
import com.botmaker.plugin.api.DeclaredPlugin;
import com.botmaker.plugin.api.StudioPlugin;

/**
 * A BotMaker Studio plugin, declared in one expression.
 *
 * <p>A plugin does up to three things, and this skeleton does each once. Read them in this order:
 *
 * <ol>
 *   <li><b>A value type</b> a bot can hold and Studio edits — {@code api/Greeting}, declared in
 *       {@code plugin/types/ExampleTypes}, drawn by {@code plugin/editors/GreetingEditor};</li>
 *   <li><b>A palette block</b> a bot calls — {@code api/ExampleApi}, which needs no line here: the host finds
 *       every {@code @Palette} class in this jar. {@code @Greetee} and {@code plugin/editors/ExampleEditors}
 *       give one {@code String} argument an editor of its own;</li>
 *   <li><b>A managed value</b> this plugin keeps in the bot's Java — {@code api/GreetValue} marks it,
 *       {@code plugin/values/ExampleValues} declares it.</li>
 * </ol>
 *
 * <p>{@code types} and {@code values} name the class holding the declarations: the host takes each
 * {@code public static final} one in it, in field order, which is the order a picker offers types in.
 * {@code editors} stays a list, narrowest match first. {@code toolbar} and {@code recorded} are there when you
 * need them.
 *
 * <p>Studio finds this class through {@code META-INF/services/com.botmaker.plugin.api.StudioPlugin} and
 * constructs it with {@code ServiceLoader} while a project is opening, and so does a headless host with no
 * JavaFX. That is why nothing is built here: the host reads each class or supplier when it asks.
 *
 * <p>This class is wiring only. Each toolbar feature you add gets a {@code plugin.<feature>} package of its own.
 */
public final class ExamplePlugin extends DeclaredPlugin {

    /**
     * The id Studio and the plugin registry know this plugin by. It is <b>not</b> the Maven coordinate: a
     * plugin may be re-published under a new coordinate, and this id must not change when it is. Two
     * plugins may not claim the same one. {@code botmaker plugin new} writes the one you give it here.
     */
    public static final String ID = "com.example.greetings";

    /**
     * The name a user reads. {@code botmaker plugin new} writes the one you give it here and in
     * {@code ExampleApi}'s palette label, the two places it is spelled as {@code "Greeter"}.
     */
    public static final String NAME = "Greeter";

    public ExamplePlugin() {
        super(StudioPlugin.id(ID).named(NAME)
                .types(ExampleTypes.class)
                .values(ExampleValues.class)
                .editors(() -> ExampleEditors.ALL));
    }
}
