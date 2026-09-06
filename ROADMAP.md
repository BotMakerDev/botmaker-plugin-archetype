# ROADMAP — botmaker-plugin-archetype

The running engineering log. `CHANGELOG.md` is the short, per-release answer; this is the detail and the
reasoning.

## Done

### 2026-09-02 — JDK 25 LTS, in the module's own pom *and* in the one it writes

Two poms, and the second is the one that matters: `archetype-resources/pom.xml` now says
`maven.compiler.release` 25 and pins `javafx-controls` at 25.0.4, so a plugin generated after this compiles
against 25's platform API rather than against whatever JDK its author happens to run. A generated project
therefore needs a JDK 25 or newer. `jitpack.yml` → `openjdk25` and CI → `java-version: '25'` alongside. The
full account is in `../botmaker-studio-api/ROADMAP.md`, dated the same day.

### 2026-09-06 — the skeleton stopped compiling, and nothing noticed for days

`Source.string` grew a return type — `Expr` rather than `String` — and `ExamplePlugin` still passed
`Source::string` where a codec's literal function wants a `Function<T, String>`. Every generated project
therefore failed on its first `mvn test` with *incompatible types: bad return type in method reference*.
Fixed by calling `Source.string(text).source()`, which is also the truer line: an `Expr` is a Java
expression and `source()` is the text of it.

**The reason it went unnoticed is the reason the check now exists.** The verification recorded below was
done *by hand, once*, against the reactor of that day. Nothing in any build generates a plugin: the only
plugin in the world is compiled from the same reactor as the toolkit it uses, so it cannot be out of step
with it, and the archetype ships **text**, which no compiler reads. `botmaker-cli`'s `ArchetypeSkeletonTest`
closes it — the placeholders are substituted in the test, the skeleton is compiled with `javac`, loaded
through `PluginLoader` and run past every `PluginValidator` check, with no Maven and no network. It is in
the CLI rather than here because that module already has the contract, the loader and the validator; what it
does not cover is this module's own `archetype-metadata.xml`, since nothing there runs velocity.

### 2026-08-28 — the module exists, and the skeleton is checked by building it

Plugin-ecosystem plan, phase 6. The deliverable is a project that comes out of
`mvn archetype:generate` and passes `mvn verify` with **no edits** — verified end to end against the local
reactor (7 tests green), and then loaded through `botmaker-plugin-host`'s `PluginLoader`, which reported
`com.example.myplugin / My Plugin types=1 palette-problems=[]`. That second half matters: it is the first
time anything outside the SDK has been loaded as a plugin at all.

**The skeleton teaches by being right about the things that fail confusingly**, not by being small. The
three dependency scopes are the whole of what an author cannot guess, and each is wrong in a way that
produces no compile error: `provided` on the contract (or a `ClassCastException` between identically named
classes), `compile` on the toolkit (or a `NoClassDefFoundError` at load, because Studio does not have it),
`provided` on JavaFX. The `META-INF/services` file gets a comment saying it must be edited if the class is
renamed, because nothing else will say so.

**The editor is chosen by the call, not by the type**, which is a deliberate choice of example. A type-matched
`String` editor would claim every string in every bot, and an author copying it would ship that. The
call-site form also gives the test its most useful case: the contexts the predicate must **decline** — a
second argument, another method, another class, and a Parameters row. That last one is the half a plugin
author never hits by hand, because every context in front of them while developing has a call in it.

**Three findings worth not rediscovering, all recorded in `CLAUDE.md`:**

- **Velocity eats `##`** — it is a line comment — so a filtered markdown file loses every level-2 heading
  and the rest of its line, silently. The skeleton's `README.md` is unfiltered for that reason and names
  nothing project-specific.
- **Velocity eats `${...}`**, so the generated pom uses literal versions rather than `<properties>` plus
  `${javafx.version}`.
- **`archetype:jar` drops `**/.gitignore`** through the archiver's default excludes, with no warning. Setting
  `addDefaultExcludes=false` on `maven-resources-plugin` gets the file into `target/classes` and **not** into
  the jar, because it is `archetype:jar` and not `maven-jar-plugin` that packages it, and that goal exposes
  no such knob. Every remaining workaround ends with the generated project holding a file named something
  other than `.gitignore`. The skeleton ships none and its README asks for one.

**And one defect in a neighbouring module, found by using this one.** Loading the generated skeleton with the
toolkit left off the classpath threw `NoClassDefFoundError` straight out of `PluginLoader.open` — an `Error`,
so it escaped a `catch (ServiceConfigurationError | RuntimeException)` and would have aborted Studio's
project-open rather than falling back to the bundled plugins. Fixed in `botmaker-plugin-host` by catching
`LinkageError` (deliberately not `Error`), with a test that compiles a plugin against a helper and then
deletes the helper's `.class` — which is exactly the state a jar resolved without its dependency is in.

## Deferred / next

- **`botmaker new` (plan phase 7) shells to this**, rather than carrying its own templates. If that turns out
  to need a property this archetype does not have, add it here.
- **A second skeleton — a type-matched editor and a `ParameterGroup`** — only when somebody wants one. Two
  archetypes is two things to keep building; the generated README points at both mechanisms instead.
- **The `.gitignore` limitation is worth one more look** if the archetype ever moves off
  `maven-archetype` packaging. Do not solve it by renaming the resource.
