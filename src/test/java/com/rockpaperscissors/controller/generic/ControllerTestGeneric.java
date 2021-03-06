package com.rockpaperscissors.controller.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockpaperscissors.controller.communication.SystemResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * This class attempts to have a repository of functions
 * to deal with the different mock petitions that the
 * different test of the different controllers might
 * use. It also abstract the usage of the {@link MockMvc}
 * class through different methods.
 * <p>
 * Also, methods like {@link #executeMockPetitionAndExpectOK(MockHttpServletRequestBuilder)}
 * are shorthands to execute a mock petition, analyze the result and then return
 * the system response to the implementing class.
 * <p>
 * Also, this methods do not expect any exceptions, hence the throws in the
 * method signature. This is because if an exception rises, the test is considered
 * a failure.
 */
public abstract class ControllerTestGeneric
{
    /**
     * Jackson deserializer to parse the JSON returned by the mock petition
     */
    private final ObjectMapper mapper;
    @Autowired
    protected MockMvc mockMvc;

    public ControllerTestGeneric()
    {
        this.mapper = new ObjectMapper();
    }

    /**
     * This method will execute the given petition and will check if the returned response
     * suits the criteria which in this case is a 404 error.
     *
     * @param petitionToExecute The petition to execute
     * @throws Exception When literally something goes wrong.
     */
    public void executeMockPetitionAnExpectNotFoundError(MockHttpServletRequestBuilder petitionToExecute) throws Exception
    {
        MvcResult petitionResult = this.mockMvc.perform(petitionToExecute)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), petitionResult.getResponse().getStatus());
    }


    /**
     * Similar to {@link #executeMockPetitionAnExpectNotFoundError(MockHttpServletRequestBuilder)} but
     * in this case, a HTTP status 200 is expected.
     *
     * @param builder The petition to execute and check
     * @return The system response generated by the mock controller
     * @throws Exception When something goes wrong
     */
    protected SystemResponse executeMockPetitionAndExpectOK(MockHttpServletRequestBuilder builder) throws Exception
    {
        MvcResult mvcResult = this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        return mapper.readValue(mvcResult.getResponse().getContentAsString(), SystemResponse.class);
    }


    /**
     * There are some cases where a success petition is sent without any content.
     * This methods check the response in that case and makes sure everything is in order
     *
     * @param response the response to check.
     */
    public void checkSuccessSystemResponseWithNullContent(SystemResponse response)
    {
        Assertions.assertTrue(response.isExecutedSuccessfully(), "The success flag is false and it shouldn't be!");
        Assertions.assertNull(response.content(), "The content of the response is not null, and it should be null");
    }


}
