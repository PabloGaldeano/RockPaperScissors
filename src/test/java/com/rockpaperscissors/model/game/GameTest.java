package com.rockpaperscissors.model.game;

import com.rockpaperscissors.model.game.round.Round;
import com.rockpaperscissors.model.game.round.RoundOutcome;
import com.rockpaperscissors.model.player.FixedMovementPlayer;
import com.rockpaperscissors.model.player.RandomMovementPlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class will test out all the logic within the Game. Like the exceptions thrown
 * when constructing a new game, the state change when generating a new round etc...
 */
class GameTest
{

    /**
     * The game used to invoke the operations on.
     */
    private Game gameToTest;

    @BeforeEach
    public void testSetUp()
    {
        this.gameToTest = new Game(new FixedMovementPlayer(MovementTypes.ROCK), new FixedMovementPlayer(MovementTypes.PAPER), "test");
    }

    //<editor-fold desc="Tests">

    /**
     * Method to make sure the size of the round list is 0 when the game gets created
     */
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

    /**
     * This method will test out if the size of the progress list
     * grows according with the numbers of calls performed to {@link Game#playNewRound()}
     * It will also construct a new round to test out the round data
     * contains the information according to the game rules
     */
    @Test
    void testNewRound()
    {
        // Generating new round
        Round playedRound = this.gameToTest.playNewRound();

        // Checking the state has changed
        this.assertNumberOfRounds(1);

        // Checking the returned round matches the test one
        Assertions.assertNotNull(playedRound, "The round generated should not be null");
        Round resultRound = new Round(RoundOutcome.PLAYER2, MovementTypes.ROCK, MovementTypes.PAPER);
        Assertions.assertEquals(resultRound, playedRound, "The resulting round from the game should be equal to the one specified in the test");
    }

    /**
     * Method to test if the progress data is cleared when invoking the reset method.
     */
    @Test
    void testResetGame()
    {
        // Generating new round and checking the state
        this.gameToTest.playNewRound();
        this.assertNumberOfRounds(1);

        // Restarting the game and checking the state
        this.gameToTest.resetGame();
        this.assertNumberOfRounds(0);

    }
    //</editor-fold>

    //<editor-fold desc="Checker methods">

    /**
     * Method used to assert if the number of rounds inside the game is the one given by parameter
     *
     * @param expectedRounds The number of rounds that should be inside the game
     */
    private void assertNumberOfRounds(int expectedRounds)
    {
        Assertions.assertEquals(expectedRounds, this.gameToTest.getTotalAmountOfRounds(), "The total amount of rounds should be 1");
    }
    //</editor-fold>

}