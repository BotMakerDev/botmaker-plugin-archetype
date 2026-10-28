package com.example.greetings.plugin;

import com.botmaker.plugin.toolkit.testing.PluginTests;
import org.junit.jupiter.api.Test;

/** The checks a host makes when it loads this plugin, run by {@code mvn verify}. */
class ExamplePluginTest {

    /**
     * A host finds a plugin only through the {@code botmaker.plugin} property of its pom, so a plugin class
     * renamed or moved without it loads nothing, and nothing else says so. The same check constructs the plugin
     * and builds every holder it names, as a host does.
     */
    @Test
    void the_pom_names_this_plugin_and_a_host_can_load_it() {
        PluginTests.assertDeclared(ExamplePlugin.class);
    }
}
