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
import java.util.Map;

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
    void testPetitionsWithNonExistingGame() throws Exception
    {
        this.buildPetitionsForGame("nothing");
        this.executeMockPetitionAnExpectNotFoundError(this.getGameProgress);
        this.executeMockPetitionAnExpectNotFoundError(this.createNewRound);
        this.executeMockPetitionAnExpectNotFoundError(this.getGameProgress);
        this.executeMockPetitionAnExpectNotFoundError(this.restartGame);
    }

    @Test
    void testCreateNewGameAndGetCreatedGame() throws Exception
    {

        // Creating the game
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);
        Assertions.assertTrue(petitionResponse.getContent() instanceof String, "The returned value in the response should be a string, indicating the gameID");

        String gameID = (String) petitionResponse.getContent();
        this.buildPetitionsForGame(gameID);

        // Retrieving the progress of the just created game
        this.executeMockPetitionAndExpectOK(this.createNewRound);
        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);

        this.checkGameProgressIntegrityFromResponseAndNumberOfRounds(petitionResponse,1);
    }

    @Test
    void testCreateNewRound() throws Exception
    {
        this.createNewGameAndBuildPetitions();

        SystemResponse response = this.executeMockPetitionAndExpectOK(this.createNewRound);

        Assertions.assertTrue(response.isExecutedSuccessfully(), "The petition should succeed");
        Assertions.assertTrue(response.getContent() instanceof Map, "The content should be a map");

        this.checkRoundInformationIntegrity((Map<String, String>) response.getContent());
    }

    @Test
    void testRestartGame() throws Exception
    {

        // Creating the game
        this.createNewGameAndBuildPetitions();

        this.executeMockPetitionAndExpectOK(this.createNewRound);
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);

        this.checkGameProgressIntegrityFromResponseAndNumberOfRounds(petitionResponse, 1);
        petitionResponse = this.executeMockPetitionAndExpectOK(this.restartGame);
        this.checkSuccessSystemResponseWithNullContent(petitionResponse);

        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        this.checkGameProgressIntegrityFromResponseAndNumberOfRounds(petitionResponse, 0);


    }


    private void checkGameProgressIntegrityFromResponseAndNumberOfRounds(SystemResponse response, int numberOfRounds)
    {
        List<?> roundList = this.checkAndRetrieveGameProgressFromResponse(response);

        Assertions.assertEquals(numberOfRounds, roundList.size(),  String.format("There should be %d rounds in the list", numberOfRounds));

        if (numberOfRounds > 0)
        {
            Object roundElement = roundList.get(0);
            Assertions.assertTrue(roundElement instanceof Map, "The element should be a map");
            Map<String,String> roundData = (Map<String, String>) roundElement;
            this.checkRoundInformationIntegrity(roundData);
        }
    }

    private void checkRoundInformationIntegrity(Map<String,String> roundData)
    {
        Assertions.assertEquals(3, roundData.keySet().size(), "There should be only 3 keys in this map");
        Assertions.assertTrue(roundData.containsKey("result"), "There should be a result key indicating the outcome of the round");
        Assertions.assertTrue(roundData.containsKey("player_one_movement"), "There should be a result key indicating the movement of the first player");
        Assertions.assertTrue(roundData.containsKey("player_two_movement"), "There should be a result key indicating the movement of the second player");
    }

    private void createNewGameAndBuildPetitions() throws Exception
    {
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);

        String gameID = petitionResponse.getContent().toString();
        this.buildPetitionsForGame(gameID);
    }

    private List<?> checkAndRetrieveGameProgressFromResponse(SystemResponse response)
    {
        Assertions.assertTrue(response.getContent() instanceof Map, "The content should be a map");
        Map<String, Object> roundInfo = (Map<String, Object>) response.getContent();

        Assertions.assertTrue(roundInfo.get("gameProgress") instanceof List, "Inside the content there should be a key holding the rgame progress");

        return (List<?>) roundInfo.get("gameProgress");
    }


    private void buildPetitionsForGame(String gameID)
    {
        this.restartGame = MockMvcRequestBuilders.put(String.format("/game/%s/restart", gameID));
        this.createNewRound = MockMvcRequestBuilders.patch(String.format("/game/%s/newRound", gameID));
        this.getGameProgress = MockMvcRequestBuilders.get(String.format("/game/%s", gameID));
    }
}