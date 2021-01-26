package com.rockpaperscissors.model.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record Round(@JsonProperty("result") RoundOutcome result,
                    @JsonProperty("player_one_movement") MovementTypes playerOneMovement,
                    @JsonProperty("player_two_movement") MovementTypes playerTwoMovement)
{
    public Round
    {
        Objects.requireNonNull(result);
        Objects.requireNonNull(playerOneMovement);
        Objects.requireNonNull(playerTwoMovement);

    }
}
