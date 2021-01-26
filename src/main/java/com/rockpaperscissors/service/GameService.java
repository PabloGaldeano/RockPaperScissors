package com.rockpaperscissors.service;

import com.rockpaperscissors.dao.game.IGamesDAO;
import com.rockpaperscissors.model.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GameService
{

    @Autowired
    @Qualifier("Memory")
    private IGamesDAO gamesDatabase;

    public void generateNewRound(String gameIdentifier)
    {

    }

    public String StartNewDefaultGame()
    {
        return null;
    }

    public Game getGameByIdentifier(String gameIdentifier)
    {
        return null;
    }

}
