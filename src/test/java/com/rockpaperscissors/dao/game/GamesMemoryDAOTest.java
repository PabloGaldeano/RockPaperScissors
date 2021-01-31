package com.rockpaperscissors.dao.game;

import com.rockpaperscissors.exceptions.dao.DaoException;
import com.rockpaperscissors.exceptions.dao.RecordAlreadyExistsException;
import com.rockpaperscissors.exceptions.dao.RecordNotFoundException;
import com.rockpaperscissors.model.game.Game;
import com.rockpaperscissors.model.game.MovementTypes;
import com.rockpaperscissors.model.player.FixedMovementPlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This class will test out the DAO implementation for the games and make sure
 * that for every operation perform the state of the DAO does not end in a unwanted
 * state.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class GamesMemoryDAOTest
{
    private IGamesDAO gamesDatabase;

    private Game testGame;

    public GamesMemoryDAOTest(@Qualifier("GamesMemory") IGamesDAO gamesDatabase)
    {
        this.testGame = new Game(new FixedMovementPlayer(MovementTypes.ROCK), new FixedMovementPlayer(MovementTypes.PAPER), "test");
        this.gamesDatabase = gamesDatabase;
    }

    @BeforeEach
    public void testSetup()
    {
        try
        {
            // In here the exception is needed because there can be some test that do not insert
            // elements in the DB or they deleted for some reason, hence this call has to succeed and not
            // break the test for a record not found exception
            this.gamesDatabase.delete(this.testGame.getGameID());
        } catch (RecordNotFoundException e)
        {
            // ignore
        }
    }


    /**
     * This method will attempt to insert a record in the database in different ways
     * in order to assert the appropriate exceptions are thrown and to check if the
     * data inserted ends up correctly inside of the database;
     *
     * @throws RecordAlreadyExistsException Thrown when a record is already inserted in the DB.
     */
    @Test
    void testInsert() throws RecordAlreadyExistsException
    {
        // Saving and retrieving a test game
        this.gamesDatabase.save(this.testGame);
        Game fromDB = this.gamesDatabase.get(this.testGame.getGameID()).orElse(null);

        // Checking the element in the DB is the same and is not null
        Assertions.assertNotNull(fromDB, "The game in the database, should not be null since it has been inserted");
        Assertions.assertSame(this.testGame, fromDB, "Both objects should be the same one");

        // Asserting exceptions are thrown when saving with wrong parameters and when trying to save the same element
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesDatabase.save(null));
        Assertions.assertThrows(RecordAlreadyExistsException.class, () -> this.gamesDatabase.save(this.testGame));
    }

    /**
     * Same as {@link #testInsert()} this method will attempt to perform the
     * delete operation in different ways to make sure the exceptions are thrown
     *
     * @throws DaoException Thrown when something wrong happens.
     */
    @Test
    void testDelete() throws DaoException
    {
        // Asserting there are no record in the database
        Assertions.assertThrows(RecordNotFoundException.class, () -> this.gamesDatabase.delete(this.testGame.getGameID()));
        this.gamesDatabase.save(this.testGame);

        // Asserting we can delete the record with no exceptions
        this.gamesDatabase.delete(this.testGame.getGameID());
    }

    /**
     * Method to test out the get operation. As the previous tests it will make sure the data
     * is retrieved when it is meant to.
     */
    @Test
    public void testGet()
    {
        Game gameFromDB = this.gamesDatabase.get("test").orElse(null);
        Assertions.assertNull(gameFromDB);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesDatabase.get(null));
    }

    /**
     * Same as {@link #testGet()} but for {@link IGamesDAO#getAll()}
     *
     * @throws DaoException thrown when something wrong happened.
     */
    @Test
    public void testGetAll() throws DaoException
    {
        Assertions.assertEquals(0, this.gamesDatabase.getAll().size());

        this.gamesDatabase.save(this.testGame);
        Assertions.assertEquals(1, this.gamesDatabase.getAll().size());


        this.gamesDatabase.delete(this.testGame.getGameID());
        Assertions.assertEquals(0, this.gamesDatabase.getAll().size());
    }
}