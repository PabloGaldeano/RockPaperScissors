package com.rockpaperscissors.model.player;

import com.rockpaperscissors.model.game.MovementTypes;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

public class RandomMovementPlayer implements IMovementProvider
{
    private SecureRandom randomNumberGenerator;

    private int totalAmountOfMovements;

    public RandomMovementPlayer()
    {
        this.randomNumberGenerator = new SecureRandom();
        this.totalAmountOfMovements = MovementTypes.values().length;
    }

    @Override
    public MovementTypes generateMovement()
    {
        return MovementTypes.values()[this.randomNumberGenerator.nextInt(this.totalAmountOfMovements)];
    }
}
