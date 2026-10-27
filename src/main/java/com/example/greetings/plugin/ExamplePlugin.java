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
 * <p>Studio finds this class through the {@code botmaker.plugin} property of {@code pom.xml}, and knows the
 * plugin by that pom's {@code groupId:artifactId} and shows its {@code <name>}: none of the three is code. If
 * you rename or move this class, change the property too; {@code ExamplePluginTest} fails until you do. Studio
 * constructs it while a project is opening, and so does a headless host with no JavaFX. That is why nothing is
 * built here: the host reads each class or supplier when it asks.
 *
 * <p>This class is wiring only. Each toolbar feature you add gets a {@code plugin.<feature>} package of its own.
 */
public final class ExamplePlugin extends DeclaredPlugin {

    public ExamplePlugin() {
        super(StudioPlugin.declare()
                .types(ExampleTypes.class)
                .values(ExampleValues.class)
                .editors(() -> ExampleEditors.ALL));
    }
}
