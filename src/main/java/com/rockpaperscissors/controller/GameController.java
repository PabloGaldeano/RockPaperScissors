package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.exceptions.game.GameNotFoundException;
import com.rockpaperscissors.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.Callable;

@RequestMapping("/game")
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
        return this.executeServicePetition(() ->
                SystemResponse.generateSuccessResponse(this.gameService.StartNewDefaultGame()));
    }

    @PatchMapping(value = "{id}/newRound", produces = "application/json")
    public @ResponseBody SystemResponse newRoundInGame(@PathVariable("id") String gameID)
    {
        return this.executeServicePetition(() ->
                SystemResponse.generateSuccessResponse(this.gameService.generateNewRound(gameID)));
    }


    @GetMapping(value = "{id}", produces = "application/json")
    public @ResponseBody SystemResponse getGameProgress(@PathVariable("id") String gameID)
    {
        return this.executeServicePetition(() ->
                SystemResponse.generateSuccessResponse(this.gameService.getGameByIdentifier(gameID).getProgress()));
    }

    @PutMapping(value = "{id}/restart", produces = "application/json")
    public @ResponseBody SystemResponse restartGame(@PathVariable("id") String gameID)
    {
        return this.executeServicePetition(() ->
        {
            this.gameService.restartGame(gameID);
            return SystemResponse.generateSuccessResponse(null);
        });
    }


    private SystemResponse executeServicePetition(Callable<SystemResponse> func)
    {
        try
        {
            return func.call();
        } catch (GameNotFoundException e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested game does not exists");
        } catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Please try it again later");
        }
    }
}
