package com.rockpaperscissors.model.player;

import com.rockpaperscissors.model.game.MovementTypes;

import java.security.SecureRandom;

/**
 * This class implements the behaviour of a "regular player" in the game,
 * which is to select a random literal in the {@link MovementTypes} enumeration and
 * return it when {@link #performMovement()} is invoked.
 *
 * This class follows the same principles and patterns as {@link FixedMovementPlayer}
 *
 */
final public class RandomMovementPlayer extends Player
{
    /**
     * This is the random generator to be used.
     */
    private final SecureRandom randomNumberGenerator;

    /**
     * This is the total amount of available movements.
     * It is used as a bounds for the random generator and it is
     * static because it will be the same one for every instance
     * of the player. Also, it is saved to save couple of nanoseconds.
     */
    private static final int totalAmountOfMovements = MovementTypes.values().length;

    public RandomMovementPlayer()
    {
        this.randomNumberGenerator = new SecureRandom();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovementTypes performMovement()
    {
        return MovementTypes.values()[this.randomNumberGenerator.nextInt(totalAmountOfMovements)];
    }


}
