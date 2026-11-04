package com.example.greetings.api;

import com.botmaker.plugin.api.managed.ManagedMarker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Level 3, a managed value:</b> the mark on a value this plugin keeps in a bot's own Java.
 *
 * <p>Studio writes the bot's parameter with this mark on it, and finds it again by the mark, never by the
 * method's name. A parameter is a method of the plugin's group, the {@code @Param} class Studio writes in the
 * bot's {@code params/<groupId>/<artifactId>/} folder, private to each computer. {@code ExampleValues.WELCOME}
 * declares the value {@link Id#WELCOME} stands for:
 *
 * <pre>{@code
 * @Param
 * public class Greetings {
 *     @GreetValue(GreetValue.Id.WELCOME)
 *     @Param.Shown(category = "Greetings", description = "Who the bot greets first")
 *     public Greeting welcome() {
 *         return Greeting.to("world");
 *     }
 * }
 * }</pre>
 *
 * <p>Each part is required: {@link ManagedMarker} makes this the plugin's mark, {@code RUNTIME} lets Studio see
 * it, and the nested {@link Id} enum names every value the plugin keeps, one constant each. {@code METHOD} is
 * where this plugin's values sit, parameters and {@code ManagedValue.method} values alike.
 */
@Documented
@ManagedMarker
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GreetValue {

    Id value();

    /** Every value this plugin keeps in a bot. */
    enum Id {
        WELCOME
    }
}
