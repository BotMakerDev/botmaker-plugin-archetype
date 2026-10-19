package ${package}.api;

import com.botmaker.plugin.api.managed.ManagedMarker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Level 3, a managed value:</b> the mark on a value this plugin keeps in a bot's own Java.
 *
 * <p>Studio writes the bot's field with this mark on it, and finds it again by the mark, never by the field's
 * name, so a bot's author may move the field's class around. {@code ExampleValues.WELCOME} declares the value
 * {@link Id#WELCOME} stands for:
 *
 * <pre>{@code
 * public final class Greetings {
 *     @GreetValue(GreetValue.Id.WELCOME)
 *     @Param(category = "Greetings", visibility = Param.PUBLIC, description = "Who the bot greets first")
 *     public static Greeting welcome = new Greeting("world", 1);
 * }
 * }</pre>
 *
 * <p>Each part is required: {@link ManagedMarker} makes this the plugin's mark, {@code RUNTIME} lets Studio see
 * it, and the nested {@link Id} enum names every value the plugin keeps, one constant each. {@code FIELD} is
 * the one place this plugin's values sit; add {@code METHOD} when you declare a {@code ManagedValue.method}.
 */
@Documented
@ManagedMarker
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GreetValue {

    Id value();

    /** Every value this plugin keeps in a bot. */
    enum Id {
        WELCOME
    }
}
