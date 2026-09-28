package ${package}.plugin.editors;

import ${package}.api.ExampleApi;
import com.botmaker.plugin.api.slot.SlotEditor;
import com.botmaker.plugin.toolkit.Editors;

import java.util.List;

/**
 * The editors a type cannot choose for itself. An editor for one of this plugin's own types is not here — it
 * is declared beside the type, in {@code ExampleTypes}.
 *
 * <p>This one is chosen by the <b>call</b> rather than by the type, which is the only way to tell the first
 * argument of {@code ExampleApi.greet} apart from every other {@code String} in a bot. A call-site editor is
 * absent from the Parameters window by construction — a row has no call behind it — and
 * {@code SlotEditor.forCall} declines there rather than guessing. {@code SlotEditor.calls} checks when it is
 * built that {@code ExampleApi} declares a public {@code greet}, so renaming it fails this plugin's tests
 * rather than hiding the editor.
 */
public final class ExampleEditors {

    private ExampleEditors() {}

    /** Every editor this plugin offers, narrowest match first. */
    public static final List<SlotEditor> ALL = List.of(
            SlotEditor.forCall(SlotEditor.calls(ExampleApi.class, "greet"), 0,
                    ctx -> Editors.text(ctx, "Who to greet")));
}
