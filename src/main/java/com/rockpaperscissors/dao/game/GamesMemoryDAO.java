package com.rockpaperscissors.dao.game;

import com.rockpaperscissors.exceptions.dao.RecordAlreadyExistsException;
import com.rockpaperscissors.exceptions.dao.RecordNotFoundException;
import com.rockpaperscissors.model.game.Game;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This is a specific implementation of {@link IGamesDAO} where the entities
 * will be saved in memory
 */
@Repository("GamesMemory")
public class GamesMemoryDAO implements IGamesDAO
{

    private final Map<String, Game> games = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Game> get(String id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("The id of the game can not be null");
        }
        return Optional.ofNullable(this.games.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Game> getAll()
    {
        return this.games.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Game entity) throws RecordAlreadyExistsException
    {
        this.checkIfGameIsValid(entity);

        if (this.doesGameExists(entity.getGameID()))
        {
            throw new RecordAlreadyExistsException(entity.getGameID());
        }

        this.games.put(entity.getGameID(), entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(String id, Game value) throws RecordNotFoundException
    {
        this.checkIfGameIsValid(value);
        if (id == null)
        {
            throw new IllegalArgumentException("The ID of the game can not be null");
        }

        if (!doesGameExists(value.getGameID()))
        {
            throw new RecordNotFoundException(value.getGameID());
        }

        this.games.put(id, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String id) throws RecordNotFoundException
    {

        if (!this.doesGameExists(id))
        {
            throw new RecordNotFoundException(id);
        }

        this.games.remove(id);
    }

    /**
     * This method will perform the checks to make sure the game is in
     * the appropriate shape.
     *
     * @param game The game entity to check
     */
    private void checkIfGameIsValid(Game game)
    {
        if (game == null)
        {
            throw new IllegalArgumentException("The game to insert can not be null");
        }
    }

    /**
     * Method to check if the game does exists or not by querying the {@link #games} map
     *
     * @param ID the ID of the game to check
     * @return <code>true</code> if the game does exist, <code>false</code> otherwise
     */
    private boolean doesGameExists(String ID)
    {
        return this.games.containsKey(ID);
    }
}
