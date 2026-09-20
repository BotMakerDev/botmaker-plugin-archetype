package ${package};

import com.botmaker.plugin.api.SlotEditor;
import com.botmaker.plugin.api.catalog.PaletteCatalog;
import com.botmaker.plugin.api.value.ValueCatalog;
import com.botmaker.plugin.api.value.ValueType;
import com.botmaker.plugin.toolkit.AbstractStudioPlugin;
import com.botmaker.plugin.toolkit.CallSites;
import com.botmaker.plugin.toolkit.Codecs;
import com.botmaker.plugin.toolkit.Editors;
import com.botmaker.plugin.toolkit.Source;

import java.util.List;

/**
 * ${pluginName} — a BotMaker Studio plugin.
 *
 * <p>Studio finds this class through {@code META-INF/services/com.botmaker.plugin.api.StudioPlugin} and
 * constructs it with {@code ServiceLoader}, which is why nothing expensive belongs in a constructor or a
 * field initialiser: that happens while a project is opening, whether or not the answer is ever wanted.
 * {@code AbstractStudioPlugin}'s {@code build…} hooks run at most once, and only when the host asks.
 *
 * <p>Three of the four contribution surfaces are shown below. The fourth, {@code parameters(pin)}, adds rows
 * to Studio's Parameters window and is left out because it is the one you are least likely to want first.
 */
public final class ExamplePlugin extends AbstractStudioPlugin {

    /**
     * The id Studio and the plugin registry know this plugin by. It is <b>not</b> the Maven coordinate: a
     * plugin may be re-published under a new coordinate, and this id must not change when it is, because a
     * project's stored data refers to it. Two plugins may not claim the same one.
     */
    public static final String ID = "${pluginId}";

    /**
     * A value type this plugin registers, so a project variable can be one.
     *
     * <p>The <b>id is the identity</b> — it is what is written into {@code activities.json} — so it must
     * never change once a project has stored it, and it must not collide with another plugin's. Prefix it
     * with something of your own, as here.
     *
     * <p>A project opened without this plugin installed keeps values of this type as raw text, renders them
     * read-only and declines to emit them. That is not an error state; it is the ordinary consequence of an
     * open vocabulary, and it is why the id has to outlive the class.
     */
    public static final ValueType GREETING = ValueType.of(ID + ".greeting")
            .label("Greeting")
            .group("${pluginName}")
            .source("String")
            .build();

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
     * The value vocabulary: what a project variable may be.
     *
     * <p>A codec is per <em>item</em> — parse a stored string, store one back, render a Java literal and
     * read that literal back. A composite (a list, a map) is composed above it, so one codec serves every
     * shape without knowing they exist. {@code Codecs.or} is what makes a partial parser total: a reader of
     * a stored value must degrade rather than throw, because a value the user typed can be anything and an
     * editor that throws while building leaves a row of the Parameters window empty.
     *
     * <p>The third function is the <b>Java literal</b> the generated bot will compile, so it goes through
     * {@code Source} rather than through string concatenation of your own: a value can contain a quote, a
     * backslash or a pasted newline, and each of those hand-escaped wrongly is a compile error in somebody
     * else's bot. {@code Source.string} hands back an {@code Expr} — a Java expression, not merely text —
     * and {@code source()} is the text of it, which is what a codec's literal is declared to return.
     *
     * <p>The fourth is that literal read backwards, and it is <b>not optional</b>: a user parameter is a
     * field in the bot's own Java, so a type that cannot read its own literal is one the editor writes once
     * and thereafter shows read-only. Write it in the same expression as the literal it undoes. Declining is
     * still a legitimate answer for a <em>particular</em> source — a hand-written expression this plugin
     * never emits — and the host then shows that source as it stands.
     */
    @Override
    protected ValueCatalog buildValueTypes() {
        return ValueCatalog.builder()
                .add(GREETING, Codecs.or(Codecs.of(
                        wire -> wire,
                        stored -> stored,
                        text -> Source.string(text).source(),
                        java -> Source.stringValue(java)), ""))
                .build();
    }

    /**
     * The editors: what a value looks like when a bot author clicks it.
     *
     * <p>This one is chosen by the <b>call</b> rather than by the type, which is the only way to tell the
     * first argument of {@code ExampleApi.greet} apart from every other {@code String} in a bot. A call-site
     * editor is absent from the Parameters window by construction — a row has no call behind it — and
     * {@code CallSites} declines there rather than guessing.
     *
     * <p>The other kind matches on type: {@code ctx -> ctx.type().is(MyThing.class)}. Use that for a value
     * that means the same thing everywhere it appears.
     */
    @Override
    protected List<SlotEditor> buildSlotEditors() {
        return List.of(SlotEditor.of(
                CallSites.firstArgumentOf(ExampleApi.class, "greet"),
                ctx -> Editors.text(ctx, "Who to greet")));
    }
}
