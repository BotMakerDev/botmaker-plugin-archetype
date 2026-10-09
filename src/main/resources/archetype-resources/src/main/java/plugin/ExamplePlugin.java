package ${package}.plugin;

import ${package}.plugin.editors.ExampleEditors;
import ${package}.plugin.types.ExampleTypes;
import com.botmaker.plugin.api.DeclaredPlugin;
import com.botmaker.plugin.api.StudioPlugin;

/**
 * ${pluginName} — a BotMaker Studio plugin, declared in one expression.
 *
 * <p>Each surface after {@code named} is optional and names what it lists: {@code types} (what a user can
 * hold), {@code parts} (calls inside a value that are not types of their own), {@code editors},
 * {@code values} (values a window of yours keeps in the bot, each marked with an annotation of your own that
 * carries {@code @ManagedMarker}), {@code toolbar} and {@code recorded}.
 * The palette needs nothing: the host finds every {@code @Palette} class in this jar, so {@code ExampleApi}
 * is offered because it carries the annotation.
 *
 * <p>Studio finds this class through {@code META-INF/services/com.botmaker.plugin.api.StudioPlugin} and
 * constructs it with {@code ServiceLoader} while a project is opening, and so does a headless host with no
 * JavaFX. That is why each list is behind a supplier: nothing is built until the host asks for it.
 *
 * <p>This class is wiring only. Declarations live in {@code plugin.types}, editors in {@code plugin.editors},
 * and each toolbar feature gets a {@code plugin.<feature>} package of its own.
 */
public final class ExamplePlugin extends DeclaredPlugin {

    /**
     * The id Studio and the plugin registry know this plugin by. It is <b>not</b> the Maven coordinate: a
     * plugin may be re-published under a new coordinate, and this id must not change when it is. Two
     * plugins may not claim the same one.
     */
    public static final String ID = "${pluginId}";

    /** The name a user reads. */
    public static final String NAME = "${pluginName}";

    public ExamplePlugin() {
        super(StudioPlugin.id(ID).named(NAME)
                .types(() -> ExampleTypes.ALL)
                .editors(() -> ExampleEditors.ALL));
    }
}
