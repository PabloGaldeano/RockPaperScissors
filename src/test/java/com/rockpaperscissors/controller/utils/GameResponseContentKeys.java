package com.rockpaperscissors.controller.utils;

/**
 * This enumeration holds the different keys of the different values
 * sent by the controller through the SystemResponse.
 */
public enum GameResponseContentKeys
{
    ROUND_PLAYER_ONE_MOVEMENT("player_one_movement"),
    ROUND_PLAYER_TWO_MOVEMENT("player_two_movement"),
    ROUND_RESULT("result");

    private final String keyName;

    GameResponseContentKeys(String keyName)
    {
        this.keyName = keyName;
    }

    @Override
    public String toString()
    {
        return keyName;
    }
}
