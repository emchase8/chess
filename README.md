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

[Phase 2 link](https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZTAcygQArtgDEAFgDMADgBMAThAxbyMwAswHQQrQwAlFDMkVTAoOSQINExEVFIAWgA+ckoaKAAuGABtAAUAeTIAFQBdGAB6KwMoAB00AG8AInrKNGAAWxQ23LaYNoAaYdx1AHdoDgGh0eGUHuAkBDnhgF9MYRyYDNZ2Lkp89s6obr71hbaJ1WmoWcHhsballbWntq22Tm5YfZ2onyUAiUTAlAAFOFItFKOEAI4haIASm22VEe0ysnkShU6nyZhQYAAqg0IWcLihUdjFMo1KpMUYdLkAGJITgwUmUGkwHQATxgFN6Yh0IOAAGtOQ0YJMkGA-IKGpSYMAEGKOAKUAAPMEaGm4+l7AHolT5LlQGlokQqI2ZHbHGAKNUoYAa8oS9AAUS1KmwBASVt2qX2yXQYHy9gADI5mu0+upgISBsNPVBLHlFV1hSrna6BfJxegvph0BxMPq6epbVlrSh8mgrAgEIGMfsK3jVLkQGLwebyQ0adTtAaq-tjLkFBwOFLudoWza28PK53uy7wQorPKIcBN35B+Wlx3GePJ9ON-LLYCF-sfkcM9CwXC1I2sLe-tX7RnTkrhUN8vMXh3eVyggQs0D-YZNkDShq1DDB8lcSNI1jDof0uGB-2eYYgL8ECwIg+YtlLcxLBsWxoHYQlvBZOBPWkOAFBgAAZCBIkSODmGNagHWKMoqlqAx1HiNAUKFS5MOuW57keSDi0-Rk3wdb8s3E2SXikmZk0IzBFKgDIrzrGAEFY9kIRYtiESRMBUQMjJ23pAkiT7MSqQPHFlwyccYDZDlzR5flM3OYV5xQOzDwcmBCTAc8-G3Xd93s0dMi8nyz3i7ReQFFzMFFF1JXNGAokFbpd2gJAAC8UDLRKGWDO0TUMmLLwaurPzNEr5TKyqOG9X1-UST86o4iNIwARhQ+NVETfp-1TdN8isDq-C6qri2ImrWoa+tG2bAzq10-JzPZcJVBfHTDj+TbuK-VCVJmtTsN3PCiwkrZBoyYaYEQ5DWluoLVIAx7gNAl7ZKIzgSOsOwLBQdBvF8AIYbh8zrCwDjGTawppE9JjPXKT1qhqQTVGE5ocOegbskoDIDpVJ6QcSXT9K2ozWNRsy2c3Sy1GskKwvcjtHOi3c4ovbQhwF+lPOZbz2TSsX5EyungbAnKxXy6UisWnCVuq8LRy4qggUddL5D55nrvanWoAqqrepQP1hOgvSPuQMMRvG37JumrS5ugBalt1taIY2i2jdNGAGybc30lplGuefBBXwu6mw4dKOEBgCEkA4SUkAAMxgVQID6eV2TMGAemgQxVGWxtpzMCAYD8ShXPe9JPu+2NwbLTALCh2wQWnWxsHZSUmLBGAAHFhQ0dHDZ4qe8cJsxhTJ+nVcG2OU4zcmGfO35qfSAz8mQaIzLBbmUT59IaqFmLRb3cW3NpDtpfyVKTYV4Alb31XcolDOWAWtA4226i-EctVDbGyanOWyacMzElAbbHqPoHb9WdkNN28EYBRk9nGekPtZppn9sVa2KDg5llsrffWnZIpEhnn0R+FMJavylukccn9GEoB5PnaAOZM6r3jL-De6AIEeTDsbbhzVax1TjpfROydD4u2PlTG6Qi1DJgKO0DRABJaQyZRquEcPYF4kw-ByhQOaSkWlrhBFAOKaxv5-zXA0QAOWccML4MBKhvTUVglI4Yu6-Q0aoLROjhT6MMcY0xwxzGWKcQDOxCAHGJPum0VxwoPFJI2D4nukMyIcAAOzuEjCgSM3hPSODgLRAAbPAHshhuEwCSNgzi9VLaFFKBUFea8loUxQu4zx2kt60z-ugMYES+jZPSVBJmqjaxdkaRCOAjSr682oXfehwstw4QSrQ9+ssOSwMVgFcZiQAEaxgiA8h4CaofhZic4AMdMZINuXbNBjsAzt0+ngiahCkzEPmmQ0qYDVr5NDgs8OdYM43y2auOQKBuEQg0fsyW6h36nmnsKGR0K5E71yKstcKATpnSZgg3IUyUBRPyEYkxfjroBPdl9JCgzIkGNpTE-JfdSJ2D5CgJsEBJjwyQP4MA-LBXCoAFIQHZNivo3h7EgHFK0wJGM1H5GKMSfiNQNHrxVugFC2AUn8qgHACAxkoBaWGAAdRYLo-GNQABCTEFBwAANKZL6DSmAdL7AMpyDTAlytcIM0mTcE1lBzWWutW0O1DrPTOtdR6r11KOW+q5QfO8kiI4ACtZVoAhDK46KBEQ8xsi1Gh6LOxRQfns5+G0OEy0-k8kRBqLnqyAYVBk2tQUoPEUeOqNZoX5CeS8jVnJkHdXtl8ymjLXaBI9v8hMgKUwkIzL2zqYLZj5M2bQoWyLUUNoOU21kct5U8IygFPR0g1Z5S7TcvtdzaEPMWRe3Fogro5HyO6MCM6ME-LaSNGMXsAXpL9rvXMGoYAFiLBCg5ObDKwvgdvZR+Ri1oFJUnLNl0KWRx2lnHOedC7F1LhYtAFcq4giLnXBADcm4txBBW+dHcgMsp+i0bl-cyIWGAAKHworEaxAFIgF0sBgDYGNYQOICQWnzw6d+rGOM8YE1qMYTBqG7y5FhfMk+MAQDcDwBCZjeKq1sPxNsutptgDIjGFFQ9OLxZ2acmSM4aLzOqHftIAVRJDCqkzk8sY0jtBjEOEAy0kKT7IZakGtDom8BYaUdmvDGdCO50KiRkuRJyOUerjR6wdHIoMdbiZ1OrHF3se7iWCGQA)