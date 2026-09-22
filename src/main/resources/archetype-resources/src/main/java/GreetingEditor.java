package ${package};

import com.botmaker.plugin.api.slot.ValueContext;
import com.botmaker.plugin.toolkit.Fields;
import javafx.scene.Node;

/**
 * What a {@link Greeting} looks like when a bot author clicks one.
 *
 * <p>The editor is handed the value, never text: {@code ctx.value(Greeting.class)} is the greeting the
 * host read out of the bot's source, and {@code ctx.set(…)} hands a new one back for the host to write.
 * When the source is something the host cannot take apart — a variable, a method call somebody wrote by
 * hand — {@code value} is empty, and this starts from a blank greeting. Building it writes nothing: the
 * author's expression stays until they commit a new name.
 */
final class GreetingEditor {

    private GreetingEditor() {
    }

    static Node of(ValueContext ctx) {
        Greeting current = ctx.value(Greeting.class).orElseGet(() -> new Greeting("", 1));
        return Fields.committing(current.who(), "Who to greet",
                who -> ctx.set(new Greeting(who, current.times())));
    }
}
