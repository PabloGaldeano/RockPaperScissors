package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
