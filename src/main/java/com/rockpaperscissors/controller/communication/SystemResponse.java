package com.rockpaperscissors.controller.communication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemResponse
{
    @JsonProperty("result")
    private ResponseStatus status;

    @JsonProperty("content")
    private Object content;

    @JsonProperty("error_message")
    private String errorMessage;

    private SystemResponse()
    {

    }

    public static SystemResponse generateSuccessResponse(Object content)
    {
        SystemResponse response = new SystemResponse();
        response.content = content;
        response.status = ResponseStatus.SUCCESS;
        return response;
    }

    public static SystemResponse generateFailureResponse(String errorMessage)
    {
        SystemResponse response = new SystemResponse();
        response.errorMessage = errorMessage;
        response.status = ResponseStatus.FAILURE;
        return response;
    }
}
