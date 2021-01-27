package com.rockpaperscissors.model.game;

import com.rockpaperscissors.dto.GameProgressDTO;
import com.rockpaperscissors.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class that models assembles a rock-paper-scissors game. It consists
 * of two implementations of the class {@link Player}, a list of {@link Round}, its
 * unique identifier and several methods to access its properties as well as one method
 * {@link #playNewRound()} responsible of generating a new round.
 *
 * It uses the open-close and the single responsibility principles.
 *
 */
public class Game
{

    private final List<Round> gameRounds;

    private final Player playerOne;
    private final Player playerTwo;

    private final String gameID;


    /**
     * In order to construct a game, it is needed to supply both player implementation as well as
     * the ID.
     * @param playerOne The first player, can not be null
     * @param playerTwo The second player, can not be null
     * @param gameID The ID of the game, can not be null
     * @throws IllegalArgumentException when one of its parameter is null
     */
    public Game(Player playerOne, Player playerTwo, String gameID)
    {
        if (playerOne == null || playerTwo == null)
        {
            throw new IllegalArgumentException("Error while constructing the game, no player can be null");
        }
        if (gameID == null)
        {
            throw new IllegalArgumentException("The ID of the game can not be null");
        }
        this.gameRounds = new ArrayList<>();
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.gameID = gameID;
    }


    //<editor-fold desc="Public interface">
    public void resetGame()
    {
        this.gameRounds.clear();
    }

    /**
     * This method will evaluate the movements performed by each player, which are
     * the result of both calls to {@link Player#performMovement()} and
     * apply the game rules to see who wins or if it was a tie. As a bonus,
     * it returns the just created {@link Round} for reference after it has been
     * added to {@link #gameRounds}
     *
     * @return The new {@link Round}
     */
    public Round playNewRound()
    {
        // Getting the movement of the players
        MovementTypes playerOneMovement = this.playerOne.performMovement();
        MovementTypes playerTwoMovement = this.playerTwo.performMovement();

        // Initializing the result to DRAW
        RoundOutcome roundResult = RoundOutcome.DRAW;

        // Comparing movements
        if (playerTwoMovement != playerOneMovement)
        {
            roundResult = (playerOneMovement.beats(playerTwoMovement) ? RoundOutcome.PLAYER1 : RoundOutcome.PLAYER2);
        }

        // Creating round object, adding it to the list and returning it
        Round result = new Round(roundResult, playerOneMovement, playerTwoMovement);
        this.gameRounds.add(result);
        return result;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public int getTotalAmountOfRounds()
    {
        return this.gameRounds.size();
    }

    /**
     * This getter returns an instance of {@link GameProgressDTO} which is an
     * immutable object representing the progress of the game. It is done in such
     * way to avoid the state of the game being modified externally.
     *
     * @return The immutable object with the game progress inside
     */
    public GameProgressDTO getProgress()
    {
        return new GameProgressDTO(this.gameRounds);
    }

    public String getGameID()
    {
        return gameID;
    }
    //</editor-fold>
}
