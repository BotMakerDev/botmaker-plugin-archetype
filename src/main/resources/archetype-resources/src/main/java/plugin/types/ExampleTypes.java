package ${package}.plugin.types;

import ${package}.api.Greeting;
import ${package}.plugin.editors.GreetingEditor;
import com.botmaker.plugin.api.value.DeclaredCallType;
import com.botmaker.plugin.api.value.PluginType;

import java.util.List;

/**
 * The types this plugin owns, each declared once as a {@code PluginType.value(…)} whose steps ask, in order,
 * for everything the host needs — the compiler walks you through it:
 *
 * <ol>
 *   <li>{@code fresh(…)} — what a new one starts as ({@code firstConstant()} for an enum, or
 *       {@code filledBy(Owner::method)} for a value the bot fills in itself);</li>
 *   <li>{@code editor(…)} — how a person edits one, then optionally {@code preview(…)};</li>
 *   <li>{@code writtenAs…} — how its Java is written: {@code writtenAsRecord()} for a record, as here;
 *       {@code writtenAs(Owner::factory, Type::part, …)} for a call, one accessor per argument;
 *       {@code writtenAsEach(…)} for a varargs call; {@code writtenAsConstant()} for an enum;
 *       {@code writtenAsLiteral()} for text or a number.</li>
 * </ol>
 *
 * <p>Nothing here parses or prints Java — the host does, from the parts — which is why a quote inside
 * {@code who} cannot break a bot. {@code botmaker plugin validate} checks that {@code build(components(v))}
 * gives {@code v} back: a value that changed on the way would be rewritten every time a bot is saved.
 *
 * <p><b>The editor is named behind a second arrow</b>, {@code () -> GreetingEditor::of}. A headless host — the
 * registry's CI, {@code botmaker plugin validate} — builds this list with no JavaFX, and a reference to a
 * method returning a {@code Node} would load JavaFX the moment the list is built.
 */
public final class ExampleTypes {

    private ExampleTypes() {}

    /** A greeting, written {@code new Greeting("world", 1)}. */
    public static final DeclaredCallType<Greeting> GREETING = PluginType.value(Greeting.class)
            .fresh(() -> new Greeting("world", 1))
            .editor(() -> GreetingEditor::of)
            .writtenAsRecord();

    /**
     * Every type this plugin owns, in the order a picker offers them. The class is the identity, so no other
     * plugin may list {@link Greeting} — a host loading both would leave one of them out.
     */
    public static final List<PluginType<?>> ALL = List.of(GREETING);
}
