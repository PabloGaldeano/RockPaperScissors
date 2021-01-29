package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This is the controller used to retrieve the statistics. This is because
 * {@link GameController} is the controller for the games themselves, and the
 * statistics are not precisely inside the game context from this perspective.
 *
 * That is why there is only one method to retrieve the statistics.
 */
@CrossOrigin("*")
@RequestMapping(produces = "application/json")
@RestController
public class StatisticsController
{
    @Autowired
    private GameService gameService;

    @GetMapping("/statistics")
    public @ResponseBody
    SystemResponse getStatistics()
    {
       return SystemResponse.generateSuccessResponse(this.gameService.getStatistics());
    }
}
