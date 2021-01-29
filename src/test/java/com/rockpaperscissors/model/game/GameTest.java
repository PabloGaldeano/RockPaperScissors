package com.rockpaperscissors.model.game;

import com.rockpaperscissors.model.game.round.Round;
import com.rockpaperscissors.model.game.round.RoundOutcome;
import com.rockpaperscissors.model.player.FixedMovementPlayer;
import com.rockpaperscissors.model.player.RandomMovementPlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest
{

    private Game gameToTest;

    @BeforeEach
    public void testSetUp()
    {
        this.gameToTest = new Game(new FixedMovementPlayer(MovementTypes.ROCK), new FixedMovementPlayer(MovementTypes.PAPER), "test");
    }

    //<editor-fold desc="Tests">
    @Test
    void testInit()
    {
        // Checking initial state
        Assertions.assertEquals(0, this.gameToTest.getTotalAmountOfRounds(), "The total amount of rounds at the start should be 0");
    }

    /**
     * This method will test the construction of a game using a sequences of
     * arguments where at least one will be null.
     */
    @Test
    void testNewGameWithNulls()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(null, null, "test"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(null, new RandomMovementPlayer(), "test"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(new RandomMovementPlayer(), null, "test"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(new RandomMovementPlayer(), new RandomMovementPlayer(), null));
    }

    @Test
    void testNewRound()
    {
        // Generating new round
        Round playedRound = this.gameToTest.playNewRound();

        // Checking the state has changed
        this.checkNumberOfRounds(1);

        // Checking the returned round matches the test one
        Assertions.assertNotNull(playedRound, "The round generated should not be null");
        Round resultRound = new Round(RoundOutcome.PLAYER2, MovementTypes.ROCK, MovementTypes.PAPER);
        Assertions.assertEquals(resultRound, playedRound, "The resulting round from the game should be equal to the one specified in the test");
    }

    @Test
    void testResetGame()
    {
        // Generating new round and checking the state
        this.gameToTest.playNewRound();
        this.checkNumberOfRounds(1);

        // Restarting the game and checking the state
        this.gameToTest.resetGame();
        this.checkNumberOfRounds(0);

    }
    //</editor-fold>

    //<editor-fold desc="Checker methods">
    private void checkNumberOfRounds(int expectedRounds)
    {
        Assertions.assertEquals(expectedRounds, this.gameToTest.getTotalAmountOfRounds(), "The total amount of rounds should be 1");
    }
    //</editor-fold>

}