package com.rockpaperscissors.model.game;

import com.rockpaperscissors.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private final List<Round> gameRounds;

    private final Player playerOne;
    private final Player playerTwo;

    private final String gameID;


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

    public void resetGame()
    {
        this.gameRounds.clear();
    }

    public Round playNewRound()
    {
        MovementTypes playerOneMovement = this.playerOne.performMovement();
        MovementTypes playerTwoMovement = this.playerTwo.performMovement();

        RoundOutcome roundResult = RoundOutcome.DRAW;

        if (playerTwoMovement != playerOneMovement)
        {
            roundResult = (playerOneMovement.beats(playerTwoMovement) ? RoundOutcome.PLAYER1 : RoundOutcome.PLAYER2);
        }

        Round result = new Round(roundResult, playerOneMovement, playerTwoMovement);
        this.gameRounds.add(result);
        return result;
    }

    public int getTotalAmountOfRounds()
    {
        return this.gameRounds.size();
    }

    public String getGameID()
    {
        return gameID;
    }
}
