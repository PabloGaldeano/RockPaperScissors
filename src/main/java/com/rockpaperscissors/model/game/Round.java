package com.rockpaperscissors.model.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * One round of the game is considered to be immutable, that is why this is a record
 * and not a class. the record keyword allows to simplify the code and to create a class
 * with its getters and constructor out of the box. Also, some checks can be done like
 * require not null parameters.
 */
public record Round(@JsonProperty("result") RoundOutcome result,
                    @JsonProperty("player_one_movement") MovementTypes playerOneMovement,
                    @JsonProperty("player_two_movement") MovementTypes playerTwoMovement)
{
    public Round
    {
        // This is where the parameters are checked to avoid nulls
        Objects.requireNonNull(result);
        Objects.requireNonNull(playerOneMovement);
        Objects.requireNonNull(playerTwoMovement);

    }
}
