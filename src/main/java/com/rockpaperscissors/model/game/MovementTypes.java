package com.rockpaperscissors.model.game;

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

    public abstract boolean beats(MovementTypes other);
}
