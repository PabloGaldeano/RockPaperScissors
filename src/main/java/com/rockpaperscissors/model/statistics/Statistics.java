package com.rockpaperscissors.model.statistics;

import com.rockpaperscissors.model.game.round.Round;

public class Statistics
{
    private int totalRoundsPlayed;
    private int totalPlayerOneWins;
    private int totalPlayerTwoWins;
    private int totalDraws;

    public Statistics()
    {
        this.totalDraws = 0;
        this.totalPlayerOneWins = 0;
        this.totalPlayerTwoWins = 0;
        this.totalRoundsPlayed = 0;
    }

    public void countNewRound(Round newRound)
    {
        if (newRound == null)
        {
            throw new IllegalArgumentException("The new round should not be null");
        }
        this.totalRoundsPlayed++;

        switch (newRound.result())
        {
            case PLAYER1 -> this.totalPlayerOneWins++;
            case DRAW -> this.totalDraws++;
            case PLAYER2 -> this.totalPlayerTwoWins++;
        }
    }


}
