package com.rockpaperscissors.model.player;

import com.rockpaperscissors.model.game.MovementTypes;

/**
 * This class implements the kind of player that will always select
 * the same {@link MovementTypes} for every round. Said movement will be
 * specified during construction time.
 * <p>
 * Also, and until specified otherwise, this class also follows
 * the immutable software pattern. Since there is no requirement
 * saying this class can change its movement at run-time nor it has subtypes.
 */
final public class FixedMovementPlayer implements IPlayer
{

    private final MovementTypes fixedMovement;

    /**
     * This is the only constructor of the class where the movement to
     * be returned by this player is specified, it will be returned
     * every time {@link #performMovement()} is invoked
     *
     * @param movementToChoose The movement to set to the player, can not be null.
     * @throws IllegalArgumentException Thrown when the given movement is null
     */
    public FixedMovementPlayer(MovementTypes movementToChoose)
    {
        if (movementToChoose == null)
        {
            throw new IllegalArgumentException("The movement for FixedMovementPlayer can not be null");
        }

        this.fixedMovement = movementToChoose;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MovementTypes performMovement()
    {
        return this.fixedMovement;
    }
}
