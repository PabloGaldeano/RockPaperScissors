package com.rockpaperscissors.controller.utils;

/**
 * This enumeration contains all the different keys that should be
 * inside the response of the get statistics call
 */
public enum StatisticsResponseKeys
{
    TOTAL_ROUNDS("totalRoundsPlayed"),
    TOTAL_PLAYER_ONE_WINS("totalPlayerOneWins"),
    TOTAL_PLAYER_TWO_WINS("totalPlayerTwoWins"),
    TOTAL_DRAWS("totalDraws");

    private final String keyName;

    StatisticsResponseKeys(String keyName)
    {
        this.keyName = keyName;
    }

    @Override
    public String toString()
    {
        return keyName;
    }
}
