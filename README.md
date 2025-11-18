# ChaCuN

Board game developed in Java with JavaFX inspired from the board game "Carcassonne". This project is part of the EPFL course CS-108.

## Prerequisites

- Java 21
- Maven

## Build

```bash
mvn compile
```

## Run

```bash
mvn javafx:run
```

By default, launches with 2 players (Alice and Bob).

### Change players

Edit `pom.xml`, `<commandlineArgs>` section:

```xml
<commandlineArgs>Player1 Player2 Player3</commandlineArgs>
```

### With a seed

```xml
<commandlineArgs>Alice Bob --seed=12345</commandlineArgs>
```

## Tests

```bash
mvn test
```

## Package

```bash
mvn package
```
