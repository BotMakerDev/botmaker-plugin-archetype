# botmaker-plugin-archetype

The example **BotMaker Studio plugin**, and the project a new plugin starts as:

```bash
botmaker plugin new discord-notifier --group com.example --plugin-name "Discord Notifier"
```

[`botmaker plugin new`](https://github.com/BotMakerDev/botmaker-cli) copies this plugin as it was when that
`botmaker` was released, and renames it: the package and its directories, the plugin's name, the pom's
coordinates (the plugin's identity) and the class its `botmaker.plugin` property names. Every option has a default derived from the artifact id. Without the cli, copy this repository at a
tag and rename those by hand.

It was a `maven-archetype` (`mvn archetype:generate`) until 2026-10-19. It is a plain plugin now, so CI builds
and tests exactly the project a newcomer gets.

## What is in it

A plugin that **builds and passes its tests as it stands**, package `com.example.greetings`, named `Greeter`:

- `pom.xml` with the scopes that are easy to get wrong: `botmaker-studio-api` `compile` (level 3 marks the
  bot's code), `botmaker-plugin-toolkit` `compile`, `javafx-controls` `provided`.
- the pom's `botmaker.plugin` property, the only way Studio finds the plugin; its `groupId:artifactId` is the
  plugin's identity and its `<name>` what Studio shows.
- One of each thing a plugin does, labelled in the code as levels:
  1. a value type: `api/Greeting`, `plugin/types/ExampleTypes`, `plugin/editors/GreetingEditor`;
  2. a palette block: `api/ExampleApi`, with `api/Greetee` and `plugin/editors/ExampleEditors` for the one
     argument that needs an editor of its own;
  3. a managed value: `api/GreetValue` and `plugin/values/ExampleValues`, a `@Param` Studio writes into the bot.
- `plugin/ExamplePluginTest`: nine tests, including the contexts the editor must *decline*.

`src/template/README.md` is the README a new plugin gets; it walks through the same files.
`src/template/DECLARE.md`, "Declare a plugin in 10 minutes", is the one page to read first: it quotes the
example's code, and the build checks that the quotes still match.

## The versions it pins

The pom pins `botmaker.studioapi.version` and `botmaker.plugintoolkit.version` like any module here: the
upstream's `-SNAPSHOT` on `main`, its released tag on a tag. A plugin made by a released `botmaker` pins the
contract and toolkit released with it, which a released Studio loads.

## Building

```bash
mvn install     # the plugin's jar and the `template` zip botmaker-cli bundles
```

It needs the contract and toolkit at their `main` `-SNAPSHOT` in `~/.m2`: run `mvn install` at the umbrella root
first, or open this module in the umbrella in IntelliJ, where it is an ordinary Maven module.

Releases are cut from the umbrella only, with `--plugin-archetype <version>`.
