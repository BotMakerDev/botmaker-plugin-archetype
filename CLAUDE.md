# CLAUDE.md

Guidance for working in **botmaker-plugin-archetype**, the example plugin `botmaker plugin new` copies.

Read the umbrella `../CLAUDE.md` first, `../botmaker-studio-api/CLAUDE.md` for the contract the example
implements, `../botmaker-plugin-toolkit/CLAUDE.md` for the widgets it uses, and
`../docs/refactor/24-plugin-platform.md` for why any of it exists. `../docs/refactor/46-plugin-target.md` is the
plugin this example is meant to be.

## What this module is

**A plugin, and the single source of truth for what a new plugin looks like.** It was a `maven-archetype` of
Velocity templates until 2026-10-19; nothing built them, so the skeleton drifted from the toolkit twice before a
test caught it, Velocity ate every `##` heading, and the jar could not carry a `.gitignore`. Now:

- `src/main`, `src/test`: the example, package `com.example.greetings`, name `Greeter` (the pom's `<name>` and
  its palette label).
  The pom's `botmaker.plugin` property names `ExamplePlugin`; the pom's coordinate is the plugin's identity and
  its `<name>` what Studio shows (2026-10-27): the class names neither.
  `mvn verify` compiles it and runs its 9 tests.
- `pom.xml` is both this module's build and the new plugin's pom. Two regions are marked for the copy:
  `<!-- botmaker plugin new: coordinates -->` (replaced by the new plugin's coordinates and name) and
  `<!-- botmaker plugin new: drop -->` (removed: the assembly that zips the template). Everything outside them
  reaches every new plugin, so write it for a plugin author.
- `src/template/README.md`: the README a new plugin gets. `README.md` at the root is this repository's.
- `src/template/DECLARE.md`: "Declare a plugin in 10 minutes", the one page an outside author reads. Each
  ```` ```java ```` block is a verbatim quote of `src/main/java` (dedent allowed), checked by `DeclareDocTest`;
  ```` ```java sdk ```` quotes another project and is not checked. Edit the example, then the page. It is not
  renamed on copy, so it quotes no line holding the package, the quoted id or the quoted name.
- `src/assembly/template.xml`: the `template` zip (classifier `template`): the pom, `src/main`, `src/test`
  without `DeclareDocTest`, the template README and `DECLARE.md`, and `.gitignore` stored as `gitignore`.

`botmaker-cli` bundles the zip (`maven-dependency-plugin:copy` into its jar) and its `PluginTemplate` renames on
copy. **The copy finds what to rename by anchors this module keeps**; a template without one is refused:

| Anchor | Where | Renamed to |
|---|---|---|
| `com.example.greetings` | every `.java`, the pom's `botmaker.plugin`, the source directories | the new package |
| `"Greeter"` (quoted) | `ExampleApi`'s palette label | the plugin name |
| the two marked regions | `pom.xml` | coordinates; nothing |

So: never spell the package, the quoted id or the quoted name anywhere they should survive the rename, and keep
`api` free of `plugin` (the palette label is a literal for that reason). `PluginTemplateTest` in botmaker-cli
renames the bundled zip, compiles it, loads it and runs every validator check.

## The pins

One: the `<parent>`, `botmaker-plugin-parent` (2026-10-27), which pins the contract, the toolkit and JavaFX
together and flattens the published pom. The release moves its `<version>` like any pin: `main`'s `-SNAPSHOT`
here (found by `relativePath` in the umbrella), its released tag on a tag commit, which is what a released
`botmaker` hands to a new plugin. The parent forces this module (the contract and the toolkit force the parent);
this module forces the cli, which bundles it. The dependencies name no version and javafx no scope: the parent's
`dependencyManagement` says both.

## The scopes the example exists to get right

They are what a plugin author cannot guess, and each is wrong in a way that produces a confusing failure rather
than a compile error:

| dependency | scope | what a wrong scope does |
|---|---|---|
| `botmaker-studio-api` | `compile` | Level 3 puts `@Param` and `@GreetValue` (a `@ManagedMarker`) on a bot's own code, so the bot needs the contract, as the SDK's bots do. Harmless in Studio: `PluginLoader` is parent-first for the contract, and `pom-scopes` accepts both. A plugin that marks no bot code may make it `provided`. |
| `botmaker-plugin-toolkit` | `compile` | `provided` compiles fine and then `NoClassDefFoundError`s at load, because Studio does not have the toolkit. |
| `javafx-controls` | `provided` | `compile` puts a second JavaFX in a process that has one. |

## Building

```bash
mvn -pl botmaker-plugin-archetype -am install    # umbrella root: contract, toolkit, then this
mvn -pl botmaker-cli test -Dtest=PluginTemplateTest   # the renamed copy compiles, loads and validates
```

IntelliJ opens this module like any other in the umbrella; edit the example directly.

## Style

The example's comments are the deliverable. A plugin author reads them before any documentation, so every one
says which *mistake* it prevents: that renaming `ExamplePlugin` needs the pom's `botmaker.plugin` edited, that a type's
editor is delegated so a headless host can load its declaration, that a call-site predicate must decline a
Parameters row. That is the same rule the contract and the toolkit keep.

Published through JitPack. Releases are cut from the umbrella with `../release.sh --plugin-archetype <version>`.
