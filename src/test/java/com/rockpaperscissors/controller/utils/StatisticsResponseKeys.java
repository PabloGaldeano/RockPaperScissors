package com.rockpaperscissors.controller.utils;

public enum StatisticsResponseKeys
{
    STATISTICS("statistics"),
    TOTAL_ROUNDS("totalRoundsPlayed"),
    TOTAL_PLAYER_ONE_WINS("totalPlayerOneWins"),
    TOTAL_PLAYER_TWO_WINS("totalPlayerTwoWins"),
    TOTAL_DRAWS("totalDraws");

    StatisticsResponseKeys(String keyName)
    {
        this.keyName = keyName;
    }

    private final String keyName;

    public String getKeyName()
    {
        return keyName;
    }
}
