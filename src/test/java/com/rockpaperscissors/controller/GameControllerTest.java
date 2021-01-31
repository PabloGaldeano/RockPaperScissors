package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.controller.generic.GameControllerTestGeneric;
import com.rockpaperscissors.controller.utils.GameResponseContentKeys;
import com.rockpaperscissors.utils.EnumerationCheckingUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

/**
 * This class will test the different methods of the {@link GameController}, in order
 * to make the task easier, it will inherit form {@link GameControllerTestGeneric} to make use
 * of its methods.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest extends GameControllerTestGeneric
{

    //<editor-fold desc="Tests">

    /**
     * Method used to test out the 404 error that should be thrown when accessing a non
     * existing resource (game)
     *
     * @throws Exception Thrown when something unexpected happens.
     */
    @Test
    void testPetitionsWithNonExistingGame() throws Exception
    {
        this.buildPetitionsForGame("nothing");
        this.executeMockPetitionAnExpectNotFoundError(this.getGameProgress);
        this.executeMockPetitionAnExpectNotFoundError(this.createNewRound);
        this.executeMockPetitionAnExpectNotFoundError(this.getGameProgress);
        this.executeMockPetitionAnExpectNotFoundError(this.restartGame);
    }

    /**
     * Tis method will perform the create new game and get game progress in order
     * to make sure the game is successfully created and the progress is returned in
     * the appropriate format.
     *
     * @throws Exception Thrown when something unexpected happens.
     */
    @Test
    void testCreateNewGameAndGetCreatedGame() throws Exception
    {

        // Creating the game
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);
        Assertions.assertTrue(petitionResponse.content() instanceof String, "The returned value in the response should be a string, indicating the gameID");

        // Retrieving the Game ID and building the petitions for it
        String gameID = (String) petitionResponse.content();
        this.buildPetitionsForGame(gameID);

        // Retrieving the game progress and checking its integrity
        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameProgress);
        this.assertGameProgressIntegrityAndNumberOfRoundsFromResponse(petitionResponse, 0);
    }

    /**
     * Method used to test if the create new round call returns the data in the way it is meant to by checking
     * if within the content of the response lies all the information of the round.
     *
     * @throws Exception Thrown when something unexpected happens.
     */
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
        EnumerationCheckingUtils.checkIfEnumerationLiteralsAreInMap((Map<?, ?>) response.content(), GameResponseContentKeys.values());
    }

    /**
     * This method will create a game, add a new round to it and then restart it. All this process is done
     * through different requests and also, in between each step the integrity of the data is
     * checked in order to make sure the information will arrive to the clients the way it is meant to.
     *
     * @throws Exception Thrown when something unexpected happened.
     */
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
     * This method will invoke {@link EnumerationCheckingUtils#checkIfEnumerationLiteralsAreInMap(Map, Enum[])} in order to check
     * the information of every round and also it will check if the number of rounds within
     * the game progress are equal to the amount given by parameter. It is worth mentioning
     * the fact of checking the integrity of the game progress includes the checking of
     * the integrity of each round within.
     * <p>
     * The information to check will be extracted from the system response
     *
     * @param response       The response to extract the data from
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
            Map<?, ?> roundData = (Map<?, ?>) roundElement;

            // Invoking the method to check the round integrity
            EnumerationCheckingUtils.checkIfEnumerationLiteralsAreInMap(roundData, GameResponseContentKeys.values());
        }
    }

    /**
     * This method will retrieve the content of the response and it will check its integrity.
     * This process consist on first checking if the content is a {@link Map} then, checking
     * if the key 'gameProgress' and the value of that key is a {@link List}.
     *
     * @param response The response to retrieve the data
     * @return The list representing the game progress
     */
    private List<?> checkAndRetrieveGameProgressFromResponse(SystemResponse response)
    {
        // Checking of the value under the game progress key is a list
        Assertions.assertTrue(response.content() instanceof List, "Inside the content there should be a key holding the game progress");

        // Returning the game progress as a list
        return (List<?>) response.content();
    }
    //</editor-fold>

}