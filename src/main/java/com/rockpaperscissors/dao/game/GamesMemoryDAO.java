package com.rockpaperscissors.dao.game;

import com.rockpaperscissors.exceptions.dao.RecordAlreadyExistsException;
import com.rockpaperscissors.exceptions.dao.RecordNotFoundException;
import com.rockpaperscissors.model.game.Game;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public void save(Game game) throws RecordAlreadyExistsException
    {
        this.checkIfGameIsValid(game);

        if (this.doesGameExists(game.getGameID()))
        {
            throw new RecordAlreadyExistsException(game.getGameID());
        }

        this.games.put(game.getGameID(), game);
    }

    @Override
    public void update(String id, Game game) throws RecordNotFoundException
    {
        this.checkIfGameIsValid(game);
        if (id == null)
        {
            throw new IllegalArgumentException("The ID of the game can not be null");
        }

        if (!doesGameExists(game.getGameID()))
        {
            throw new RecordNotFoundException(game.getGameID());
        }

        this.games.put(id, game);
    }

    @Override
    public void delete(Game game) throws RecordNotFoundException
    {
        this.checkIfGameIsValid(game);

        if (!this.doesGameExists(game.getGameID()))
        {
            throw new RecordNotFoundException(game.getGameID());
        }

        this.games.remove(game.getGameID());
    }

    private void checkIfGameIsValid(Game game)
    {
        if (game == null)
        {
            throw new IllegalArgumentException("The game to insert can not be null");
        }
    }

    private boolean doesGameExists(String ID)
    {
        return this.games.containsKey(ID);
    }
}
