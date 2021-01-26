package com.rockpaperscissors.dao.game;

import com.rockpaperscissors.model.game.Game;
import com.rockpaperscissors.model.game.MovementTypes;
import com.rockpaperscissors.model.player.FixedMovementPlayer;
import org.junit.jupiter.api.Assertions;
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

    @Test
    void testInsert()
    {
        Game test = new Game(new FixedMovementPlayer(MovementTypes.ROCK), new FixedMovementPlayer(MovementTypes.PAPER), "test");

        this.gamesDatabase.save(test);

        Game fromDB = this.gamesDatabase.get(test.getGameID()).orElse(null);

        Assertions.assertNotNull(fromDB, "The game in the database, should not be null since it has been inserted");

        Assertions.assertSame(test, fromDB, "Both objects should be the same one");



    }
}