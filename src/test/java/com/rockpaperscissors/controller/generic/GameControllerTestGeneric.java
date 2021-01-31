package com.rockpaperscissors.controller.generic;

import com.rockpaperscissors.controller.communication.SystemResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * This class wraps all the calls to operate with the games since the test
 * classes they would use them in order to perform the test.
 */
public abstract class GameControllerTestGeneric extends ControllerTestGeneric
{
    /**
     * Mock request to invoke the creation of a new game against the controller
     */
    protected final MockHttpServletRequestBuilder createNewGame;

    /**
     * Mock request to retrieve the game progress against the controller
     */
    protected MockHttpServletRequestBuilder getGameProgress;

    /**
     * Mock request to create a new round
     */
    protected MockHttpServletRequestBuilder createNewRound;

    /**
     * Mock petition used to restart a game
     */
    protected MockHttpServletRequestBuilder restartGame;

    public GameControllerTestGeneric()
    {
        this.createNewGame = MockMvcRequestBuilders.post("/game/create");
    }

    /**
     * This method will parametrize all the mock petitions in order to point to the
     * game resource identified by the supplied id
     *
     * @param gameID The ID of the game
     */
    protected void buildPetitionsForGame(String gameID)
    {
        this.createNewRound = MockMvcRequestBuilders.patch(String.format("/game/%s/newRound", gameID));
        this.getGameProgress = MockMvcRequestBuilders.get(String.format("/game/%s", gameID));
        this.restartGame = MockMvcRequestBuilders.patch(String.format("/game/%s/restart", gameID));
    }

    /**
     * It is a common operation to create the game and then build the petitions for it in order to execute
     * subsequent operations, it is not included in the setUp since not all of the calls need a game
     * created.
     *
     * @throws Exception In case something goes wrong
     */
    protected void createNewGameAndBuildPetitions() throws Exception
    {
        SystemResponse petitionResponse = this.executeMockPetitionAndExpectOK(this.createNewGame);

        String gameID = petitionResponse.content().toString();
        this.buildPetitionsForGame(gameID);
    }

}
