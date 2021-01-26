package com.rockpaperscissors.model.player;

import com.rockpaperscissors.model.game.MovementTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RandomMovementPlayerTest
{

    /**
     * This method will do a simple check to make sure the {@link RandomMovementPlayer#generateMovement()} does not
     * generate the same element over and ove again. In order to make sure the test won't fail due to randomness,
     * it is repeated 10 times and a total of 200 calls are made on each call.
     *
     * This test is parametrized, which means it will be run for all of the literals inside {@link MovementTypes} in order
     * to make sure the test passes for all of them.
     *
     * Basically, the only existing assert inside this test is to ensure that all of generated results are not
     * the same type it is being tested.
     *
     * @param typeToCheck The type to check.
     */
    @ParameterizedTest
    @EnumSource(MovementTypes.class)
    void generateMovement(MovementTypes typeToCheck)
    {
        RandomMovementPlayer playerToTest = new RandomMovementPlayer();

        int amountOfRuns = 10;
        int testingPoolSie;
        int numberOfRocks = 0;
        List<MovementTypes> testPool;

        for(; amountOfRuns>0; amountOfRuns--)
        {
            testingPoolSie = 200;

            testPool = new ArrayList<>(testingPoolSie);

            for (; testingPoolSie >= 0; testingPoolSie--)
            {
                testPool.add(playerToTest.generateMovement());
            }

            numberOfRocks += testPool.stream()
                    .filter(movementTypes -> movementTypes == typeToCheck)
                    .count();
        }

        Assertions.assertNotEquals(numberOfRocks, 2000);
    }
}