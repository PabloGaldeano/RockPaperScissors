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
    private MockHttpServletRequestBuilder getGameProgress;
    private MockHttpServletRequestBuilder createNewGame;
    private MockHttpServletRequestBuilder restartGame;
    private MockHttpServletRequestBuilder createNewRound;


    public GameControllerTest()
    {
        this.createNewGame = MockMvcRequestBuilders.post("/game/create");

    }

    @Test
    void testGetGameProgressWithNoGameID() throws Exception
    {
        this.buildPetitionsForGame("nothing");
        this.executeMockPetitionAnExpectNotFoundError(this.getGameProgress);
    }

    @Test
    void testCreateNewGameAndGetCreatedGame() throws Exception
    {

        // Creating the game
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);
        Assertions.assertTrue(petitionResponse.getContent() instanceof String, "The returned value in the response should be a string");

        String gameID = (String) petitionResponse.getContent();
        this.buildPetitionsForGame(gameID);

        // Retrieving the progress of the just created game
        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        Assertions.assertTrue(petitionResponse.getContent() instanceof List, "The returned value in the response should be a list");

    }

    @Test
    void testRestartGame() throws Exception
    {

        // Creating the game
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);

        String gameID = petitionResponse.getContent().toString();
        this.buildPetitionsForGame(gameID);

        petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewRound);
        petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewRound);
        Assertions.assertTrue(petitionResponse.getContent() instanceof List, "The returned content should be a list of rounds");

        List<?> gameProgress = (List<?>) petitionResponse.getContent();

        Assertions.assertEquals(1, gameProgress.size(), "The number of rounds should be one");
        petitionResponse = this.executeMockPetitionAndExpectOK(this.restartGame);
        this.testSuccessServiceResponseWithNullContent(petitionResponse);

        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        Assertions.assertTrue(petitionResponse.getContent() instanceof List, "The returned content should be a list of rounds");

        gameProgress = (List<?>) petitionResponse.getContent();
        Assertions.assertEquals(0, gameProgress.size(), "The number of rounds should be one");

    }


    private void buildPetitionsForGame(String gameID)
    {
        this.restartGame = MockMvcRequestBuilders.patch(String.format("/game/%s/restart", gameID));
        this.createNewRound = MockMvcRequestBuilders.patch(String.format("/game/%s/newRound", gameID));
        this.getGameProgress = MockMvcRequestBuilders.get(String.format("/game/%s", gameID));
    }
}