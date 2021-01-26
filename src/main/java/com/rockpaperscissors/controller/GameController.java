package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/games")
@RestController
public class GameController
{

    @Autowired
    private GameService gameService;

    @PostMapping(value = "/create", produces = "application/json")
    public SystemResponse createNewGame()
    {
        return null;
    }

    @PostMapping(value="/newRound", consumes = "application/json", produces = "application/json")
    public SystemResponse newRoundInGame(@RequestBody String gameID)
    {
        return null;
    }


    @GetMapping(value="/getProgress", consumes = "application/json", produces = "application/json")
    public SystemResponse getGameProgress(@RequestBody String gameID)
    {
        return null;
    }

    @PostMapping(value="/restart", consumes = "application/json", produces = "application/json")
    public SystemResponse restartGame()
    {
        return null;
    }
}
