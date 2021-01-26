package com.rockpaperscissors.service;

import com.rockpaperscissors.model.game.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        Game retrievedGame = this.gamesService.getGameByIdentifier(gameID);

        Assertions.assertNotNull(retrievedGame, "The returned game by the service can not be null");

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesService.getGameByIdentifier(null),
                "When trying to look for a game with a NULL ID, an exception should be thrown");
    }

    @Test
    void testGameManipulation()
    {
        String gameID = this.gamesService.StartNewDefaultGame();
        Game retrievedGame = this.gamesService.getGameByIdentifier(gameID);

        retrievedGame.playNewRound();

        Game gameFromService = this.gamesService.getGameByIdentifier(gameID);

        Assertions.assertEquals(retrievedGame.getTotalAmountOfRounds(), gameFromService.getTotalAmountOfRounds(),
                """
                Both amount of rounds should be equal since both game objects
                represents the same one.
                """);

    }
}