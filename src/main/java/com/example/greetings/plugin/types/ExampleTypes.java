package com.example.greetings.plugin.types;

import com.example.greetings.api.Greeting;
import com.example.greetings.plugin.editors.GreetingEditor;
import com.botmaker.plugin.api.TypeHolder;
import com.botmaker.plugin.api.value.DeclaredCallType;
import com.botmaker.plugin.api.value.PluginType;

import java.util.List;

/**
 * <b>Level 1, a value type.</b> The types this plugin owns, each a {@code public static final} field here and
 * listed by {@link #types()} — {@code .types(ExampleTypes.class)} takes that list, in its order, which is the order
 * a picker offers them. A field you forget to list is declared nowhere; {@code ExamplePluginTest} says so.
 * Each is declared once as a {@code PluginType.value(…)} whose steps ask, in order,
 * for everything the host needs — the compiler walks you through it:
 *
 * <ol>
 *   <li>{@code fresh(…)} — what a new one starts as ({@code firstConstant()} for an enum; a value the bot
 *       fills in itself is no declaration here, its method in {@code api} carries {@code @Fills});</li>
 *   <li>{@code editor(…)} — how a person edits one, then optionally {@code preview(…)};</li>
 *   <li>{@code writtenAs…} — how its Java is written: {@code writtenAs(Owner::factory)} for a factory call, as
 *       here, each parameter read by the accessor of the same name and type, then each method marked
 *       {@code @Link} chained after it; {@code writtenAsRecord()} for a record's constructor;
 *       {@code writtenAsEach(…)} for a varargs call; {@code writtenAsConstant()} for an enum;
 *       {@code writtenAsLiteral()} for text or a number.</li>
 * </ol>
 *
 * <p>The names are the declaration: the plugin is compiled with {@code -parameters} (the parent sets it), and a
 * factory parameter or a {@code @Link} with no accessor to pair with is refused when this class is first read —
 * by {@code ExamplePluginTest}, before any bot sees it. For a type that is not yours (a JDK class) or a pair whose
 * names differ, name the accessors yourself: {@code writtenAs(LocalDate::of, LocalDate::getYear, …)}, and
 * {@code .with(Owner::wither, Owner::part)} for a link.
 *
 * <p>Nothing here parses or prints Java — the host does, from the parts — which is why a quote inside
 * {@code who} cannot break a bot. {@code botmaker plugin validate} checks that {@code build(components(v))}
 * gives {@code v} back: a value that changed on the way would be rewritten every time a bot is saved.
 *
 * <p><b>The editor is named behind a second arrow</b>, {@code () -> GreetingEditor::of}. A headless host — the
 * registry's CI, {@code botmaker plugin validate} — builds this list with no JavaFX, and a reference to a
 * method returning a {@code Node} would load JavaFX the moment the list is built.
 */
public final class ExampleTypes implements TypeHolder {

    /**
     * A greeting, written {@code Greeting.to("world")}, or {@code Greeting.to("world").times(3)} when said more
     * than once. The class is the identity, so no other plugin may declare {@link Greeting} — a host loading both
     * would leave one of them out.
     */
    public static final DeclaredCallType<Greeting> GREETING = PluginType.value(Greeting.class)
            .fresh(() -> Greeting.to("world"))
            .editor(() -> GreetingEditor::of)
            .writtenAs(Greeting::to);

    @Override
    public List<PluginType<?>> types() {
        return List.of(GREETING);
    }
}
