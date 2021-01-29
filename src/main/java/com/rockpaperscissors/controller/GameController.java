package com.rockpaperscissors.controller;

import com.rockpaperscissors.controller.communication.SystemResponse;
import com.rockpaperscissors.exceptions.game.GameNotFoundException;
import com.rockpaperscissors.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.Callable;

/**
 * This is the controller in charge of handling all the petitions regarding
 * the games, such as creating a new game, creating one new round and so on.
 * <p>
 * This class follows the single responsibility purpose as well meaning it will
 * only handle the petitions. For the rest of the operations it will invoke
 * the different methods of the {@link GameService}
 * <p>
 * Per standard, all the petitions will return an instance of the record {@link SystemResponse}
 * <p>
 * In the implementation of this controller, <strong>the game is treated as a resource</strong> which means,
 * adding a new round to it will be considered as a partial modification and not as a
 * new resource. And also a {@link ResponseStatusException} with the code of {@link HttpStatus#NOT_FOUND} will
 * be raised in case an access to a non existent game is performed. In case of a unknown error the
 * same exception will be thrown but with the code {@link HttpStatus#I_AM_A_TEAPOT}, each one of these will
 * contain a descriptive message.
 */
@CrossOrigin("*")
@RequestMapping(value = "/game", produces = "application/json")
@RestController
public class GameController
{
    private final GameService gameService;

    public GameController(GameService gameService)
    {
        this.gameService = gameService;
    }

    /**
     * This petition is a post since creating a new game it is not
     * idempotent, meaning it will create a new one every time this
     * call is invoked.
     * <p>
     * The {@link SystemResponse#content()} of the system response returned by
     * this method will contain the ID of the newly created game
     *
     * @return A system response containing the call information
     */
    @PostMapping(value = "/create")
    public @ResponseBody
    SystemResponse createNewGame()
    {
        return this.executeServicePetitionAndHandleExceptions(() ->
                SystemResponse.generateSuccessResponse(this.gameService.StartNewDefaultGame()));
    }

    /**
     * This petition will <strong>modify a game</strong> by adding a newly generated round to it,
     * which this is not idempotent either, that is why patch is used, since patch petitions are not
     * necessarily idempotent.
     * <p>
     * The {@link SystemResponse#content()} of the system response returned by
     * this method will contain the information of the new round.
     *
     * @param gameID the ID of the game to add the round to it
     * @return System response containing the information of the round or an error.
     */
    @PatchMapping("{id}/newRound")
    public @ResponseBody
    SystemResponse generateNewRound(@PathVariable("id") String gameID)
    {
        return this.executeServicePetitionAndHandleExceptions(() ->
                SystemResponse.generateSuccessResponse(this.gameService.generateNewRound(gameID)));
    }

    /**
     * This call is used to retrieve the information about the current progress of the game.
     * Said intel will be saved into the  {@link SystemResponse#content()} of the returned
     * response.
     *
     * @param gameID The ID of the game to query
     * @return The response containing the requested information
     */
    @GetMapping("{id}")
    public @ResponseBody
    SystemResponse getGameProgress(@PathVariable("id") String gameID)
    {
        return this.executeServicePetitionAndHandleExceptions(() ->
                SystemResponse.generateSuccessResponse(this.gameService.getGameByIdentifier(gameID).getProgress()));
    }

    /**
     * This is another call similar to {@link #generateNewRound(String)}, but in this case
     * the call is idempotent since no matter how many times we apply this call, the result
     * will be the same.
     *
     * In this case {@link SystemResponse#content()} will be null since there is no response data
     * to return.
     *
     * @param gameID The id of the game to restart
     * @return A system response notifying of the outcome.
     */
    @PatchMapping("{id}/restart")
    public @ResponseBody
    SystemResponse restart(@PathVariable("id") String gameID)
    {
        return this.executeServicePetitionAndHandleExceptions(() ->
        {
            this.gameService.restartGame(gameID);
            return SystemResponse.generateSuccessResponse(null);
        });
    }


    /**
     * This method will act as a common exception handler since all of the exception risen by the
     * service are handled in the exact same way. The existence of this method will prevent
     * repetitive try/catch blocks in all the methods.
     *
     * In order for this method to develop its function it just needs a {@link Callable} function
     * containing the call to the service.
     *
     * It has some side effects also, but it makes the code much more maintainable.
     *
     * @param func The function to execute.
     * @return The system response generated by the executed function
     */
    private SystemResponse executeServicePetitionAndHandleExceptions(Callable<SystemResponse> func)
    {
        try
        {
            return func.call();
        } catch (GameNotFoundException e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested game does not exists");
        } catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Please try it again later");
        }
    }
}
