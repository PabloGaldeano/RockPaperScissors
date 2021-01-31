package com.rockpaperscissors.controller.communication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * In order to preserve the consistency of the system, every controller
 * will return one instance of this class to the clients except
 * when there is an error such as element not found or internal server error.
 * this class follows the immutable software pattern and represents the outcome of one call the services,
 * it contains a status indicating if the call was a success or not, a content containing the data
 * to send to the client and a error message.
 * <p>
 * The content will be null in case of an error since no data will be returned and the error message
 * will be null in case of a success call.
 * </p>
 * <p>
 * There are also some helper methods to help constructing the response based on the outcome,
 * these are {@link #generateSuccessResponse(Object)} and {@link #generateFailureResponse(String)}
 * </p>
 */
public record SystemResponse(
        @JsonProperty("status") ResponseStatus status,
        @JsonProperty("content") Object content,
        @JsonProperty("error_message") String errorMessage)
{


    public static SystemResponse generateSuccessResponse(Object content)
    {
        return new SystemResponse(ResponseStatus.SUCCESS, content, null);
    }

    public static SystemResponse generateFailureResponse(String errorMessage)
    {
        return new SystemResponse(ResponseStatus.FAILURE, null, errorMessage);
    }

    @JsonIgnore
    public boolean isExecutedSuccessfully()
    {
        return this.status() == ResponseStatus.SUCCESS;
    }
}
