package ${package}.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a parameter that takes who to greet.
 *
 * <p>Nothing reads it while a bot runs. It is how this plugin tells Studio which {@code String} arguments its
 * editor draws: {@code ExampleEditors} claims every argument passed to a parameter carrying it. It must be
 * {@code RUNTIME}, or reflection cannot see it and the editor refuses to be built.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Greetee {}
