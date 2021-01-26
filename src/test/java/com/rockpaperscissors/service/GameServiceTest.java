package com.rockpaperscissors.service;

import com.rockpaperscissors.exceptions.game.GameNotFoundException;
import com.rockpaperscissors.model.game.Game;
import com.rockpaperscissors.model.game.Round;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.fail;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class GameServiceTest
{
    @Autowired
    private GameService gamesService;


    @Test
    void testNewGame()
    {
        String gameID = this.gamesService.StartNewDefaultGame();
        Game retrievedGame = this.getGameByIDAndExpectNoErrors(gameID);

        Assertions.assertNotNull(retrievedGame, "The returned game by the service can not be null");

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesService.getGameByIdentifier(null),
                "When trying to look for a game with a NULL ID, an exception should be thrown");

        this.deleteGameAndExpectNoErrors(retrievedGame);
    }

    @Test
    void testGetGame()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesService.getGameByIdentifier(null));
        Assertions.assertThrows(GameNotFoundException.class, () -> this.gamesService.getGameByIdentifier("test2"));

        String gameID = this.gamesService.StartNewDefaultGame();
        Game retrievedGame = this.getGameByIDAndExpectNoErrors(gameID);

        Assertions.assertNotNull(retrievedGame, "the retrieved game should not be null");

        Round round = null;
        try
        {
            round = this.gamesService.generateNewRound(gameID);
        } catch (GameNotFoundException e)
        {
            fail("No exceptions should be thrown here");
        }

        Assertions.assertNotNull(round, "The generated round should not be null");

        Game gameFromService = this.getGameByIDAndExpectNoErrors(gameID);

        Assertions.assertEquals(retrievedGame.getTotalAmountOfRounds(), gameFromService.getTotalAmountOfRounds(),
                """
                        Both amount of rounds should be equal since both game objects
                        represents the same one.
                        """);
        Assertions.assertEquals(1, gameFromService.getTotalAmountOfRounds(), "The number of rounds should be 1");
        this.deleteGameAndExpectNoErrors(retrievedGame);
    }

    @Test
    void testRestartGame() throws GameNotFoundException
    {
        String gameID = this.gamesService.StartNewDefaultGame();
        this.gamesService.generateNewRound(gameID);
        this.gamesService.restartGame(gameID);
        Game retrievedGame = this.getGameByIDAndExpectNoErrors(gameID);
        Assertions.assertEquals(0,retrievedGame.getTotalAmountOfRounds(),"After reset, the rounds should be 0");
    }

    private Game getGameByIDAndExpectNoErrors(String gameID)
    {
        try
        {
            return this.gamesService.getGameByIdentifier(gameID);
        } catch (GameNotFoundException e)
        {
            fail("No exception should be thrown here");
        }
        return null;
    }

    private void deleteGameAndExpectNoErrors(Game game)
    {
        try
        {
            this.gamesService.deleteGame(game);
        } catch (GameNotFoundException e)
        {
            fail("No exceptions should be thrown here");
        }
    }

}