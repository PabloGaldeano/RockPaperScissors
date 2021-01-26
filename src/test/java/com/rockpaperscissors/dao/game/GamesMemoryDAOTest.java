package com.rockpaperscissors.dao.game;

import com.rockpaperscissors.exceptions.dao.RecordAlreadyExistsException;
import com.rockpaperscissors.exceptions.dao.RecordNotFoundException;
import com.rockpaperscissors.exceptions.game.GameAlreadyExistException;
import com.rockpaperscissors.exceptions.game.GameNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GamesMemoryDAOTest
{
    @Autowired
    @Qualifier("Memory")
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
            this.gamesDatabase.delete(this.testGame);
        } catch (RecordNotFoundException e)
        {
            // ignore
        }
    }


    @Test
    void testInsert()
    {
        this.insertGameInDBAndExpectNoErrors();

        Game fromDB = this.gamesDatabase.get(this.testGame.getGameID()).orElse(null);

        // Checking the element in the DB is the same and is not null
        Assertions.assertNotNull(fromDB, "The game in the database, should not be null since it has been inserted");
        Assertions.assertSame(this.testGame, fromDB, "Both objects should be the same one");

        // Asserting exceptions are thrown when saving with wrong parameters and when trying to save the same element
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesDatabase.save(null));
        Assertions.assertThrows(RecordAlreadyExistsException.class, () -> this.gamesDatabase.save(this.testGame));
    }

    @Test
    void testDelete()
    {
        Assertions.assertThrows(RecordNotFoundException.class, () -> this.gamesDatabase.delete(this.testGame));

        this.insertGameInDBAndExpectNoErrors();

        try
        {
            this.gamesDatabase.delete(this.testGame);
        } catch (RecordNotFoundException e)
        {
            fail("No exception should be thrown here");
        }
    }

    @Test
    public void testGet()
    {
        Game gameFromDB = this.gamesDatabase.get("test").orElse(null);
        Assertions.assertNull(gameFromDB);
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.gamesDatabase.get(null));
    }

    @Test
    public void testNewRound()
    {

    }

    @Test
    public void testGetAll()
    {
        Assertions.assertEquals(0, this.gamesDatabase.getAll().size());

        this.insertGameInDBAndExpectNoErrors();

        Assertions.assertEquals(1, this.gamesDatabase.getAll().size());

        try
        {
            this.gamesDatabase.delete(this.testGame);
        } catch (RecordNotFoundException e)
        {
            fail("No exception should be thrown here");
        }

        Assertions.assertEquals(0, this.gamesDatabase.getAll().size());
    }

    // ######################### UTILITY METHODS #########################

    private void insertGameInDBAndExpectNoErrors()
    {
        try
        {
            this.gamesDatabase.save(this.testGame);
        } catch (RecordAlreadyExistsException e)
        {
            fail("No exception should be thrown here");
        }
    }
}