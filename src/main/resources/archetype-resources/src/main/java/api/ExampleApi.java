package ${package}.api;

import com.botmaker.plugin.api.palette.Hidden;
import com.botmaker.plugin.api.palette.Palette;

/**
 * The API this plugin offers a bot, and the class its palette entries are read off.
 *
 * <p><b>Curation is opt-out.</b> Every {@code public} method declared here is offered in Studio's block
 * palette; {@link Hidden} takes one back out. Nothing anywhere lists member names as strings, so renaming
 * {@link #greet} renames the palette entry and cannot leave a catalog pointing at a member that no longer
 * exists.
 *
 * <p>The unit of a palette entry is the member <em>name</em>, not the overload: two {@code greet} methods
 * make one menu entry with a submenu, not two entries.
 *
 * <p>A bot only ever writes down the names in {@code api}, so treat this package as the version boundary —
 * you may add to it freely, and anything you remove breaks a bot that already spelled it. What a bot needs
 * but never names goes in {@code internal}; what only Studio needs goes in {@code plugin}, which is the only
 * package that may touch JavaFX or the toolkit.
 */
@Palette(category = "util", categoryLabel = "${pluginName}", icon = "👋", order = 100)
public final class ExampleApi {

    private ExampleApi() {
    }

    /**
     * Says hello. Replace this with the first thing your plugin actually does.
     *
     * <p>The first argument of this call is what {@code ExamplePlugin}'s slot editor draws. That editor is chosen by <em>the call</em> rather than by the argument's type,
     * which is the only way to tell this {@code String} apart from every other {@code String} in a bot.
     */
    public static String greet(String who) {
        return "Hello, " + who + "!";
    }

    /** Not offered in the palette: {@link Hidden} is how a public method stays public and stays out. */
    @Hidden
    public static String internalHelper(String who) {
        return who == null ? "" : who.strip();
    }
}
