package com.rockpaperscissors.service;

import com.rockpaperscissors.dao.game.IGamesDAO;
import com.rockpaperscissors.exceptions.dao.RecordAlreadyExistsException;
import com.rockpaperscissors.exceptions.dao.RecordNotFoundException;
import com.rockpaperscissors.exceptions.game.GameNotFoundException;
import com.rockpaperscissors.model.game.Game;
import com.rockpaperscissors.model.game.MovementTypes;
import com.rockpaperscissors.model.game.Round;
import com.rockpaperscissors.model.player.FixedMovementPlayer;
import com.rockpaperscissors.model.player.RandomMovementPlayer;
import com.rockpaperscissors.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GameService
{

    private final IGamesDAO gamesDatabase;

    public GameService(@Qualifier("Memory") IGamesDAO gamesDatabase)
    {
        this.gamesDatabase = gamesDatabase;
    }

    public Round generateNewRound(String gameIdentifier) throws GameNotFoundException
    {
        Game selectedGame = this.getGameByIdentifier(gameIdentifier);
        return selectedGame.playNewRound();
    }

    public String StartNewDefaultGame()
    {
        String gameID = StringUtils.getRandomStringOfLength(32);

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
        }
        catch (RecordNotFoundException ex)
        {
            throw new GameNotFoundException(game);
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

}
