# Declare a plugin in 10 minutes

You know Java and you have this project, which compiles and passes its tests as it stands. This page reads its
code in the order you need it. Every `java` block below is copied from the files it names, as `botmaker plugin new`
made them. Nothing checks them in your project, so they drift from your code as you rename and change it.

A plugin does up to three things: **a value type** a bot holds and Studio edits, **a palette block** a bot calls,
and **a managed value** the plugin keeps in the bot's own Java. Skip a level you do not need.

## The plugin class: wiring only

`plugin/ExamplePlugin.java`:

```java
public ExamplePlugin() {
    super(StudioPlugin.id(ID).named(NAME)
            .types(ExampleTypes.class)
            .values(ExampleValues.class)
            .editors(() -> ExampleEditors.ALL));
}
```

- `ID` is what Studio and the registry know the plugin by. It is not the Maven coordinate, and it never changes.
- `.types(…)` and `.values(…)` name a class. The host takes every `public static final` declaration in it, in
  field order. Add a field and it is declared; nothing else lists it.
- `.editors(…)` stays a list, because the first editor that matches wins: narrowest first.
- Studio finds the class through `src/main/resources/META-INF/services/com.botmaker.plugin.api.StudioPlugin`.
  If you rename or move `ExamplePlugin`, edit that file too. `ExamplePluginTest` fails until you do.

The constructor builds nothing. A headless host (`botmaker plugin validate`, the registry's CI) constructs it with
no JavaFX, and the host reads each class or supplier only when it needs it.

## Level 1: a value type

The value is plain Java in `api/`, which is what a bot compiles against. `api/Greeting.java`:

```java
public record Greeting(String who, int times) {
}
```

Its one declaration, `plugin/types/ExampleTypes.java`:

```java
public static final DeclaredCallType<Greeting> GREETING = PluginType.value(Greeting.class)
        .fresh(() -> new Greeting("world", 1))
        .editor(() -> GreetingEditor::of)
        .writtenAsRecord();
```

Each step offers only what may come next, so the compiler walks you through a new type:

1. `fresh(…)`: what a new one starts as. Use `firstConstant()` for an enum.
2. `editor(…)`: how a person edits one. `preview(…)` may follow.
3. `writtenAs…`: how its Java is written. Use `writtenAsRecord()` for a record and
   `writtenAs(Owner::factory, Type::part, …)` for a factory call, with one accessor per argument. Use
   `writtenAsConstant()` for an enum and `writtenAsLiteral()` for text or a number.

The host writes and reads the Java from those parts. Your plugin never parses or prints Java.

**The second arrow**, `() -> GreetingEditor::of`, is required. A method reference to a method that returns a
JavaFX `Node` loads JavaFX the moment it is evaluated. The arrow delays that until Studio draws, and a headless
host never does. A plain `GreetingEditor::of` does not compile.

The editor is handed the value, never text. `plugin/editors/GreetingEditor.java`:

```java
public static Node of(ValueContext ctx) {
    Greeting current = ctx.value(Greeting.class).orElseGet(() -> new Greeting("", 1));
    return Fields.committing(current.who(), "Who to greet",
            who -> ctx.set(new Greeting(who, current.times())));
}
```

`ctx.value(…)` is empty when the bot's Java is something the host cannot take apart, such as a variable. Building
an editor never writes and never throws.

## Level 2: a palette block

`api/ExampleApi.java` carries `@Palette(category = …, categoryLabel = …, icon = …)`. Every `public` method of a
`@Palette` class is offered in Studio's block palette, and `@Hidden` takes one back out. The plugin class does not
name it: the host finds every `@Palette` class in the jar.

```java
public static String greet(@Greetee String who) {
    return "Hello, " + who + "!";
}
```

An argument of one of your types gets its type's editor. A `String` needs help, because every `String` looks
alike. `@Greetee` (in `api/`, `RUNTIME` retention) marks the parameter, and `plugin/editors/ExampleEditors.java`
claims it:

```java
public static final List<SlotEditor> ALL = List.of(
        SlotEditor.onParameter(Greetee.class).draw(() -> GreetingEditor::who));
```

## Level 3: a managed value

A value the plugin keeps in the bot's own Java, here a parameter. Studio writes the field when a bot adds the
plugin, and finds it again by its mark, never by its name. The mark is `api/GreetValue.java`:

```java
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
```

The declaration is `plugin/values/ExampleValues.java`:

```java
public static final ManagedValue<Greeting> WELCOME = ManagedValue.param(GreetValue.Id.WELCOME)
        .in("Greetings")
        .startsAs(ExampleValues::welcome)
        .shown("Greetings", "Who the bot greets first", Param.Visibility.PUBLIC)
        .because(ExamplePlugin.NAME
                + " reads this parameter. Change its value in the Runner or Project ▸ Parameters.");
```

The steps are, in order:

1. the mark's id;
2. the bot class the field goes in;
3. its first value, as a method reference;
4. where the Parameters window lists it;
5. the sentence a user reads when Studio keeps them from renaming or deleting it.

The bot reads the value as plain Java, `Greetings.welcome`. A bot without a `Greetings` class gets one in its
package `plugins.<the last segment of your plugin id>`. This is why the contract is a `compile` dependency in
`pom.xml`: the bot's code carries `@GreetValue` and `@Param`.

## Two more you will meet

**A value the bot fills in.** Some values come from a run, such as the last match a search found: a bot holds
one, but nobody edits it. Declare none in `types`. Mark the public static no-argument method in `api/` that
returns it, and the host declares the type, writes a fresh one as that call, and draws it as a pill. From the SDK:

```java sdk
@Fills(label = "Picture match")
public static MatchResult lastMatch() { … }
```

**An editor that needs its own declaration.** An editor that edits a value part by part (a row of numbers for a
point) asks the host for the declaration it edits. It does not name the field that holds it. From the SDK:

```java sdk
return ctx.declared(type)
        .<Node>map(declared -> Editors.tuplePill(ctx, declared, spec, SdkScreenPicks.forSlot(ctx)))
        .orElseGet(() -> Pills.bare(Slots.sourceOr(ctx, spec.placeholder())));
```

`ctx.declared(…)` is empty on a host older than the method. The editor then shows the value read-only.

## Check it

```bash
mvn verify                              # the tests in plugin/ExamplePluginTest
botmaker plugin validate                # what a host checks on load: each type's round trip, headless
botmaker plugin run --project <bot>     # install, pin it in that project and open Studio
```

A `-SNAPSHOT` plugin loads only once you tick **Dev mode** in that project's *Project ▸ Plugins & Libraries*.

When a check fails, its message names the line to write. `README.md` covers dev mode, the dependency scopes and
what happens to a bot's values when your plugin changes.
