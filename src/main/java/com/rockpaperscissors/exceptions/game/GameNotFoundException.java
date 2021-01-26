package com.rockpaperscissors.exceptions.game;

import com.rockpaperscissors.model.game.Game;

public class GameNotFoundException extends Exception
{
    public GameNotFoundException(Game game)
    {
        this(game.getGameID());
    }

    public GameNotFoundException(String gameID)
    {
        super(String.format("The game with ID: '%s' was not found", gameID));

    }
}
