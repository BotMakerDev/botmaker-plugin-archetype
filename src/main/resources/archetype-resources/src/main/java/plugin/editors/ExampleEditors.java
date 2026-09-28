package ${package}.plugin.editors;

import ${package}.api.Greetee;
import com.botmaker.plugin.api.slot.SlotEditor;

import java.util.List;

/**
 * The editors a type cannot choose for itself. An editor for one of this plugin's own types is not here — it
 * is declared beside the type, in {@code ExampleTypes}.
 *
 * <p>This one is chosen by the <b>parameter</b> rather than by the type: {@code ExampleApi.greet} marks its
 * argument {@code @Greetee}, which is the only way to tell it apart from every other {@code String} in a bot.
 * An editor chosen this way is absent from the Parameters window by construction — a row has no call behind
 * it — and declines there rather than guessing. {@code onParameter} refuses an annotation reflection cannot
 * see, so a missing {@code RUNTIME} fails this plugin's tests rather than hiding the editor.
 *
 * <p>The drawing is {@code () -> GreetingEditor::who}: the extra arrow keeps building this list from loading
 * JavaFX, which a headless host ({@code botmaker plugin validate}) does not have.
 */
public final class ExampleEditors {

    private ExampleEditors() {}

    /** Every editor this plugin offers, narrowest match first. */
    public static final List<SlotEditor> ALL = List.of(
            SlotEditor.onParameter(Greetee.class).draw(() -> GreetingEditor::who));
}
