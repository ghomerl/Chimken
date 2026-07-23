# chimken

## Introduction

A Chicken invaders rip-off game, written by its only developer(me).

### Download Link
[1.0.0 version](https://drive.google.com/file/d/1zEy-b4vLyJeCO1Kynbr4KQD_ENnBTvMq/view?usp=drive_link)

## Devs


| 40412034 | طاها معدن کن |
| -------- | ------------ |

## How To Play

First of all, you need to be logged in to play this game. If you don't have an account, go register and then log in to play.
For starting the game, click on "Save The World", and then "New Game".
Congratulations! You have started your first game!

## Controls

| Up           | W     |
| ------------ | ----- |
| Left         | A     |
| Down         | S     |
| Right        | D     |
| Shoot/Attack | Space |
| Missile*     | M     |
| Pause        | Esc   |


This game will have 4 types of items dropping off of enemies:
1. Food, which will grant you food points*.
2. Gifts : Changes your weapon to the weapon of that gift. If you have that gift's weapon equipped, then It'll level up instead.
3. Power Ups : Levels up your weapon.
4. Keys : Currency of the game. Currently useless.

Missile*: Spawns a missile upon requesting one, the missile travels to the middle of the screen and explodes; resulting in dealing 2500 points of damage to every single enemy on-screen.
Food points*: For every 50 food points, You'll be granted a missile.
Extra lives: For every 10k points, you'll be granted an extra life.


## Libgdx Stuff 

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
