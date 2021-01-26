package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.exceptions.game.GameNotFoundException;
import com.rockpaperscissors.model.game.Game;
import com.rockpaperscissors.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;

@RequestMapping("/games")
@RestController
public class GameController
{

    private final GameService gameService;

    public GameController(GameService gameService)
    {
        this.gameService = gameService;
    }

    @PostMapping(value = "/create", produces = "application/json")
    public @ResponseBody SystemResponse createNewGame()
    {
        return null;
    }

    @PostMapping(value="/newRound", consumes = "application/json", produces = "application/json")
    public @ResponseBody SystemResponse newRoundInGame(@RequestBody String gameID)
    {
        return null;
    }


    @GetMapping(value="/{id}", produces = "application/json")
    public @ResponseBody SystemResponse getGameProgress(@PathVariable("id") String gameID)
    {
        try
        {
            Game requestedGame = this.gameService.getGameByIdentifier(gameID);
            return SystemResponse.generateSuccessResponse(requestedGame.getProgress());
        } catch (GameNotFoundException e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested game does not exists");
        }
    }

    @PostMapping(value="/restart", consumes = "application/json", produces = "application/json")
    public @ResponseBody SystemResponse restartGame()
    {
        return null;
    }
}
