package com.rockpaperscissors.model.game;

import com.rockpaperscissors.model.player.FixedMovementPlayer;
import com.rockpaperscissors.model.player.Player;
import com.rockpaperscissors.model.player.RandomMovementPlayer;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private final List<Round> gameRounds;

    private final Player playerOne;
    private final Player playerTwo;

    public Game()
    {
        this.gameRounds = new ArrayList<>();
        this.playerOne = new FixedMovementPlayer(MovementTypes.ROCK);
        this.playerTwo = new RandomMovementPlayer();
    }

    public Game(Player playerOne, Player playerTwo)
    {
        if (playerOne == null || playerTwo == null)
        {
            throw new IllegalArgumentException("Error while constructing the game, no player can be null");
        }
        this.gameRounds = new ArrayList<>();
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public void resetGame()
    {
        this.gameRounds.clear();
    }

    public Round playNewRound()
    {
        return null;
    }

    public int getTotalAmountOfRounds()
    {
        return this.gameRounds.size();
    }
}
