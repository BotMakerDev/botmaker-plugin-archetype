package ${package}.plugin.types;

import ${package}.api.Greeting;
import ${package}.plugin.editors.GreetingEditor;
import com.botmaker.plugin.api.slot.ValueContext;
import com.botmaker.plugin.api.value.ComponentType;
import com.botmaker.plugin.toolkit.AbstractPluginType;
import javafx.scene.Node;

import java.util.List;

/**
 * Everything the host needs to know about a {@link Greeting}, in one place.
 *
 * <p>Two questions, two interfaces. {@code PluginType} (through {@code AbstractPluginType}) is <i>what a
 * user picks</i>: the class, what a new one starts as, and how one is edited. {@code ComponentType} is
 * <i>how the host reads and writes the Java</i>: a greeting is written {@code new Greeting("world", 1)}, so
 * the host takes it apart into its two components, writes each by its own rule, and reads them back the same
 * way. Nothing here parses or prints Java — that is the host's job, and it is why a quote inside
 * {@code who} cannot break a bot.
 *
 * <p>A type whose Java is not a call — an enum constant, a {@code String} — implements {@code PluginType}
 * only. {@code botmaker plugin validate} checks that {@code build(components(fresh()))} gives the same
 * components back: a value that changed on the way would be rewritten every time a bot is saved.
 */
public final class GreetingType extends AbstractPluginType<Greeting> implements ComponentType<Greeting> {

    public GreetingType() {
        super(Greeting.class);
    }

    @Override
    public Greeting fresh() {
        return new Greeting("world", 1);
    }

    /**
     * Delegates rather than building the widget here, and that is not style. A headless host — the
     * registry's CI, {@code botmaker plugin validate} — loads this class to ask {@link #fresh()}, and a
     * method in it that handed back a {@code TextField} as a {@code Node} would make the JVM load JavaFX to
     * check that one is the other. {@code GreetingEditor} is loaded only when a greeting is actually drawn.
     */
    @Override
    public Node editor(ValueContext ctx) {
        return GreetingEditor.of(ctx);
    }

    @Override
    public List<Class<?>> componentTypes() {
        return List.of(String.class, int.class);
    }

    @Override
    public List<Object> components(Greeting greeting) {
        return List.of(greeting.who(), greeting.times());
    }

    @Override
    public Greeting build(List<Object> parts) {
        return new Greeting(text(parts, 0), whole(parts, 1));
    }
}
