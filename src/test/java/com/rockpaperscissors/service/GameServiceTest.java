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

/**
 * This class will test the different methods of the {@link GameService} in order to make sure
 * it reacts accordingly.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class GameServiceTest
{
    /**
     * The instance of the game service to test against.
     */
    private final GameService gamesService;

    public GameServiceTest(@Autowired GameService gamesService)
    {
        this.gamesService = gamesService;
    }

    //<editor-fold desc="Test methods">

    /**
     * This method will test out the creation of a new game through the service. The process is
     * to create a new game and then retrieving it and asserting it is not null, then the deletion is
     * perform and then a check is made in order to guarantee the service throws the appropriate exception
     *
     * @throws GameNotFoundException Thrown when the inserted game is not inside the system.
     */
    @Test
    void testNewGameAndDelete() throws GameNotFoundException
    {
        // Creating the game and retrieving it
        Game retrievedGame = this.createAGameAndAssertNotNull();

        // Asserting the game is not null
        Assertions.assertNotNull(retrievedGame, "The returned game by the service can not be null");


        // Deleting the game and checking it does not exist
        this.gamesService.deleteGame(retrievedGame.getGameID());
        Assertions.assertThrows(GameNotFoundException.class, () -> this.gamesService.getGameByIdentifier(retrievedGame.getGameID()));

    }

    /**
     * This method will test out different combinations for retrieving a game making sure the outcome is the expected one.
     * Also, it will test out if when modifiying the game through the service, the returned game after said modification
     * reflects the change.
     *
     * @throws GameNotFoundException Thrown when something unexpected happens.
     */
    @Test
    void testGetGameAndNewRound() throws GameNotFoundException
    {
        // Checking the proper exception is thrown when a null identifier is supplied
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesService.getGameByIdentifier(null),
                "When trying to look for a game with a NULL ID, an IllegalArgumentException should be thrown");
        Assertions.assertThrows(GameNotFoundException.class, () -> this.gamesService.getGameByIdentifier("test2"),
                "When retrieving a game that does not exist, a GameNotFoundException should be thrown");

        // Creating and retrieving the game
        Game retrievedGame = this.createAGameAndAssertNotNull();

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
        this.gamesService.deleteGame(gameFromService.getGameID());
    }

    /**
     * Same as {@link #testGetGameAndNewRound()} but for {@link GameService#restartGame(String)}
     *
     * @throws GameNotFoundException Thrown when something unexpected happens.
     */
    @Test
    void testRestartGame() throws GameNotFoundException
    {
        // Creating a game, generating a new round, restarting and asserting the number of rounds are 0
        String gameID = this.gamesService.startNewDefaultGame();
        this.gamesService.generateNewRound(gameID);
        this.gamesService.restartGame(gameID);
        Game retrievedGame = this.gamesService.getGameByIdentifier(gameID);
        Assertions.assertEquals(0, retrievedGame.getTotalAmountOfRounds(), "After reset, the rounds should be 0");
    }
    //</editor-fold>

    //<editor-fold desc="Helper methods">

    /**
     * This method is used to create a game and retrieve it from the service asserting it is not null.
     *
     * @return The created game
     * @throws GameNotFoundException thrown when something unexpected happens.
     */
    private Game createAGameAndAssertNotNull() throws GameNotFoundException
    {
        // Creating and retrieving the game
        String gameID = this.gamesService.startNewDefaultGame();
        Game retrievedGame = this.gamesService.getGameByIdentifier(gameID);
        Assertions.assertNotNull(retrievedGame, "the retrieved game should not be null");
        return retrievedGame;
    }
    //</editor-fold>

}