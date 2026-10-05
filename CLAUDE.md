# CLAUDE.md

Guidance for working in **botmaker-plugin-archetype**, the `mvn archetype:generate` starting point for a
BotMaker Studio plugin.

Read the umbrella `../CLAUDE.md` first, `../botmaker-studio-api/CLAUDE.md` for the contract the skeleton
implements, `../botmaker-plugin-toolkit/CLAUDE.md` for the widgets it uses, and
`../docs/refactor/24-plugin-platform.md` for why any of it exists.

## What this module is

**Text, not code.** Nothing here is on anybody's classpath. The `archetype-resources/` tree is copied through
Velocity into somebody else's empty directory, and the versions it writes into the generated pom are
`requiredProperties` substituted at generation time — not Maven dependencies resolved at build time. That is
why this is the one plugin-facing module with **no `flatten-maven-plugin` and no `.deps.env`**: neither has
anything to bake.

**Its job is to be the single source of truth for the generated shape.** The plan's `botmaker new` (phase 7)
shells to this archetype rather than carrying a second copy of the templates — the pom scopes below are
exactly the thing that must not exist in two places and drift.

## The one property that matters: it builds with no edits

The acceptance test is not "the descriptor parses". It is:

```bash
mvn install                                   # umbrella root, so the contract and toolkit land in ~/.m2
cd /tmp && mvn archetype:generate -B \
  -DarchetypeGroupId=com.github.BotMakerDev \
  -DarchetypeArtifactId=botmaker-plugin-archetype -DarchetypeVersion=0.0.0-SNAPSHOT \
  -DgroupId=com.example -DartifactId=my-plugin \
  -DstudioApiVersion=0.0.0-SNAPSHOT -DtoolkitVersion=0.0.0-SNAPSHOT
cd my-plugin && mvn verify                    # must be green, 7 tests, no edits
```

Run it after **any** change to `archetype-resources/`. Nothing in this module's own build compiles a line of
the skeleton, so a skeleton that does not compile is a green build here.

## The three scopes the skeleton exists to get right

They are the whole of what a plugin author cannot guess, and each is wrong in a way that produces a confusing
failure rather than a compile error:

| dependency | scope | what a wrong scope does |
|---|---|---|
| `botmaker-studio-api` | `provided` | Harmless at `compile` since `PluginLoader` is parent-first for the contract (the SDK declares it so, because `@Param`/`@Managed` sit on a bot's own code), and `pom-scopes` accepts both. `provided` stays the skeleton's answer: a plugin that puts nothing on a bot's classpath should not bring the contract to every bot that uses it. |
| `botmaker-plugin-toolkit` | `compile` | `provided` compiles fine and then `NoClassDefFoundError`s at load, because Studio does not have the toolkit. |
| `javafx-controls` | `provided` | `compile` puts a second JavaFX in a process that has one. |

## Velocity, and the two things it eats

`archetype-resources/` files marked `filtered="true"` go through Velocity.

- **`##` is a line comment.** A filtered markdown file loses every level-2 heading and the rest of its line,
  silently. That is why `README.md` in the skeleton is **not** filtered and names nothing project-specific.
- **`${...}` is a reference.** So the generated `pom.xml` uses literal versions rather than
  `<properties>` + `${javafx.version}`: a Maven property reference in a filtered file is a Velocity
  reference first, and relying on Velocity leaving an unresolvable one alone is relying on undefined
  behaviour.

## Working on the skeleton in an IDE

IntelliJ does not treat this module as Java, and it is right not to. The packaging is `maven-archetype`, and
the skeleton's `.java` files are **resources** under `src/main/resources/archetype-resources/`, with
`${package}` where a package name goes: they are not sources of this module and do not compile as written.
Do not mark them as a source root. To edit the skeleton with completion and navigation, work on a generated
copy:

```bash
mvn -pl botmaker-plugin-archetype install        # umbrella root; the integration test generates the copy
# then open botmaker-plugin-archetype/target/test-classes/projects/basic/project/example-plugin
# in IntelliJ as a Maven project; mvn verify there must be green, 7 tests
```

Port each change back into `archetype-resources/` by hand, putting `${package}` back where the generated
copy has `com.example.plugin`. `mvn -pl botmaker-cli test -Dtest=ArchetypeSkeletonTest` then compiles,
loads and validates the result against the reactor's contract and toolkit.

## No `.gitignore` in the skeleton, and it is not an oversight

`archetype:jar` packages `archetype-resources/` through a Plexus archiver with default excludes on, and that
list holds `**/.gitignore`. The file reaches `target/classes` and is dropped from the jar with **no warning**.
Every workaround ends with the generated project holding a file named something other than `.gitignore`,
which is worse than none — so the generated `README.md` asks for one instead. Do not "fix" this by renaming
the resource.

## The integration test generates and stops

`src/test/resources/projects/basic` runs during `mvn install` because its `goal.txt` exists — the plugin
skips any IT directory without one, and until 2026-09-29 this one had none, so the test never ran. The file
is empty on purpose: no goal means generate only. It deliberately does not build what it
generates: that would resolve the contract and toolkit from the network inside `mvn install` at the umbrella
root, which the standing constraint forbids. What it does hold is still real —
the descriptor is well-formed, every filtered file's references resolve, and packaged directories land under
the requested package.

## Style

The skeleton's own comments are the deliverable. A plugin author reads them before any documentation, so
every one says which *mistake* it prevents — that renaming `ExamplePlugin` needs the services file edited,
that a type's editor is delegated so a headless host can load its declaration, that a call-site
predicate must decline a Parameters row. That is the same rule the contract and the toolkit keep.

Published through JitPack. Releases are cut from the umbrella with
`../release.sh --plugin-archetype <version>`.
