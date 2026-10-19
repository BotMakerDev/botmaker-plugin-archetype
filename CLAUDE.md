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

- `src/main`, `src/test`: the example, package `com.example.greetings`, plugin id the same, name `Greeter`.
  `mvn verify` compiles it and runs its 9 tests.
- `pom.xml` is both this module's build and the new plugin's pom. Two regions are marked for the copy:
  `<!-- botmaker plugin new: coordinates -->` (replaced by the new plugin's coordinates and name) and
  `<!-- botmaker plugin new: drop -->` (removed: the assembly that zips the template). Everything outside them
  reaches every new plugin, so write it for a plugin author.
- `src/template/README.md`: the README a new plugin gets. `README.md` at the root is this repository's.
- `src/assembly/template.xml`: the `template` zip (classifier `template`): the pom, `src/main`, `src/test`, the
  template README, and `.gitignore` stored as `gitignore`.

`botmaker-cli` bundles the zip (`maven-dependency-plugin:copy` into its jar) and its `PluginTemplate` renames on
copy. **The copy finds what to rename by anchors this module keeps**; a template without one is refused:

| Anchor | Where | Renamed to |
|---|---|---|
| `com.example.greetings` | every `.java`, the services file, the source directories | the new package |
| `"com.example.greetings"` (quoted, exactly once) | `ExamplePlugin.ID` | the plugin id |
| `"Greeter"` (quoted) | `ExamplePlugin.NAME`, `ExampleApi`'s palette label | the plugin name |
| the two marked regions | `pom.xml` | coordinates; nothing |

So: never spell the package, the quoted id or the quoted name anywhere they should survive the rename, and keep
`api` free of `plugin` (the palette label is a literal for that reason). `PluginTemplateTest` in botmaker-cli
renames the bundled zip, compiles it, loads it and runs every validator check.

## The pins

`botmaker.studioapi.version` and `botmaker.plugintoolkit.version`, moved by the release like every module's:
`main`'s `-SNAPSHOT` here, released tags on a tag commit, which is what a released `botmaker` hands to a new
plugin. studio-api and the toolkit force this module; this module forces the cli, which bundles it.

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
says which *mistake* it prevents: that renaming `ExamplePlugin` needs the services file edited, that a type's
editor is delegated so a headless host can load its declaration, that a call-site predicate must decline a
Parameters row. That is the same rule the contract and the toolkit keep.

Published through JitPack. Releases are cut from the umbrella with `../release.sh --plugin-archetype <version>`.
