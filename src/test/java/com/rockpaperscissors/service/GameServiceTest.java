package com.rockpaperscissors.service;

import com.rockpaperscissors.exceptions.game.GameNotFoundException;
import com.rockpaperscissors.model.game.Game;
import com.rockpaperscissors.model.game.round.Round;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class GameServiceTest
{
    @Autowired
    private GameService gamesService;

    //<editor-fold desc="Test methods">
    @Test
    void testNewGame() throws GameNotFoundException
    {
        // Creating the game and retrieving it
        Game retrievedGame =  this.createAGameAndAssertNotNull();

        // Asserting the game ist no null
        Assertions.assertNotNull(retrievedGame, "The returned game by the service can not be null");


        // Deleting the game and checking it does not exist
        this.gamesService.deleteGame(retrievedGame);
        Assertions.assertThrows(GameNotFoundException.class, () -> this.gamesService.getGameByIdentifier(retrievedGame.getGameID()));

    }

    @Test
    void testGetGame() throws GameNotFoundException
    {
        // Checking the proper exception is thrown when a null identifier is supplied
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesService.getGameByIdentifier(null),
                "When trying to look for a game with a NULL ID, an IllegalArgumentException should be thrown");
        Assertions.assertThrows(GameNotFoundException.class, () -> this.gamesService.getGameByIdentifier("test2"),
                "When retrieving a game that does not exist, a GameNotFoundException should be thrown");

        // Creating and retrieving the game
        Game retrievedGame =  this.createAGameAndAssertNotNull();

        // Generating a new round and asserting is not null
        Round round = this.gamesService.generateNewRound(retrievedGame.getGameID());
        Assertions.assertNotNull(round, "The generated round should not be null");

        // Retrieving the game again and asserting the object is still the same
        Game gameFromService = this.gamesService.getGameByIdentifier(retrievedGame.getGameID());
        Assertions.assertEquals(retrievedGame.getTotalAmountOfRounds(), gameFromService.getTotalAmountOfRounds(),
                """
                        Both amount of rounds should be equal since both game objects
                        represents the same one.
                        """);

        // Asserting the number of rounds
        Assertions.assertEquals(1, gameFromService.getTotalAmountOfRounds(), "The number of rounds should be 1");
        this.gamesService.deleteGame(gameFromService);
    }

    @Test
    void testRestartGame() throws GameNotFoundException
    {
        // Creating a game, generating a new round, restarting and asserting the number of rounds are 0
        String gameID = this.gamesService.StartNewDefaultGame();
        this.gamesService.generateNewRound(gameID);
        this.gamesService.restartGame(gameID);
        Game retrievedGame = this.gamesService.getGameByIdentifier(gameID);
        Assertions.assertEquals(0, retrievedGame.getTotalAmountOfRounds(), "After reset, the rounds should be 0");
    }
    //</editor-fold>

    //<editor-fold desc="Helper methods">
    private Game createAGameAndAssertNotNull() throws GameNotFoundException
    {
        // Creating and retrieving the game
        String gameID = this.gamesService.StartNewDefaultGame();
        Game retrievedGame = this.gamesService.getGameByIdentifier(gameID);
        Assertions.assertNotNull(retrievedGame, "the retrieved game should not be null");
        return retrievedGame;
    }
    //</editor-fold>

}