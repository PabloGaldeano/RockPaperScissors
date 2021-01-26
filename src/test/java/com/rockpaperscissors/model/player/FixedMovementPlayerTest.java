package com.rockpaperscissors.model.player;

import com.rockpaperscissors.model.game.MovementTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FixedMovementPlayerTest
{

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