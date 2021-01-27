package com.rockpaperscissors.dao.game;

import com.rockpaperscissors.exceptions.dao.RecordAlreadyExistsException;
import com.rockpaperscissors.exceptions.dao.RecordNotFoundException;
import com.rockpaperscissors.model.game.Game;
import com.rockpaperscissors.model.game.MovementTypes;
import com.rockpaperscissors.model.player.FixedMovementPlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GamesMemoryDAOTest
{
    @Autowired
    @Qualifier("GamesMemory")
    private IGamesDAO gamesDatabase;

    private Game testGame;

    public GamesMemoryDAOTest()
    {
        this.testGame = new Game(new FixedMovementPlayer(MovementTypes.ROCK), new FixedMovementPlayer(MovementTypes.PAPER), "test");

    }

    @BeforeEach
    public void testSetUp()
    {
        try
        {
            // In here the exception is needed because there can be some test that do not insert
            // elements in the DB or they deleted for some reason, hence this call has to succeed and not
            // break the test for a record not found exception
            this.gamesDatabase.delete(this.testGame);
        } catch (RecordNotFoundException e)
        {
            // ignore
        }
    }


    @Test
    void testInsert() throws RecordAlreadyExistsException
    {
         this.gamesDatabase.save(this.testGame);

        Game fromDB = this.gamesDatabase.get(this.testGame.getGameID()).orElse(null);

        // Checking the element in the DB is the same and is not null
        Assertions.assertNotNull(fromDB, "The game in the database, should not be null since it has been inserted");
        Assertions.assertSame(this.testGame, fromDB, "Both objects should be the same one");

        // Asserting exceptions are thrown when saving with wrong parameters and when trying to save the same element
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesDatabase.save(null));
        Assertions.assertThrows(RecordAlreadyExistsException.class, () -> this.gamesDatabase.save(this.testGame));
    }

    @Test
    void testDelete() throws RecordNotFoundException, RecordAlreadyExistsException
    {
        // Asserting there are no record in the database
        Assertions.assertThrows(RecordNotFoundException.class, () -> this.gamesDatabase.delete(this.testGame));
        this.gamesDatabase.save(this.testGame);

        // Asserting we can delete the record with no exceptions
        this.gamesDatabase.delete(this.testGame);
    }

    @Test
    public void testGet()
    {
        Game gameFromDB = this.gamesDatabase.get("test").orElse(null);
        Assertions.assertNull(gameFromDB);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesDatabase.get(null));
    }

    @Test
    public void testGetAll() throws RecordAlreadyExistsException, RecordNotFoundException
    {
        Assertions.assertEquals(0, this.gamesDatabase.getAll().size());

        this.gamesDatabase.save(this.testGame);
        Assertions.assertEquals(1, this.gamesDatabase.getAll().size());


        this.gamesDatabase.delete(this.testGame);
        Assertions.assertEquals(0, this.gamesDatabase.getAll().size());
    }
}