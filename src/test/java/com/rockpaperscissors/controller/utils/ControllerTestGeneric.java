package com.rockpaperscissors.controller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockpaperscissors.controller.communication.SystemResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public abstract class ControllerTestGeneric
{
    @Autowired
    protected MockMvc mockMvc;

    private final ObjectMapper mapper;

    public ControllerTestGeneric()
    {
        this.mapper = new ObjectMapper();
    }

    public void executeMockPetitionAnExpectNotFoundError(MockHttpServletRequestBuilder petitionToExecute) throws Exception
    {
        MvcResult petitionResult = this.mockMvc.perform(petitionToExecute).andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        Assertions.assertEquals(404, petitionResult.getResponse().getStatus());
    }


    protected SystemResponse executeMockPetitionAndExpectOK(MockHttpServletRequestBuilder builder) throws Exception
    {
        MvcResult mvcResult = this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        return this.getServiceResponseFromResult(mvcResult);
    }

    protected void writeObjectInRequestContent(MockHttpServletRequestBuilder destinationBuilder, Object toWrite) throws JsonProcessingException
    {
        destinationBuilder.content(mapper.writeValueAsString(toWrite));
    }

    protected SystemResponse getServiceResponseFromResult(MvcResult result) throws Exception
    {
        return mapper.readValue(result.getResponse().getContentAsString(), SystemResponse.class);
    }

    public void testSuccessServiceResponseWithNullContent(SystemResponse response)
    {
        Assertions.assertTrue(response.isExecutedSuccessfully(),"The success flag is false and it shouldn't!");
        Assertions.assertNull(response.getContent(),"The content of the response is not null, and it should be null");
    }



}
