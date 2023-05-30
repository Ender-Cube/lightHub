# EndercubeTemplate

A template [Minestom](https://minestom.net) server to be used as a base for all Endercube minigames

## Features

 * Custom Logback configuration for syntax highlighting in console
 * [LiteCommands](https://github.com/Rollczi/LiteCommands)
 * Player argument for LiteCommands
 * Block break protection
 * [Sign](https://github.com/Ender-Cube/EndercubeTemplate/blob/main/src/main/java/me/zax71/EndercubeTemplate/blocks/Sign.java) and [Skull](https://github.com/Ender-Cube/EndercubeTemplate/blob/main/src/main/java/me/zax71/EndercubeTemplate/blocks/Skull.java) block handlers
 * GitHub Actions CI configuration

## Usage

Press the "Use this template" button in GitHub and rename the `me.zax71.EndercubeTemplate` package to whatever you want then let IntelliJ do the refactoring for you. You will have to manually change the [build.gradle.kts](https://github.com/Ender-Cube/EndercubeTemplate/blob/main/build.gradle.kts#L50https://github.com/Ender-Cube/EndercubeTemplate/blob/main/build.gradle.kts#L50) [Settings.gradle.kts](https://github.com/Ender-Cube/EndercubeTemplate/blob/main/settings.gradle.kts#L1) and [logback.xml](https://github.com/Ender-Cube/EndercubeTemplate/blob/main/src/main/resources/logback.xml#L3) files manually

Before this project will work you need to load a world, see how [StomKor](https://github.com/Ender-Cube/StomKor/blob/main/src/main/java/me/zax71/stomKor/Main.java#L110) does this for inspiration.

