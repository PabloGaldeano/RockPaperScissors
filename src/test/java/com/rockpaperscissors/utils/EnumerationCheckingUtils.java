package com.rockpaperscissors.utils;

import org.junit.jupiter.api.Assertions;

import java.util.Map;

/**
 * Since I am using enumerations as a way of checking the integrity, a way is needed to
 * abstract the methods about all the operations performed with enumerations such as
 * asserting if one map is a representation of the enumeration.
 */
public class EnumerationCheckingUtils
{
    /**
     * This method will assert if the map given by parameter has the size of the enumeration literals
     * given by parameter and if it contains all the keys listed in the values parameter.
     *
     * @param mapToCheck The map to perform the checks
     * @param values     The array of values that should be inside the map
     * @param <T>        The type of the enumeration.
     */
    public static <T extends Enum<T>> void checkIfEnumerationLiteralsAreInMap(Map<?, ?> mapToCheck, Enum<T>[] values)
    {

        int numberOfFields = values.length;

        Assertions.assertEquals(numberOfFields, mapToCheck.keySet().size(), String.format("The map should have %d keys", numberOfFields));

        for (Enum<T> keyToCheck : values)
        {
            Assertions.assertTrue(mapToCheck.containsKey(keyToCheck.toString()), String.format("The key: %s should be included", keyToCheck.toString()));

        }

    }
}
