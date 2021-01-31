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
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This class exposes all the different calls that can be performed over
 * a specific game and also includes a method to introduce a new game in the system.
 * <p>
 * The way this service creates a game is by {@link #startNewDefaultGame()} which construct
 * the game by applying the rules specified in the requirements. Leaving the
 * possibility opened for a game to be constructed differently
 */
@Service
public class GameService
{
    /**
     * The game persisance layer
     */
    private final IGamesDAO gamesDatabase;

    /**
     * The statistics object
     */
    private final Statistics statistics;

    public GameService(@Qualifier("GamesMemory") IGamesDAO gamesDatabase)
    {
        this.gamesDatabase = gamesDatabase;
        this.statistics = new Statistics();
    }

    /**
     * This method will look for the game corresponding to the ID given by parameter and invoking the method
     * {@link Game#playNewRound()}. After that call is performed its result is returned to the upper layers.
     *
     * @param gameIdentifier the ID of the game
     * @return The newly generated {@link Round}
     * @throws GameNotFoundException Thrown when the requested game does not exists.
     */
    public Round generateNewRound(String gameIdentifier) throws GameNotFoundException
    {
        Game selectedGame = this.getGameByIdentifier(gameIdentifier);
        Round round = selectedGame.playNewRound();
        this.statistics.countNewRound(round);
        return round;
    }

    /**
     * This method will construct a game where the players involved will be {@link RandomMovementPlayer} and
     * {@link FixedMovementPlayer} where the last one will always generate a movement of type {@link MovementTypes#ROCK}
     *
     * @return The ID of the newly generated game
     */
    public String startNewDefaultGame()
    {
        // Generating the ID
        String gameID = UUID.randomUUID().toString();

        // Constructing the game
        Game newGame = new Game(new RandomMovementPlayer(), new FixedMovementPlayer(MovementTypes.ROCK), gameID);

        // Sending it to the persistence layer
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

    /**
     * This method will delete a game from the database
     *
     * @param gameIdentifier the ID of the game to delete
     * @throws GameNotFoundException thrown when the requested game does not exist.
     */
    public void deleteGame(String gameIdentifier) throws GameNotFoundException
    {
        try
        {
            this.gamesDatabase.delete(gameIdentifier);
        } catch (RecordNotFoundException ex)
        {
            throw new GameNotFoundException(gameIdentifier);
        }
    }

    /**
     * This method will return a game based on the ID given by parameter or will throw an exception
     * if the game could not be found.
     *
     * @param gameIdentifier The ID of the requested game
     * @return The game that has the given ID
     * @throws GameNotFoundException when the game could not be found
     */
    public Game getGameByIdentifier(String gameIdentifier) throws GameNotFoundException
    {
        return this.gamesDatabase.get(gameIdentifier).orElseThrow(() -> new GameNotFoundException(gameIdentifier));
    }

    /**
     * This method will restart an already existing game, meaning its progress will be cleared.
     *
     * @param gameIdentifier The ID of the game to reset.
     * @throws GameNotFoundException when the game to reset could not be found
     */
    public void restartGame(String gameIdentifier) throws GameNotFoundException
    {
        Game requestedGame = this.getGameByIdentifier(gameIdentifier);
        requestedGame.resetGame();
    }

    /**
     * This is a getter for the {@link Statistics} object.
     *
     * @return the instance of the statistics.
     */
    public Statistics getStatistics()
    {
        return this.statistics;
    }

}
