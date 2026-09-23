# Changelog

All notable changes to `botmaker-plugin-archetype`.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this module uses
[semantic versioning](https://semver.org/). `release.sh` refuses to cut a version with no section here.

## [Unreleased]

### Changed

- **`ExamplePlugin` no longer overrides `buildCatalog()`.** The host finds every `@Palette` class in the
  plugin's jar, so `ExampleApi` is offered because it is annotated, and a new facade needs no line in the
  plugin class. `ExamplePluginTest` checks the annotations with `PaletteCatalog.of(ExampleApi.class)`.

- **The skeleton is laid out as the standard plugin package tree.** `api/` holds `ExampleApi` and
  `Greeting` (what a bot names), `plugin/` holds `ExamplePlugin` (wiring only), `plugin/types/GreetingType`
  and `plugin/editors/GreetingEditor`. The services file names `${package}.plugin.ExamplePlugin`. The rule
  the tree encodes — `plugin` → `internal` → `api`, and JavaFX or the toolkit only under `plugin` — is
  the umbrella's `docs/refactor/34-plugin-package-tree.md`, and the SDK is being moved onto the same tree.

- **The skeleton declares one type, and it is the documentation.** `buildValueTypes()`, the `GREETING`
  `ValueType` and its `Codecs.or(Codecs.of(…))` block are gone with the contract's value vocabulary. In their
  place are `Greeting` (a record a bot holds as a `@Param` field), `GreetingType` (one declaration: what a
  new one is and the components the host writes it as, through the toolkit's `AbstractPluginType`) and
  `GreetingEditor` (the widget, kept apart so a headless host can load the declaration without JavaFX).
  `ExamplePluginTest` asserts the round trip `build(components(v)) == v` where it asserted a codec's
  literal, and the call-site editor is `SlotEditor.forCall`. Still seven tests, still green unedited.

## [0.0.7] — 2026-09-21

### Changed

- **The generated plugin imports the contract's new packages.** `ExamplePlugin` and its test take
  `SlotEditor`, `SlotContext` and `ValueContext` from `com.botmaker.plugin.api.slot` now. A skeleton
  generated from an earlier archetype against `botmaker-studio-api` v0.1.6 will not compile — repoint the
  imports with the table in `botmaker-studio-api`'s changelog. The skeleton's shape is unchanged.

## [0.0.6] — 2026-09-21

No source changes since v0.0.5; re-released for updated upstream pins.

No source changes since v0.0.4; re-released for updated upstream pins.

No source changes since v0.0.3; re-released for updated upstream pins.

### Fixed

- **The generated skeleton compiles again.** `ExamplePlugin`'s value codec passed `Source::string` where a
  codec's literal function must return a `String`; `Source.string` returns an `Expr` — a Java expression
  rather than merely text — so a freshly generated project failed with *incompatible types: bad return type
  in method reference*. The template calls `Source.string(text).source()` now. Everything this README claims
  is true again: the skeleton builds and passes seven tests unedited.

  It went unnoticed because nothing in the platform's own build ever generates a plugin — the only plugin
  that exists is compiled from the same reactor as the toolkit it uses, so it cannot be out of step with it.
  `botmaker-cli`'s `ArchetypeSkeletonTest` compiles this skeleton, loads it through `PluginLoader` and runs
  every `PluginValidator` check over it, so the next such drift fails a build instead of a stranger's first
  five minutes.

## [0.0.5] — 2026-09-19

No source changes since v0.0.4; re-released for updated upstream pins.

No source changes since v0.0.3; re-released for updated upstream pins.

### Fixed

- **The generated skeleton compiles again.** `ExamplePlugin`'s value codec passed `Source::string` where a
  codec's literal function must return a `String`; `Source.string` returns an `Expr` — a Java expression
  rather than merely text — so a freshly generated project failed with *incompatible types: bad return type
  in method reference*. The template calls `Source.string(text).source()` now. Everything this README claims
  is true again: the skeleton builds and passes seven tests unedited.

  It went unnoticed because nothing in the platform's own build ever generates a plugin — the only plugin
  that exists is compiled from the same reactor as the toolkit it uses, so it cannot be out of step with it.
  `botmaker-cli`'s `ArchetypeSkeletonTest` compiles this skeleton, loads it through `PluginLoader` and runs
  every `PluginValidator` check over it, so the next such drift fails a build instead of a stranger's first
  five minutes.

## [0.0.4] — 2026-09-18

No source changes since v0.0.3; re-released for updated upstream pins.

### Fixed

- **The generated skeleton compiles again.** `ExamplePlugin`'s value codec passed `Source::string` where a
  codec's literal function must return a `String`; `Source.string` returns an `Expr` — a Java expression
  rather than merely text — so a freshly generated project failed with *incompatible types: bad return type
  in method reference*. The template calls `Source.string(text).source()` now. Everything this README claims
  is true again: the skeleton builds and passes seven tests unedited.

  It went unnoticed because nothing in the platform's own build ever generates a plugin — the only plugin
  that exists is compiled from the same reactor as the toolkit it uses, so it cannot be out of step with it.
  `botmaker-cli`'s `ArchetypeSkeletonTest` compiles this skeleton, loads it through `PluginLoader` and runs
  every `PluginValidator` check over it, so the next such drift fails a build instead of a stranger's first
  five minutes.

## [0.0.3] — 2026-09-16

### Fixed

- **The generated skeleton compiles again.** `ExamplePlugin`'s value codec passed `Source::string` where a
  codec's literal function must return a `String`; `Source.string` returns an `Expr` — a Java expression
  rather than merely text — so a freshly generated project failed with *incompatible types: bad return type
  in method reference*. The template calls `Source.string(text).source()` now. Everything this README claims
  is true again: the skeleton builds and passes seven tests unedited.

  It went unnoticed because nothing in the platform's own build ever generates a plugin — the only plugin
  that exists is compiled from the same reactor as the toolkit it uses, so it cannot be out of step with it.
  `botmaker-cli`'s `ArchetypeSkeletonTest` compiles this skeleton, loads it through `PluginLoader` and runs
  every `PluginValidator` check over it, so the next such drift fails a build instead of a stranger's first
  five minutes.

## [0.0.2] — 2026-09-02

### Changed

- **The generated skeleton targets Java 25 and JavaFX 25.0.4.** Its pom says `maven.compiler.release` rather
  than `source`/`target`, so a plugin author on a newer JDK compiles against 25's platform API rather than
  their own JDK's. A generated project needs a JDK 25 or newer.

## [0.0.1] — 2026-09-02

First release. `0.x` to match the platform the skeleton is generated against; the archetype itself pins
nothing and is forced by no other module's release, because what it ships is text.

### Added

- **The module** — the tenth BotMaker repository, and the first one aimed at somebody who does not work on
  BotMaker. Until now the only plugin was the SDK, written by hand inside the umbrella, which is exactly why
  the pom requirements were undocumented and untested.
- **A skeleton that builds and passes its tests with no edits** — a `pom.xml` with the three scopes that are
  easy to get wrong, the `META-INF/services` declaration, `ExampleApi` (one offered method and one
  `@Hidden`), `ExamplePlugin` (a palette, one registered `ValueType` with a codec, one slot editor chosen by
  the *call*) and `ExamplePluginTest` (seven tests, including the contexts the editor must **decline** —
  the half a plugin author never tests by hand).
- **The skeleton's codec writes its literal with `Source.string`** (2026-08-28), where it used to carry a
  private `quote(…)` escaping the backslash and the quote and nothing else. The example a beginner copies is
  the one they will copy into a real plugin, and a hand-rolled escaper is exactly the mistake the toolkit
  now owns: a pasted tab in a value becomes a compile error in somebody's bot.
- **Four required properties, each with a working default** — `pluginId`, `pluginName`, `studioApiVersion`,
  `toolkitVersion`. An archetype that stops to ask a question a beginner cannot answer is worse than one
  that guesses: a guess is visible in the generated files and can be edited.
- **`main-SNAPSHOT` as the default BotMaker version.** A real JitPack coordinate that never goes stale,
  rather than a released tag baked in here — which would owe an edit on every contract release and age
  silently between them. The generated README says to pin a tag before publishing.

### Deliberately absent

- **A `.gitignore` in the skeleton.** `archetype:jar` drops `**/.gitignore` through the archiver's default
  excludes, with no warning; every workaround ends with the generated project holding a file named something
  else. The generated README asks for one instead.
- **A second copy of the templates in the CLI.** `botmaker new` will shell to this archetype. The pom scopes
  are the thing that must not drift between two copies.
- **An integration test that builds what it generates.** That would resolve `main-SNAPSHOT` from the network
  inside `mvn install` at the umbrella root. The manual command is in `README.md` and runs against the local
  reactor.
