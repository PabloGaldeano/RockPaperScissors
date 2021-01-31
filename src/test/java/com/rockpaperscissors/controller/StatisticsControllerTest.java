package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.controller.generic.GameControllerTestGeneric;
import com.rockpaperscissors.controller.utils.StatisticsResponseKeys;
import com.rockpaperscissors.utils.EnumerationCheckingUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

/**
 * This class will test out the result of the request to the
 * {@link StatisticsController} in order to check if the information
 * is being sent properly.
 * <p>
 * Same as the rest of the test, if an exception is thrown the test should fail.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StatisticsControllerTest extends GameControllerTestGeneric
{
    private MockHttpServletRequestBuilder getGameStatistics;

    StatisticsControllerTest()
    {
        super();
        this.getGameStatistics = MockMvcRequestBuilders.get("/statistics");
    }

    /**
     * This method will make use of the mock requests in order to create a game, add new rounds
     * and check if the statistics follows the business logic.
     *
     * @throws Exception Thrown if something goes wrong.
     */
    @Test
    void getStatistics() throws Exception
    {
        // Creating a new game and adding a newround
        this.createNewGameAndBuildPetitions();
        this.executeMockPetitionAndExpectOK(this.createNewRound);

        // Retrieving the game progress and checking its integrity
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameStatistics);
        Map<?, ?> statisticsData = this.checkStatisticsIntegrityFromResponse(petitionResponse);

        // Retrieving the already existing number of rounds in order to build the expected number after the future modifications
        int expectedNumberOfRounds = (int) statisticsData.get(StatisticsResponseKeys.TOTAL_ROUNDS.toString()) + 1;

        // Creating a new game and adding a new round
        this.createNewGameAndBuildPetitions();
        this.executeMockPetitionAndExpectOK(this.createNewRound);

        // Retrieving the statistics
        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameStatistics);
        statisticsData = this.checkStatisticsIntegrityFromResponse(petitionResponse);

        // Asserting the number of rounds is the expected one
        Assertions.assertEquals(expectedNumberOfRounds, statisticsData.get(StatisticsResponseKeys.TOTAL_ROUNDS.toString()), "There should be only " + expectedNumberOfRounds + " round(s)");

        // Restarting the game and making sure the statistics haven't changed.
        this.executeMockPetitionAndExpectOK(this.restartGame);
        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameStatistics);
        statisticsData = this.checkStatisticsIntegrityFromResponse(petitionResponse);

        Assertions.assertEquals(expectedNumberOfRounds, statisticsData.get(StatisticsResponseKeys.TOTAL_ROUNDS.toString()), "There should be only " + expectedNumberOfRounds + " round(s)");

    }


    /**
     * This method will check and return the object representing the statistics. Basically, it will check
     * the content of the system response is a map (a.k.a JSON object) by asserting if the number of keys
     * is the expected ones and also asserting all the keys are inside the deserialized object.
     *
     * @param response Map representing the statistics.
     * @return The map containing the information about the statistics
     */
    private Map<?, ?> checkStatisticsIntegrityFromResponse(SystemResponse response)
    {
        Assertions.assertTrue(response.content() instanceof Map, "The content should be a map");
        Map<?, ?> statisticsData = (Map<?, ?>) response.content();
        EnumerationCheckingUtils.checkIfEnumerationLiteralsAreInMap(statisticsData, StatisticsResponseKeys.values());
        return statisticsData;
    }
}