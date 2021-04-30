# Tic Tac Toe Game
Proposed solution of Tic Tac Toe Game with Spring Boot &amp; Test Unit


## Description

This project is a proposed solution for TicTacToe Game challenge with Java, Spring boot, JPA, H2 and TTD with JUnit & Mockito

## Rules

- X always goes first.

- Players cannot play on a played position.

- Players alternate placing X’s and O’s on the board until either:

    - One player has three in a row, horizontally, vertically or diagonally

    - All nine squares are filled.

- If a player is able to draw three X’s or three O’s in a row, that player wins.

- If all nine squares are filled and neither player has three in a row, the game is a draw.

## Stack used

The technologies used is Example is :

- JDK 11
- Spring boot
- JPA
- Lombok
- H2 Memory Database
- JUnit
- Mockito
- Maven

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.digitalstork.tictactoe.TictactoeApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

shell
mvn spring-boot:run


## Test End Point with Postman
Our example contains two end points:

- Create Board: [localhost:8080/api/tictactoe/new](http://localhost:8080/api/tictactoe/new), With GET Method
- Play round: [localhost:8080/api/tictactoe/play](http://localhost:8080/api/tictactoe/play), With POST Method

You execute the first end point ([localhost:8080/api/tictactoe/new](http://localhost:8080/api/tictactoe/new)) which will generate an id which allows you to play the game in the second end point ([localhost:8080/api/tictactoe/play](http://localhost:8080/api/tictactoe/play)) we give the name of the player (X or O), X is play the first time, and the values (between 0 and 2) of each row and column of our matrix (3X3)

Note: for the second end point which had a POST method, we must introduce the data in Body-> raw-> JSON:

json
{
  "id": "540fc375-30bd-4bc6-b89e-26410968d209",
  "player": "X",
  "row": 0,
  "col": 0
}

## Display of the result:
the result will be displayed in the console (CMD, Temrinal ...) or on your IDE console

## Run Tests

You have several methods to run the test,
From IDE :
bash
- Right click on the project: "Run All Tests"
- Right click on the class (test): "Run"
- Right click on the method (test): "Run"


or
You can use the mvn command :
shell
mvn test