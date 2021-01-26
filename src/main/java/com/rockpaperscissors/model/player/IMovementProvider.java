package com.rockpaperscissors.model.player;

import com.rockpaperscissors.model.game.MovementTypes;

public interface IMovementProvider
{
    MovementTypes generateMovement();
}
