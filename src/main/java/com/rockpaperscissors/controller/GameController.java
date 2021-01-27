package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.dto.GameProgressDTO;
import com.rockpaperscissors.exceptions.game.GameNotFoundException;
import com.rockpaperscissors.model.game.Game;
import com.rockpaperscissors.service.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public @ResponseBody
    SystemResponse createNewGame()
    {
        return SystemResponse.generateSuccessResponse(this.gameService.StartNewDefaultGame());
    }

    @PatchMapping(value = "{id}/newRound", produces = "application/json")
    public @ResponseBody
    SystemResponse newRoundInGame(@PathVariable("id") String gameID)
    {
        try
        {
            return SystemResponse.generateSuccessResponse(this.gameService.generateNewRound(gameID));
        } catch (GameNotFoundException e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested game does not exists");
        }
    }


    @GetMapping(value = "{id}", produces = "application/json")
    public @ResponseBody
    SystemResponse getGameProgress(@PathVariable("id") String gameID)
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

    @PatchMapping(value = "{id}/restart", produces = "application/json")
    public @ResponseBody  SystemResponse restartGame(@PathVariable("id") String gameID)
    {
        try
        {
            this.gameService.restartGame(gameID);
            return SystemResponse.generateSuccessResponse(null);

        } catch (GameNotFoundException e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested game does not exists");
        }
    }
}
