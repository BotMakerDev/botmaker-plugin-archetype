# A BotMaker Studio plugin

Made by `botmaker plugin new` from the example plugin, `botmaker-plugin-archetype`, with your package, plugin id
and name. It compiles and its tests pass as it stands. The class names are still the example's (`ExamplePlugin`,
`Greeting`, …): rename them in your IDE as your plugin takes shape.

# Build it

```bash
mvn verify
```

# Try it in Studio

No release is needed to see a change in Studio:

1. Run `mvn install`. It puts your plugin in `~/.m2` at its `-SNAPSHOT` version.
2. Add it to a bot: `botmaker plugin run --project <bot>` does the install and the pin and opens Studio.
   Or open the bot in Studio and pin the coordinate in *Project ▸ Plugins & Libraries*.
3. Tick **Dev mode** in that window. Studio refuses a `-SNAPSHOT` plugin in any other project. A dev-mode
   project loads it and shows a banner on the canvas while it does.
4. After each rebuild (`mvn install`), press **Reload** in the same window.
5. Publishing the bot refuses a `-SNAPSHOT` pin. Release the plugin, pin its tag, then publish.

Dev mode is stored on this computer only (`.botmaker/settings.json`, not in git), for that one project.

# What is in here

Three packages, and the direction between them only goes one way — `plugin` → `internal` → `api`:

```
api/        what a bot compiles against: the version boundary. No JavaFX, no toolkit.
internal/   what a bot runs but never names (add it when you need it). No JavaFX, no toolkit.
plugin/     the Studio half: the plugin class, types/, editors/, one package per toolbar feature.
```

`plugin/ExamplePlugin` is the plugin itself, one declaration: `StudioPlugin.id(ID).named(NAME)` and then
each surface it fills. Studio finds it through
`src/main/resources/META-INF/services/com.botmaker.plugin.api.StudioPlugin`, which holds its fully-qualified
name. **If you rename or move `ExamplePlugin`, edit that file too**; the skeleton's test says so if you do not.

A plugin does up to three things. The skeleton does each once; read them in this order and delete what you
do not need.

**Level 1, a value type** — a value a bot holds and Studio edits.

- `api/Greeting` — the value, a record.
- `plugin/types/ExampleTypes` — its one declaration, `PluginType.value(Greeting.class)` and its steps: what a
  new one is, its editor, and how its Java is written (`writtenAsRecord()`). The steps only offer what is
  valid next, so the compiler walks you through a new type. `.types(ExampleTypes.class)` takes every type
  field in the class, in field order: add a field and it is declared.
- `plugin/editors/GreetingEditor` — what a greeting looks like when clicked. The editor is named
  `() -> GreetingEditor::of`, with a second arrow, so a host without JavaFX can read the declaration.

**Level 2, a palette block** — a method a bot calls.

- `api/ExampleApi` — every `public` method it declares is offered in Studio's block palette; `@Hidden` takes
  one back out. The plugin class names nothing for it: the host finds every `@Palette` class in the jar.
- `api/Greetee`, `plugin/editors/ExampleEditors` — the annotation on `greet`'s parameter and the editor it
  chooses. Only for a `String` (or any type) whose meaning depends on the parameter; a type of your own uses
  its own editor from level 1.

**Level 3, a managed value** — a value the plugin keeps in the bot's own Java.

- `api/GreetValue` — the plugin's mark (`@ManagedMarker`), with one enum constant per value.
- `plugin/values/ExampleValues` — `WELCOME`, a `@Param` field Studio writes into the bot's `Greetings` class
  when the bot adds the plugin (a bot without one gets it under `plugins/<the last segment of the plugin's
  id>/`). The Parameters window edits it with the greeting's editor, and the bot reads
  it as Java, `Greetings.welcome`. `.values(ExampleValues.class)` takes every value field in the class.

`plugin/ExamplePluginTest` checks each level: the type's round trip, the palette, the editor's predicate
including the contexts it must *decline*, and the managed value.

# The three dependencies, and why their scopes differ

- `botmaker-studio-api` is **`compile`** — level 3 puts `@Param` and `@GreetValue` in a bot's code, so the
  bot needs the contract too. Studio loads the contract from its own copy first, so a contract class is the
  same `Class` object on both sides of the plugin boundary. Without level 3 it may be `provided`.
- `botmaker-plugin-toolkit` is **`compile`** — it is resolved onto *your* plugin's classloader, so your
  version is yours and no other plugin's. Studio does not have it. Do not mark it `provided`.
- `javafx-controls` is **`provided`** — you draw into the host's scene graph.

# The versions are released tags

`pom.xml` pins the `botmaker-studio-api` and `botmaker-plugin-toolkit` tags released with the `botmaker` that
made it (`botmaker.studioapi.version`, `botmaker.plugintoolkit.version`). Move them when Studio moves: a plugin loads in any Studio whose contract has every
member it uses, so build against the oldest contract you mean to support. Do not pin `main-SNAPSHOT` for a
release — the tip of the contract is usually ahead of every released Studio, which refuses the plugin as
*built for a newer Studio*.

# Three rules an editor must keep

1. **A widget takes a `ValueContext` and gets everything else from it** — theming, dialogs, capture and the
   project's paths are all on `ctx.services()`. If you need something that is not there, the *contract* is
   missing it; say so rather than routing around it.
2. **Nothing throws while building a node.** A stored value is text a user may have typed anything into, or
   that a newer version of your plugin wrote. An editor that throws in its constructor leaves a row of the
   Parameters window empty with no explanation, which reads as the host being broken.
3. **Building an editor never writes** — not even to normalise what is already there. A project opened and
   closed must come back byte-identical.

# When a managed value changes shape

A managed value is Java in the bot, marked with your plugin's annotation: a method or a class of constants
your plugin keeps up to date. A release may change it freely: rename its holder, change its type or set kind,
change how its type's parts are written, or drop it. Nothing converts a bot's old values. When a bot upgrades
your plugin, Studio repairs them the way it repairs the rest of the bot's code:

- a value whose id is gone, or that you declare another way now, is removed, or kept unmarked while the bot's
  own code still names it;
- a value that no longer compiles takes its declaration's first value, marked `@Refactor` for review.

Say in your changelog what a bot's author has to redo after the upgrade.

# Next

- Declare each type you own once, in `ExampleTypes`. The class is the identity: a bot holds it as Java, and
  a project opened without your plugin shows that Java read-only rather than losing it. Keep
  `build(components(fresh()))` giving the same components back — `botmaker plugin validate` checks it.
- A type's own editor is its declaration's `.editor(...)`. Add a `SlotEditor` only when the **parameter**
  decides what a value means — a Steam app id and a window title are both `String`: annotate the parameter
  with a `RUNTIME` annotation of your own and declare `SlotEditor.onParameter(YourAnnotation.class).draw(...)`.
- Read the toolkit's Javadoc. Every widget's doc says which mistake it exists to prevent.
