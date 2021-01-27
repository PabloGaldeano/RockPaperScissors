package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.controller.generic.ControllerTestGeneric;
import static  com.rockpaperscissors.controller.utils.GameResponseContentKeys.*;
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

/**
 * This class will test the different methods of the {@link GameController}, in order
 * to make the task easier, it will inherit form {@link ControllerTestGeneric} to make use
 * of its methods.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest extends ControllerTestGeneric
{
    private final MockHttpServletRequestBuilder createNewGame;
    private MockHttpServletRequestBuilder getGameProgress;
    private MockHttpServletRequestBuilder restartGame;
    private MockHttpServletRequestBuilder createNewRound;


    public GameControllerTest()
    {
        this.createNewGame = MockMvcRequestBuilders.post("/game/create");
    }

    //<editor-fold desc="Tests">
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
        Assertions.assertTrue(petitionResponse.content() instanceof String, "The returned value in the response should be a string, indicating the gameID");

        // Retrieving the Game ID and building the petitions for it
        String gameID = (String) petitionResponse.content();
        this.buildPetitionsForGame(gameID);

        // Generate a new round for the created game
        this.executeMockPetitionAndExpectOK(this.createNewRound);

        // Retrieving the game progress and checking its integrity
        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        this.assertGameProgressIntegrityAndNumberOfRoundsFromResponse(petitionResponse, 1);
    }

    @Test
    void testCreateNewRound() throws Exception
    {
        this.createNewGameAndBuildPetitions();

        // Creating a new round
        SystemResponse response = this.executeMockPetitionAndExpectOK(this.createNewRound);

        // Checking if the response comes adequately
        Assertions.assertTrue(response.isExecutedSuccessfully(), "The petition should succeed");
        Assertions.assertTrue(response.content() instanceof Map, "The content should be a map");

        // Checking the round integrity
        this.assertRoundInformationIntegrity((Map<String, String>) response.content());
    }

    @Test
    void testRestartGame() throws Exception
    {

        // Creating the game
        this.createNewGameAndBuildPetitions();

        // Creating a new round and retrieving the game progress
        this.executeMockPetitionAndExpectOK(this.createNewRound);
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        this.assertGameProgressIntegrityAndNumberOfRoundsFromResponse(petitionResponse, 1);

        // Restarting the game and checking the response of the call
        petitionResponse = this.executeMockPetitionAndExpectOK(this.restartGame);
        this.checkSuccessSystemResponseWithNullContent(petitionResponse);

        // Retrieving the game progress and checking the game progress has 0 rounds
        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        this.assertGameProgressIntegrityAndNumberOfRoundsFromResponse(petitionResponse, 0);


    }
    //</editor-fold>

    //<editor-fold desc="Checker methods">

    /**
     * This method will invoke {@link #assertRoundInformationIntegrity(Map)} in order to check
     * the information of every round and also it will check if the number of rounds within
     * the game progress are equal to the amount given by parameter. It is worth mentioning
     * the fact of checking the integrity of the game progress includes the checking of
     * the integrity of each round within.
     *
     * The information to check will be extracted from the system response
     *
     * @param response The response to extract the data from
     * @param numberOfRounds The number of rounds the progress should have
     */
    private void assertGameProgressIntegrityAndNumberOfRoundsFromResponse(SystemResponse response, int numberOfRounds)
    {
        List<?> roundList = this.checkAndRetrieveGameProgressFromResponse(response);

        // Checking if the number of rounds is equal to the one give by parameter.
        Assertions.assertEquals(numberOfRounds, roundList.size(), String.format("There should be %d rounds in the list", numberOfRounds));

        // If there should be at least one round, the round integrity is checked
        if (numberOfRounds > 0)
        {
            // Getting the first element
            Object roundElement = roundList.get(0);

            // Checking said element is a map
            Assertions.assertTrue(roundElement instanceof Map, "The element should be a map");
            Map<String, String> roundData = (Map<String, String>) roundElement;

            // Invoking the method to check the round integrity
            this.assertRoundInformationIntegrity(roundData);
        }
    }

    /**
     * This method will check if the round information follows the standard, which means:
     * - There should be only 3 keys within the map
     * - Said keys should be, result, player_one_movement and, player_two_movement
     *
     * @param roundData The round data to check
     */
    private void assertRoundInformationIntegrity(Map<String, String> roundData)
    {
        Assertions.assertEquals(3, roundData.keySet().size(), "There should be only 3 keys in this map");
        Assertions.assertTrue(roundData.containsKey(ROUND_RESULT.getKeyName()), "There should be a result key indicating the outcome of the round");
        Assertions.assertTrue(roundData.containsKey(ROUND_PLAYER_ONE_MOVEMENT.getKeyName()), "There should be a result key indicating the movement of the first player");
        Assertions.assertTrue(roundData.containsKey(ROUND_PLAYER_TWO_MOVEMENT.getKeyName()), "There should be a result key indicating the movement of the second player");
    }

    /**
     * This method will retrieve the content of the response and it will check its integrity.
     * This process consist on first checking if the content is a {@link Map} then, checking
     * if the key 'gameProgress' and the value of that key is a {@link List}.
     * @param response The response to retrieve the data
     * @return The list representing the game progress
     */
    private List<?> checkAndRetrieveGameProgressFromResponse(SystemResponse response)
    {
        // Checking if the content is a map
        Assertions.assertTrue(response.content() instanceof Map, "The content should be a map");
        Map<String, Object> responseContent = (Map<String, Object>) response.content();

        // Checking if the response content contains the game progress key
        Assertions.assertTrue(responseContent.containsKey(GAME_PROGRESS.getKeyName()));

        // Checking of the value under the game progress key is a list
        Assertions.assertTrue(responseContent.get(GAME_PROGRESS.getKeyName()) instanceof List, "Inside the content there should be a key holding the game progress");

        // Returning the game progress as a list
        return (List<?>) responseContent.get(GAME_PROGRESS.getKeyName());
    }
    //</editor-fold>

    //<editor-fold desc="Helper methods">

    /**
     * It is a common operation to create the game and then build the petitions for it in order to execute
     * subsequent operations, it is not included in the setUp since not all of the calls need a game
     * created.
     *
     * @throws Exception In case something goes wrong
     */
    private void createNewGameAndBuildPetitions() throws Exception
    {
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);

        String gameID = petitionResponse.content().toString();
        this.buildPetitionsForGame(gameID);
    }

    /**
     * This method will parametrize all the mock petitions in order to point to the
     * game resource identified by the supplied id
     *
     * @param gameID The ID of the game
     */
    private void buildPetitionsForGame(String gameID)
    {
        this.restartGame = MockMvcRequestBuilders.patch(String.format("/game/%s/restart", gameID));
        this.createNewRound = MockMvcRequestBuilders.patch(String.format("/game/%s/newRound", gameID));
        this.getGameProgress = MockMvcRequestBuilders.get(String.format("/game/%s", gameID));
    }
    //</editor-fold>
}