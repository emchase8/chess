# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## Phase 2 Diagram Link

[Phase 2 link](https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZTAcygQArtgDEAFgDMADgBMAThAxbyMwAswHQQrQwAlFDMkVTAoOSQINExEVFIAWgA+ckoaKAAuGABtAAUAeTIAFQBdGAB6KwMoAB00AG8AInrKNGAAWxQ23LaYNoAaYdx1AHdoDgGh0eGUHuAkBDnhgF9MYRyYDNZ2Lkp89s6obr71hbaJ1WmoWcHhsballbWntq22Tm5YfZ2onyUAiUTAlAAFOFItFKOEAI4haIASm22VEe0ysnkShU6nyZhQYAAqg0IWcLihUdjFMo1KpMUYdLkAGJITgwUmUGkwHQATxgFN6Yh0IOAAGtOQ0YJMkGA-IKGpSYMAEGKOAKUAAPMEaGm4+l7AHolT5LlQGlokQqI2ZHbHGAKNUoYAa8oS9AAUS1KmwBASVt2qX2yXQYHy9gADI5mu0+upgISBsNPVBLHlFV1hSrna6BfJxegvph0BxMPq6epbVlrSh8mgrAgEIGMfsK3jVLkQGLwebyQ0adTtAaq-tjLkFBwOFLudoWza28PK53uy7wQorPKIcBN35B+Wlx3GePJ9ON-LLYCF-sfkcM9CwXC1I2sLe-tX7RnTkrhUN8vMXh3eVyggQs0D-YZNkDShq1DDB8lcSNI1jDof0uGB-2eYYgL8ECwIg+YtlLcxLBsWxoHYQlvBZOBPWkOAFBgAAZCBIkSODmGNagHWKMoqlqAx1HiNAUKFS5MOuW57keSDi0-Rk3wdb8s3E2SXikmZk0IzBFKgDIrzrGAEFY9kIRYtiESRMBUQMjJ23pAkiT7MSqQPHFlwyccYDZDlzR5flM3OYU3NpDtg3SAyzQHOdbPSez8RgQkwHPPxt13QcYGgGBVzkFAUrSi9tCHdyjzHZlvPZM90u0XkBRczLYCQAAzGA0ASQxtV1MYcvBTK0DEeKGXCmsqCBR1qvkedDGG3T8nM9lwlUF8dMOP5ws-E5UJU-oJMA3c8KLCStk-cKOIQpDRLQna1Ow-bQMO2SiM4EjrDsCwUHQbxfACd7PvM6wsA4xkNsKaRPSYz1yk9aoakE1RhOaHCDsSE70lmlU7rAlbfkofSTUM4yzABszWIByy1Gsqa7MPBzEqJfKcP3QbPPKnyqsK+Raox4D7sSUUXUlc0YCiQVul3aAkAALxQMtBo-fH60bZtbLx7iM2JMX5Ql6WOG9X1-RR7JcYyM6YCjABGFD41URNrraVN03yKxNb8bWZeLYjmdV0bTXGjngCpuKaYSjgUG4dddwKvcipCkdVBZ3JpDDolDBS-yBSR3nY4872gQbJsqYydH-s3Rblt09ajYzfOEBgCEkA4SVmpgVQID6eV2TMGAemgQxVFdxtpyJmA-EoVyTpN5Aw3O5DWiestMAsV7bBBadbGwdlJSYsEYAAcWFDQga4nJ8gKXfIZhsxhURzH0GgvS0dWh1M6xiuIoVoywTMsFyZRQPBscslSOjMY7M3SF5Nmfto6cwCi-O+-MJQzkagyZ2OE3ay2DkNY+Y0a6F3fmrM0Lt0F6xQH6YS99TpT3gmbSMltWhtGtrbLSDtoBOyIVAKW7t57ZzCrnU0acYr42piVWmSV97xghMVUK9IE6QPESgHkTUsqqlrlfeMPCZF8MMtvaIAAeeRNJ0h4OLj-Z8CBXxPz0t7JSbQ1FqGTAUdodiACS0hkzm1cI4ewLxJh+DlCgc0lItLXCCKAcUgTfz-muHYgAcpE4YXwYCVGOlXShKRwyIVnk4g+Djsl9Fce4zx3jhi+P8RE1SbQQkIDCeUu20ThRxIqYk5JJZnqL1InYDgAB2dwkYUCRm8J6RwcBaIADZ4A9kMPImASQqGcTtFXU+pQKiX2vi7ZGKFYnxO0qjdGcC0BjDySgRpdsoJv0ij1FAEI4CTN-pTWKAC6ZAK3CA+QUi44JwqhyARMCM63z5mKQW0oRaoPFhwnWGjRzYN9rglW+CT6cnYZw3WPpSEGwoZPdJEZaFW3pEw-8LCMygq1uCrhrSMEiPUJXWsE4JoBweZg-Ilz5EQjsUzTBCdTx72FJaL28KgQuOkMYyxuQblrhQGXcx2M7zUpPkcgp+QPFeJSWrNJ08YCZM2cKBVMAlX2G4e05efIw7GUmF9JA-gwDGqbBAM1AApCA7JuV9G8KEkA4pZnpOBoswoRRiT8RqHYm+PMwIoWwNU41UA4AQGMlALSwwADqLBnFQxqAAISYgoOAABpep+S3GKqKSqnIRcRXc1wrzQ5NwI2UGjbG+NbQk0ps9OmzNOa80oB1Xq74litH5AAFaOrQBCB1C0UCIgpjZIRQdKWdiSgzOl7yPLgNZpVKB6dy3I0wAgoFMEQVIohXLYakVWpKzwSDDWaDSUov1uQie6RTYW1xQmJMBK0ysNFle5FHtnp8pPT8+l07HlJT7K84AS6worvyJA+qsD-mQvjn2s4l4gOMqeSytloCOVQbkTymqAVBUIfljS51CjBG1mEdIhKMs5QYbw-IMYyGsOzs+a6acZwYBNUsD0QKPJIAt2wCgIgTVUBD2zPx4A2BsCWGk0gXK2UY22oQ7KnBZ64Xo1HWgSVFicZWPhQ6GudcG5Nxaq3dufi0Bdx7iCFuA8EBDwgCPMeU7VWYvVZque5KXpkQsMAAUPgLU-ViAKRALpYCSfDYQOICQZlHwWQQ0G4NIbQ1qMYChj9dO5Fwecj+IBuB4Ekf-NDodw55WAYusYJWU50b6IOSrydexkiY285TUGk6lZzLXADYwDHaDGIcJBvKOV9thUI0tmXQt4G09Kta1jq5KyM43YWpm25Egs1Z3utnrD2cSo50eIIXMlrc9QjzLRuFAA)