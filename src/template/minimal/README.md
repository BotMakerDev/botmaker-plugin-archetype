# A BotMaker Studio plugin

Made by `botmaker plugin new --minimal`: a plugin class that declares nothing yet, its test, and the pom. It
compiles and its test passes as it stands. Read `DECLARE.md` to add each surface: a value type, a palette block,
a managed value, a toolbar item. `botmaker plugin new` without `--minimal` makes an example that has the first
three.

# Build it

```bash
mvn verify
```

# Try it in Studio

1. Run `mvn install`. It puts your plugin in `~/.m2` at its `-SNAPSHOT` version.
2. `botmaker plugin run --project <bot>` installs it, pins it in the bot and opens Studio. Or pin the coordinate in
   *Project ▸ Plugins & Libraries*.
3. Tick **Dev mode** in that window: Studio loads a `-SNAPSHOT` plugin only in a dev-mode project.
4. After each rebuild (`mvn install`), press **Reload** in the same window.

# What else is in here

- `CHANGELOG.md` — what each release changes. The build copies it into the jar, and Studio's upgrade dialog shows
  the sections a bot moves through.
- `icon.png` — the icon Studio shows beside the plugin's name. Replace it with your own, square, 64 px.
- `jitpack.yml` and `.jitpack/` — how JitPack builds your tags; `botmaker plugin publish` rewrites them.
- `pom.xml` — its `<parent>`, `botmaker-plugin-parent`, pins the contract, the toolkit and JavaFX together;
  `botmaker plugin upgrade` moves it to its newest release. Its `groupId:artifactId` is the plugin's identity, its
  `<name>` what Studio shows, and its `botmaker.plugin` property the plugin class.
- A plugin writes to Studio's log through `services.log()`, on the `StudioServices` its steps are handed. Each
  line shows under the plugin in *Plugins & Libraries ▸ Installed ▸ Log*.
