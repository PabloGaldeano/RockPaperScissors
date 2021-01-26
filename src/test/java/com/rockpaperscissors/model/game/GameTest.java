package com.rockpaperscissors.model.game;

import com.rockpaperscissors.model.player.FixedMovementPlayer;
import com.rockpaperscissors.model.player.RandomMovementPlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest
{

    private Game gameToTest;

    @BeforeEach
    public void testSetUp()
    {
        this.gameToTest = new Game();
    }

    @Test
    void testInit()
    {
        Assertions.assertEquals(0, this.gameToTest.getTotalAmountOfRounds(), "The total amount of rounds at the start should be 0");
    }

    @Test
    void testNewGameWithNulls()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(null, new RandomMovementPlayer()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Game(new RandomMovementPlayer(), null));
    }

    @Test
    void testNewRound()
    {
        this.gameToTest = new Game(new FixedMovementPlayer(MovementTypes.ROCK), new FixedMovementPlayer(MovementTypes.PAPER));
        Round playedRound = this.gameToTest.playNewRound();
        this.testNumberOfRounds(1);
        Assertions.assertNotNull(playedRound, "The round generated should not be null");
        Round resultRound = new Round(RoundOutcome.PLAYER2, MovementTypes.ROCK, MovementTypes.PAPER);
        Assertions.assertEquals(resultRound, playedRound, "The resulting round from the game should be equal to the one specified in the test");
    }

    @Test
    void testResetGame()
    {
        this.gameToTest.playNewRound();
        this.testNumberOfRounds(1);
        this.gameToTest.resetGame();
        this.testNumberOfRounds(0);

    }

    private void testNumberOfRounds( int expectedRounds)
    {
        Assertions.assertEquals(expectedRounds, this.gameToTest.getTotalAmountOfRounds(), "The total amount of rounds should be 1");
    }

}