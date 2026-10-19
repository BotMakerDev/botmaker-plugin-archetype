package com.example.greetings.plugin;

import com.example.greetings.api.ExampleApi;
import com.example.greetings.api.Greeting;
import com.example.greetings.plugin.types.ExampleTypes;
import com.example.greetings.plugin.values.ExampleValues;
import com.botmaker.plugin.api.catalog.PaletteCatalog;
import com.botmaker.plugin.api.slot.SlotEditor;
import com.botmaker.plugin.api.source.ManagedValue;
import com.botmaker.plugin.toolkit.testing.TestContexts;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * What can be asserted about a plugin with no host, no project and no JavaFX thread.
 *
 * <p>{@code TestContexts} is the toolkit's recording {@code ValueContext}/{@code SlotContext}: it answers the
 * questions an editor asks and records what the editor writes. Without it the first thing a plugin author has
 * to write is a stub, which is why the half of an editor that is easiest to get wrong — <b>the predicate</b>,
 * and specifically the contexts it must DECLINE — usually goes untested.
 *
 * <p>Building the node itself is not asserted here: {@code Editors} returns real JavaFX controls, and a
 * headless build has no toolkit to construct them on. Run those in an integration test with TestFX, or by
 * loading the plugin in Studio.
 */
class ExamplePluginTest {

    private final ExamplePlugin plugin = new ExamplePlugin();

    /**
     * The host finds {@code ExampleApi} in this jar by its {@code @Palette} and catalogues it exactly like
     * this; {@code botmaker plugin validate} runs the real discovery.
     */
    private static final PaletteCatalog PALETTE = PaletteCatalog.of(ExampleApi.class);

    /**
     * A host finds a plugin only through its services file, so a plugin class renamed or moved without it loads
     * nothing, and nothing else says so.
     */
    @Test
    void the_services_file_names_this_plugin() throws IOException {
        String services = "META-INF/services/com.botmaker.plugin.api.StudioPlugin";
        String name = ExamplePlugin.class.getName();
        try (InputStream in = ExamplePlugin.class.getResourceAsStream("/" + services)) {
            assertNotNull(in, "src/main/resources/" + services + " is missing: write it with the line " + name);
            assertTrue(new String(in.readAllBytes(), StandardCharsets.UTF_8).lines().map(String::strip)
                    .anyMatch(name::equals), "src/main/resources/" + services + " must have the line " + name);
        }
    }

    @Test
    void the_palette_is_well_formed() {
        // problems() is load-time validation, collected rather than thrown: no malformed catalog may be the
        // reason a user's project will not open. An empty list is the assertion worth holding.
        assertTrue(PALETTE.problems().isEmpty(), PALETTE.problems().toString());
    }

    @Test
    void the_hidden_member_is_not_offered() {
        assertTrue(offers("greet"));
        assertFalse(offers("internalHelper"));
    }

    @Test
    void the_greeting_is_the_one_type_this_plugin_owns() {
        assertEquals(1, plugin.types().size());
        assertEquals(Greeting.class, plugin.types().getFirst().type());
    }

    /**
     * The host writes a greeting as its components and reads it back through {@code build}, so a value
     * that did not survive this would be rewritten every time a bot is saved. {@code botmaker plugin
     * validate} asks the same question of every type a plugin declares.
     */
    @Test
    void a_greeting_survives_being_taken_apart_and_put_back() {
        var type = ExampleTypes.GREETING;
        Greeting fresh = type.fresh();

        assertEquals(fresh, type.build(type.components(fresh)));
        assertEquals(new Greeting("it's \"quoted\"", 3),
                type.build(type.components(new Greeting("it's \"quoted\"", 3))));
    }

    /**
     * The one value this plugin keeps in a bot: a parameter Studio writes into the bot's {@code Greetings}
     * class, starting as a greeting the type above can write. {@code botmaker plugin validate} checks the same.
     */
    @Test
    void the_welcome_is_a_parameter_studio_writes_into_the_bot() {
        assertEquals(List.of(ExampleValues.WELCOME), plugin.managedValues());
        ManagedValue<?> welcome = ExampleValues.WELCOME;
        assertTrue(welcome.isParam());
        assertEquals("Greetings", welcome.holder());
        assertEquals(new Greeting("world", 1), welcome.initial());
        assertEquals(welcome.initial(), ExampleTypes.GREETING.build(
                ExampleTypes.GREETING.components(ExampleValues.welcome())));
    }

    @Test
    void the_editor_claims_the_first_argument_of_greet() {
        assertTrue(editor().matches(
                TestContexts.slot(TestContexts.call(ExampleApi::greet), 0, "\"world\"")));
    }

    @Test
    void the_editor_declines_a_second_argument_another_call_and_an_unresolved_one() {
        assertFalse(editor().matches(TestContexts.slot(TestContexts.call(ExampleApi::greet), 1, "\"x\"")));
        assertFalse(editor().matches(
                TestContexts.slot(TestContexts.call(ExampleApi::internalHelper), 0, "\"x\"")));
        assertFalse(editor().matches(TestContexts.slot(null, 0, "\"x\"")));
    }

    @Test
    void the_editor_declines_a_parameters_row() {
        // The case a plugin author never hits by hand: every context in front of you while developing an
        // editor is a slot with a call in it. A row has no call behind it, so a call-site predicate must
        // decline rather than guess.
        assertFalse(editor().matches(TestContexts.row(String.class, "world")));
    }

    private SlotEditor editor() {
        return plugin.slotEditors().get(0);
    }

    private boolean offers(String member) {
        return PALETTE.offers(ExampleApi.class, member);
    }
}
