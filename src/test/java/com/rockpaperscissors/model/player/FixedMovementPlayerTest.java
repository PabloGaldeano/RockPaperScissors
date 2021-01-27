package com.rockpaperscissors.model.player;

import com.rockpaperscissors.model.game.MovementTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.fail;

class FixedMovementPlayerTest
{

    /**
     * This test will make use of the JUnit API to make sure the {@link FixedMovementPlayer}
     * always return the same movement regardless the type specified during construction.
     *
     * @param playerFixedMovement The movement to construct the player with
     */
    @ParameterizedTest
    @EnumSource(MovementTypes.class)
    void performMovement(MovementTypes playerFixedMovement)
    {
        FixedMovementPlayer playerToTest = new FixedMovementPlayer(playerFixedMovement);
        int numberOfMovementsToGenerate = 100;
        for (int i = 0; i < numberOfMovementsToGenerate; i++)
        {
            if (playerToTest.performMovement() != playerFixedMovement)
            {
                fail("The movement produced by the player is not the expected one");
            }
        }
    }

    @Test
    void checkConstructionWithNullArgs()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FixedMovementPlayer(null));
    }
}