package com.rockpaperscissors.dao.game;

import com.rockpaperscissors.model.game.Game;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("Memory")
public class GamesMemoryDAO implements IGamesDAO
{

    private final Map<String, Game> games;

    public GamesMemoryDAO()
    {
        this.games = new HashMap<>();
    }

    @Override
    public Optional<Game> get(String id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("The id of the game can not be null");
        }
        return Optional.ofNullable(this.games.get(id));
    }

    @Override
    public Collection<Game> getAll()
    {
        return this.games.values();
    }

    @Override
    public void save(Game game)
    {

    }

    @Override
    public void update(String id, Game game)
    {

    }

    @Override
    public void delete(Game game)
    {

    }
}
