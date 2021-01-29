package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin("*")
@RequestMapping(value = "/statistics", produces = "application/json")
public class StatisticsController
{
    @Autowired
    private GameService gameService;

    @GetMapping
    public @ResponseBody
    SystemResponse getStatistics()
    {
        return null;
    }
}
