package com.rockpaperscissors.exceptions.game;


/**
 * This exception is used to inform about the request of a non existing game
 */
public class GameNotFoundException extends Exception
{
    public GameNotFoundException(String gameID)
    {
        super(String.format("The game with ID: '%s' was not found", gameID));

    }
}
