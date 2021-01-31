package com.rockpaperscissors.service;

import com.rockpaperscissors.dao.game.IGamesDAO;
import com.rockpaperscissors.exceptions.dao.RecordAlreadyExistsException;
import com.rockpaperscissors.exceptions.dao.RecordNotFoundException;
import com.rockpaperscissors.exceptions.game.GameNotFoundException;
import com.rockpaperscissors.model.game.Game;
import com.rockpaperscissors.model.game.MovementTypes;
import com.rockpaperscissors.model.game.round.Round;
import com.rockpaperscissors.model.player.FixedMovementPlayer;
import com.rockpaperscissors.model.player.RandomMovementPlayer;
import com.rockpaperscissors.model.statistics.Statistics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService
{

    private final IGamesDAO gamesDatabase;

    private Statistics statistics;

    public GameService(@Qualifier("GamesMemory") IGamesDAO gamesDatabase)
    {
        this.gamesDatabase = gamesDatabase;
        this.statistics = new Statistics();
    }

    public Round generateNewRound(String gameIdentifier) throws GameNotFoundException
    {
        Game selectedGame = this.getGameByIdentifier(gameIdentifier);
        Round round = selectedGame.playNewRound();
        this.statistics.countNewRound(round);
        return round;
    }
    public String StartNewDefaultGame()
    {
        String gameID = UUID.randomUUID().toString();

        Game newGame = new Game(new RandomMovementPlayer(), new FixedMovementPlayer(MovementTypes.ROCK), gameID);

        try
        {
            this.gamesDatabase.save(newGame);
        } catch (RecordAlreadyExistsException e)
        {
            // Todo: Sequence generator to guarantee no duplicates are inserted
            System.err.println(e.getMessage());
        }

        return gameID;
    }

    public void deleteGame(Game game) throws GameNotFoundException
    {
        try
        {
            this.gamesDatabase.delete(game);
        } catch (RecordNotFoundException ex)
        {
            throw new GameNotFoundException(game.getGameID());
        }
    }

    public Game getGameByIdentifier(String gameIdentifier) throws GameNotFoundException
    {
        return this.gamesDatabase.get(gameIdentifier).orElseThrow(() -> new GameNotFoundException(gameIdentifier));
    }

    public void restartGame(String gameIdentifier) throws GameNotFoundException
    {
        Game requestedGame = this.getGameByIdentifier(gameIdentifier);
        requestedGame.resetGame();
    }

    public Statistics getStatistics()
    {
        return this.statistics;
    }

}
