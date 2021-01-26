package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.controller.utils.ControllerTestGeneric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest extends ControllerTestGeneric
{
    protected MockHttpServletRequestBuilder getGameProgress;
    protected MockHttpServletRequestBuilder createNewGame;

    public GameControllerTest()
    {
        this.getGameProgress = MockMvcRequestBuilders.get("/games/");
        this.getGameProgress = MockMvcRequestBuilders.post("/games/create");
    }

    @Test
    void testGetGameProgressWithNoGameID() throws Exception
    {
       this.executeMockPetitionAnExpectNotFoundError(this.getGameProgress);
    }

    @Test
    void testCreateNewGameAndGetCreatedGame() throws Exception
    {

        // Creating the game
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);
        Assertions.assertTrue(petitionResponse.getContent() instanceof  String, "The returned value in the response should be a string");

        // Retrieving the progress of the just created game
        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        Assertions.assertTrue(petitionResponse.getContent() instanceof List, "The returned value in the response should be a list");

    }

    @Test
    void testRestartGame()
    {

    }




}