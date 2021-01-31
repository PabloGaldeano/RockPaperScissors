package com.rockpaperscissors.model.game;

/**
 * This is the enumeration representing the different movements
 * accepted in the game rules. Also, it contains a tiny method
 * to check which movement beats which. Since this is a static information
 * it is better to be written in here since the interaction between movements
 * it's also a definition of the movement itself.
 */
public enum MovementTypes
{
    SCISSORS
            {
                @Override
                public boolean beats(MovementTypes other)
                {
                    return other == PAPER;
                }
            },
    ROCK
            {
                @Override
                public boolean beats(MovementTypes other)
                {
                    return other == SCISSORS;
                }
            },
    PAPER
            {
                @Override
                public boolean beats(MovementTypes other)
                {
                    return other == ROCK;
                }
            };

    /**
     * Method implemented by each literal to act as a shorthand to query
     * if one movement beats another one
     *
     * @param other The movement to check
     * @return <code>true</code> if the movement receiving the message beats the one in the parameter, <code>false</code>
     * otherwise
     */
    public abstract boolean beats(MovementTypes other);
}
