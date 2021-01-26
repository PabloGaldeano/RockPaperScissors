package com.rockpaperscissors.exceptions.game;

import com.rockpaperscissors.model.game.Game;

public class GameAlreadyExistException extends Exception
{
    public GameAlreadyExistException(Game game)
    {
        super(String.format("The game with ID: %s already exists in the DB", game.getGameID()));
    }
}
