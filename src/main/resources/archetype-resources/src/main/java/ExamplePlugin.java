package ${package};

import com.botmaker.plugin.api.catalog.PaletteCatalog;
import com.botmaker.plugin.api.slot.SlotEditor;
import com.botmaker.plugin.api.value.PluginType;
import com.botmaker.plugin.toolkit.AbstractStudioPlugin;
import com.botmaker.plugin.toolkit.Editors;

import java.util.List;

/**
 * ${pluginName} — a BotMaker Studio plugin.
 *
 * <p>Studio finds this class through {@code META-INF/services/com.botmaker.plugin.api.StudioPlugin} and
 * constructs it with {@code ServiceLoader}, which is why nothing expensive belongs in a constructor or a
 * field initialiser: that happens while a project is opening, whether or not the answer is ever wanted.
 * {@code AbstractStudioPlugin}'s {@code build…} hooks run at most once, and only when the host asks.
 *
 * <p>Three contribution surfaces are shown below: the palette, the types this plugin owns, and an editor
 * chosen by the call it sits in.
 */
public final class ExamplePlugin extends AbstractStudioPlugin {

    /**
     * The id Studio and the plugin registry know this plugin by. It is <b>not</b> the Maven coordinate: a
     * plugin may be re-published under a new coordinate, and this id must not change when it is. Two
     * plugins may not claim the same one.
     */
    public static final String ID = "${pluginId}";

    public ExamplePlugin() {
        super(ID, "${pluginName}");
    }

    /**
     * The palette: what a bot author is offered in the block menus.
     *
     * <p>Built by reflection over the classes named here, so the class list is compiler-checked and the
     * member list is discovered. Add a facade by adding its class literal.
     */
    @Override
    protected PaletteCatalog buildCatalog() {
        return PaletteCatalog.of(ExampleApi.class);
    }

    /**
     * The types this plugin owns: one declaration each, and the host does the rest.
     *
     * <p>A type listed here is offered when a user adds a parameter, drawn by its own editor wherever a
     * value of it appears, and written into the bot as Java by the host. The class is the identity, so no
     * other plugin may list {@link Greeting} — a host loading both would leave one of them out.
     */
    @Override
    protected List<PluginType<?>> buildTypes() {
        return List.of(new GreetingType());
    }

    /**
     * The other kind of editor: chosen by the <b>call</b> rather than by the type, which is the only way to
     * tell the first argument of {@code ExampleApi.greet} apart from every other {@code String} in a bot.
     *
     * <p>A call-site editor is absent from the Parameters window by construction — a row has no call behind
     * it — and {@code SlotEditor.forCall} declines there rather than guessing.
     */
    @Override
    protected List<SlotEditor> buildSlotEditors() {
        return List.of(SlotEditor.forCall(ExampleApi.class, 0, ctx -> Editors.text(ctx, "Who to greet"),
                "greet"));
    }
}
