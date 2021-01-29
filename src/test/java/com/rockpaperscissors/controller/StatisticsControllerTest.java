package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.controller.generic.ControllerTestGeneric;
import com.rockpaperscissors.controller.utils.StatisticsResponseKeys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StatisticsControllerTest extends ControllerTestGeneric
{

    private final MockHttpServletRequestBuilder createNewGame;
    private MockHttpServletRequestBuilder createNewRound;
    private MockHttpServletRequestBuilder getGameStatistics;
    private MockHttpServletRequestBuilder restartGame;


    StatisticsControllerTest()
    {
        this.createNewGame = MockMvcRequestBuilders.post("/game/create");
        this.getGameStatistics = MockMvcRequestBuilders.get("/statistics");
    }

    @Test
    void getStatistics() throws Exception
    {
        this.createNewGameAndBuildPetitions();
        this.executeMockPetitionAndExpectOK(this.createNewRound);

        // Retrieving the game progress and checking its integrity
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameStatistics);
        Map<String,String> statisticsData = this.checkStatisticsIntegrityFromResponse(petitionResponse);


        Assertions.assertEquals(1, statisticsData.get(StatisticsResponseKeys.TOTAL_ROUNDS.getKeyName()), "There should be only one round");

        this.createNewGameAndBuildPetitions();
        this.executeMockPetitionAndExpectOK(this.createNewRound);

        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameStatistics);
        statisticsData = this.checkStatisticsIntegrityFromResponse(petitionResponse);

        Assertions.assertEquals(2, statisticsData.get(StatisticsResponseKeys.TOTAL_ROUNDS.getKeyName()), "There should be only one round");

        this.executeMockPetitionAndExpectOK(this.restartGame);

        petitionResponse = this.executeMockPetitionAndExpectOK(this.getGameStatistics);
        statisticsData = this.checkStatisticsIntegrityFromResponse(petitionResponse);

        Assertions.assertEquals(2, statisticsData.get(StatisticsResponseKeys.TOTAL_ROUNDS.getKeyName()), "There should be only one round");

    }

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
        this.createNewRound = MockMvcRequestBuilders.patch(String.format("/game/%s/newRound", gameID));
        this.restartGame = MockMvcRequestBuilders.patch(String.format("/game/%s/restart", gameID));

    }

    private Map<String,String> checkStatisticsIntegrityFromResponse(SystemResponse response)
    {

        Assertions.assertTrue(response.content() instanceof Map, "The content should be a map");

        Map<String,String> statisticsData = (Map<String, String>) response.content();


        Assertions.assertEquals(4,statisticsData.keySet().size(), "The statistics map should contain 4 keys");

        Assertions.assertTrue(statisticsData.containsKey(StatisticsResponseKeys.TOTAL_DRAWS.getKeyName()), "The total draws should be included");
        Assertions.assertTrue(statisticsData.containsKey(StatisticsResponseKeys.TOTAL_PLAYER_ONE_WINS.getKeyName()), "The total wins of player one should be included");
        Assertions.assertTrue(statisticsData.containsKey(StatisticsResponseKeys.TOTAL_PLAYER_TWO_WINS.getKeyName()), "The total wins of player two should be included");
        Assertions.assertTrue(statisticsData.containsKey(StatisticsResponseKeys.TOTAL_ROUNDS.getKeyName()), "The total rounds should be included");

        return statisticsData;
    }
}