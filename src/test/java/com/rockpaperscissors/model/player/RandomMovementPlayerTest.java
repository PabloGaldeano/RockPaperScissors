package com.rockpaperscissors.model.player;

import com.rockpaperscissors.model.game.MovementTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class RandomMovementPlayerTest
{

    /**
     * This method will do a simple check to make sure the {@link RandomMovementPlayer#performMovement()} ()} does not
     * generate the same element over and ove again. In order to make sure the test won't fail due to randomness,
     * it is repeated 10 times and a total of 200 calls are made on each call.
     * <p>
     * This test is parametrized, which means it will be run for all of the parameters returned by {@link #provideParameters()}
     * <p>
     * Basically, the only existing assert inside this test is to ensure that all of generated results are not
     * the same type it is being tested.
     *
     * @param typeToCheck The type to check.
     */
    @ParameterizedTest
    @MethodSource("provideParameters")
    void generateMovement(MovementTypes typeToCheck, long numberOfIterations, int numberOfMovementsToGenerate)
    {
        RandomMovementPlayer playerToTest = new RandomMovementPlayer();

        List<MovementTypes> testPool = new ArrayList<>(numberOfMovementsToGenerate);

        long accumulatedMatches;

        for (int i = 0; i < numberOfIterations; i++)
        {
            testPool.clear();

            for (int j = 0; j < numberOfMovementsToGenerate; j++)
            {
                testPool.add(playerToTest.performMovement());
            }

            accumulatedMatches = testPool.stream()
                    .filter(typeToCheck::equals)
                    .count();

            long ratio = numberOfMovementsToGenerate/accumulatedMatches;
            Assertions.assertTrue(ratio >= 2, "The ratio should be 2 or more");

        }

    }

    /**
     * This method will provide the different parameters for the test {@link #generateMovement(MovementTypes, long, int)}
     *
     * @return A stream of different set of arguments to supply to the test
     */
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(MovementTypes.ROCK, 1000,10000),
                Arguments.of(MovementTypes.ROCK, 500,5000),
                Arguments.of(MovementTypes.ROCK, 750,7500),
                Arguments.of(MovementTypes.PAPER, 1000,10000),
                Arguments.of(MovementTypes.PAPER, 750,7500),
                Arguments.of(MovementTypes.PAPER, 500,5000),
                Arguments.of(MovementTypes.SCISSORS, 1000,10000),
                Arguments.of(MovementTypes.SCISSORS, 750,7500),
                Arguments.of(MovementTypes.SCISSORS, 500,5000)
        );
    }
}