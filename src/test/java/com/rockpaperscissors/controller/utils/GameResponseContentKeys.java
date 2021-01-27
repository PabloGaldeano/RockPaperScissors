package com.rockpaperscissors.controller.utils;

/**
 * This enumeration holds the different keys of the different values
 * sent by the controller through the SystemResponse.
 */
public enum GameResponseContentKeys
{
    GAME_PROGRESS("gameProgress"),
    ROUND_PLAYER_ONE_MOVEMENT("player_one_movement"),
    ROUND_PLAYER_TWO_MOVEMENT("player_two_movement"),
    ROUND_RESULT("result");

    GameResponseContentKeys(String keyName)
    {
        this.keyName = keyName;
    }

    private final String keyName;

    public String getKeyName()
    {
        return keyName;
    }
}
