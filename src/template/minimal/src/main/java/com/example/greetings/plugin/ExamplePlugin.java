package com.example.greetings.plugin;

import com.botmaker.plugin.api.DeclaredPlugin;
import com.botmaker.plugin.api.StudioPlugin;

/**
 * A BotMaker Studio plugin that declares nothing yet.
 *
 * <p>Each surface is one step on {@code declare()}, naming the class that holds it: {@code .types(MyTypes.class)},
 * {@code .values(MyValues.class)}, {@code .editors(MyEditors.class)}, {@code .toolbar(MyToolbar.class)}. The class
 * implements the step's holder interface ({@code TypeHolder}, {@code ValueHolder}, …), whose one method lists what
 * it gives. {@code DECLARE.md} walks through each; {@code botmaker plugin new} without {@code --minimal} makes a
 * plugin that uses three of them.
 *
 * <p>Studio finds this class through the {@code botmaker.plugin} property of {@code pom.xml}, and knows the
 * plugin by that pom's {@code groupId:artifactId}. If you rename or move this class, change the property too;
 * the test fails until you do. Studio constructs it while a project is opening, and so does a headless host with
 * no JavaFX, so build nothing here.
 */
public final class ExamplePlugin extends DeclaredPlugin {

    public ExamplePlugin() {
        super(StudioPlugin.declare());
    }
}
