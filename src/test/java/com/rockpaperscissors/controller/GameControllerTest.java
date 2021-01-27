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
        Assertions.assertTrue(petitionResponse.getContent() instanceof Map, "The returned value in the response should be a list");

        Map<String,Object> petitionContent = (Map<String, Object>) petitionResponse.getContent();

        Assertions.assertTrue(petitionContent.containsKey("gameProgress"),"Inside the content there should be a key where the progress in hanging from");

        List<?>  gameProgress = (List<?>) petitionContent.get("gameProgress");

        Assertions.assertEquals(1,gameProgress.size(), "There should be one round here");

        Map<String,String> roundInfo = (Map<String, String>) gameProgress.get(0);
        Assertions.assertEquals(3, roundInfo.keySet().size(), "There should be only 3 keys in this map");
        Assertions.assertTrue(roundInfo.containsKey("result"), "There should be a result key indicating the outcome of the round");
        Assertions.assertTrue(roundInfo.containsKey("player_one_movement"), "There should be a result key indicating the movement of the first player");
        Assertions.assertTrue(roundInfo.containsKey("player_two_movement"), "There should be a result key indicating the movement of the second player");
    }

    @Test
    void testCreateNewRound() throws Exception
    {
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);

        String gameID = petitionResponse.getContent().toString();
        this.buildPetitionsForGame(gameID);

        SystemResponse response = this.executeMockPetitionAndExpectOK(this.createNewRound);

        Assertions.assertTrue(response.isExecutedSuccessfully(),"The petition should succeed");
        Assertions.assertTrue(response.getContent() instanceof Map, "The content should be a map");

        Map<String,Object> roundInfo = (Map<String, Object>) response.getContent();


        Assertions.assertEquals(3, roundInfo.keySet().size(), "There should be only 3 keys in this map");
        Assertions.assertTrue(roundInfo.containsKey("result"), "There should be a result key indicating the outcome of the round");
        Assertions.assertTrue(roundInfo.containsKey("player_one_movement"), "There should be a result key indicating the movement of the first player");
        Assertions.assertTrue(roundInfo.containsKey("player_two_movement"), "There should be a result key indicating the movement of the second player");
    }

    @Test
    void testRestartGame() throws Exception
    {

        // Creating the game
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);

        String gameID = petitionResponse.getContent().toString();
        this.buildPetitionsForGame(gameID);

        this.executeMockPetitionAndExpectOK(this.createNewRound);
        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        Assertions.assertTrue(petitionResponse.getContent() instanceof Map, "The returned content should be a list of rounds");

        List<?> gameProgress = (List<?>) ((Map<?, ?>) petitionResponse.getContent()).get("gameProgress");

        Assertions.assertEquals(1, gameProgress.size(), "The number of rounds should be one");
        petitionResponse = this.executeMockPetitionAndExpectOK(this.restartGame);
        this.testSuccessServiceResponseWithNullContent(petitionResponse);

        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        Assertions.assertTrue(petitionResponse.getContent() instanceof Map, "The returned content should be a list of rounds");

        gameProgress = (List<?>) ((Map<?, ?>) petitionResponse.getContent()).get("gameProgress");
        Assertions.assertEquals(0, gameProgress.size(), "The number of rounds should be one");

    }



    private void buildPetitionsForGame(String gameID)
    {
        this.restartGame = MockMvcRequestBuilders.patch(String.format("/game/%s/restart", gameID));
        this.createNewRound = MockMvcRequestBuilders.patch(String.format("/game/%s/newRound", gameID));
        this.getGameProgress = MockMvcRequestBuilders.get(String.format("/game/%s", gameID));
    }
}