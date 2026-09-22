package ${package};

import com.botmaker.plugin.api.slot.SlotEditor;
import com.botmaker.plugin.toolkit.testing.TestContexts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    void the_palette_is_well_formed() {
        // problems() is load-time validation, collected rather than thrown: no malformed catalog may be the
        // reason a user's project will not open. An empty list is the assertion worth holding.
        assertTrue(plugin.catalog().problems().isEmpty(), plugin.catalog().problems().toString());
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
        GreetingType type = new GreetingType();
        Greeting fresh = type.fresh();

        assertEquals(fresh, type.build(type.components(fresh)));
        assertEquals(new Greeting("it's \"quoted\"", 3),
                type.build(type.components(new Greeting("it's \"quoted\"", 3))));
    }

    @Test
    void the_editor_claims_the_first_argument_of_greet() {
        assertTrue(editor().matches(
                TestContexts.slot("ExampleApi", "greet", 0, "\"world\"")));
    }

    @Test
    void the_editor_declines_a_second_argument_and_another_call() {
        assertFalse(editor().matches(TestContexts.slot("ExampleApi", "greet", 1, "\"x\"")));
        assertFalse(editor().matches(TestContexts.slot("ExampleApi", "somethingElse", 0, "\"x\"")));
        assertFalse(editor().matches(TestContexts.slot("SomeOtherClass", "greet", 0, "\"x\"")));
    }

    @Test
    void the_editor_declines_a_parameters_row() {
        // The case a plugin author never hits by hand: every context in front of you while developing an
        // editor is a slot with a call in it. A row has no call behind it, so a call-site predicate must
        // decline rather than guess.
        assertFalse(editor().matches(TestContexts.row("String", "world")));
    }

    private SlotEditor editor() {
        return plugin.slotEditors().get(0);
    }

    private boolean offers(String member) {
        return plugin.catalog().offers(ExampleApi.class, member);
    }
}
