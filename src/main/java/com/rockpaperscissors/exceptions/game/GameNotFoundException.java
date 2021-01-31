package com.rockpaperscissors.exceptions.game;

public class GameNotFoundException extends Exception
{
     public GameNotFoundException(String gameID)
    {
        super(String.format("The game with ID: '%s' was not found", gameID));

    }
}
