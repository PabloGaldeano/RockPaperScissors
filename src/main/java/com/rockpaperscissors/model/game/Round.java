package com.rockpaperscissors.model.game;

import java.util.Objects;

public record Round(RoundOutcome result, MovementTypes playerOneMovement, MovementTypes playerTwoMovement)
{
    public Round
    {
        Objects.requireNonNull(result);
        Objects.requireNonNull(playerOneMovement);
        Objects.requireNonNull(playerTwoMovement);

    }
}
