package com.rockpaperscissors.dao.game;

import com.rockpaperscissors.model.game.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository(value = "Memory")
public class GamesMemoryDAO implements IGamesDAO
{

    private final Map<String, List<Game>> games;

    public GamesMemoryDAO()
    {
        this.games = new HashMap<>();
    }

    @Override
    public Optional<Game> get(long id)
    {
        return Optional.empty();
    }

    @Override
    public List<Game> getAll()
    {
        return null;
    }

    @Override
    public void save(Game game)
    {

    }

    @Override
    public void update(Game game, String[] params)
    {

    }

    @Override
    public void delete(Game game)
    {

    }
}
