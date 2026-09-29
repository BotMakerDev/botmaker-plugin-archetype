# botmaker-plugin-archetype

`mvn archetype:generate` for a **BotMaker Studio plugin**.

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.github.LiQiyeDev \
  -DarchetypeArtifactId=botmaker-plugin-archetype \
  -DarchetypeVersion=v0.1.0 \
  -DarchetypeRepository=https://jitpack.io
```

`-DarchetypeRepository` is required: BotMaker publishes through JitPack, and Maven does not read a project's
`<repositories>` when resolving an archetype — there is no project yet.

It asks four questions beyond the usual coordinates, each with a working default:

| property | default | what it is |
|---|---|---|
| `pluginId` | the groupId | what `StudioPlugin.id()` returns, and what the registry refuses twice. **Not** the Maven coordinate — a plugin may be re-published under a new one and must keep its id. |
| `pluginName` | the artifactId | what Studio shows in *Plugins & Libraries*. |
| `studioApiVersion` | the newest contract tag when this archetype was released | the contract version written into the generated pom. |
| `toolkitVersion` | the newest toolkit tag when this archetype was released | the toolkit version written into the generated pom. |

## What comes out

A project that **builds and passes its tests with no edits**:

- `pom.xml` with the three scopes that are easy to get wrong — `botmaker-studio-api` `provided`,
  `botmaker-plugin-toolkit` `compile`, `javafx-controls` `provided`.
- `META-INF/services/com.botmaker.plugin.api.StudioPlugin`, the only reason Studio ever finds the plugin.
- The standard package tree (`../docs/refactor/34-plugin-package-tree.md` in the umbrella): `api/` for what
  a bot compiles against, `internal/` for what it runs but never names, `plugin/` for the Studio half.
- `api/ExampleApi` — a facade with one offered method and one `@Hidden` one; `api/Greeting` — a value a bot
  holds.
- `plugin/ExamplePlugin` — one declaration, `StudioPlugin.id(ID).named(NAME).types(…).editors(…)`, on the
  contract's `DeclaredPlugin`.
- `plugin/types/ExampleTypes` — the greeting declared once, `PluginType.value(Greeting.class)` and its steps
  (fresh value, editor, `writtenAsRecord()`).
- `plugin/editors/ExampleEditors`, `plugin/editors/GreetingEditor` — one slot editor chosen by the **call**
  rather than by the type, and the greeting's editor.
- `plugin/ExamplePluginTest` — seven tests including the ones a plugin author normally never writes: the
  contexts the editor must *decline*.

## Why the versions default to tags

A released Studio loads a plugin only when its contract has every member the plugin uses. The tip of the
contract's `main` is usually ahead of every released Studio, so the old `main-SNAPSHOT` default could
generate a plugin no Studio would load (*built for a newer Studio*). The release writes the newest pair into
the descriptor each time the archetype is cut (`botmaker-cli`'s `ArchetypePin`), so nobody edits them by hand.

## Opening it in IntelliJ

The skeleton's `.java` files are resources with `${package}` placeholders, so IntelliJ does not show this
module as Java; that is expected. Run `mvn install` here, then open
`target/test-classes/projects/basic/project/example-plugin` as a Maven project: it is the generated skeleton,
and it builds. `CLAUDE.md` has the round trip.

## Building

```bash
mvn install     # com.github.LiQiyeDev:botmaker-plugin-archetype:0.0.0-SNAPSHOT
```

To generate against the local reactor rather than JitPack — the fastest way to check a change here:

```bash
mvn install                                # at the umbrella root, so the contract and toolkit are in ~/.m2
cd /tmp && mvn archetype:generate -B \
  -DarchetypeGroupId=com.github.LiQiyeDev \
  -DarchetypeArtifactId=botmaker-plugin-archetype -DarchetypeVersion=0.0.0-SNAPSHOT \
  -DgroupId=com.example -DartifactId=my-plugin \
  -DstudioApiVersion=0.0.0-SNAPSHOT -DtoolkitVersion=0.0.0-SNAPSHOT
cd my-plugin && mvn verify
```

Releases are cut from the umbrella with `../release.sh --plugin-archetype <version>`.
