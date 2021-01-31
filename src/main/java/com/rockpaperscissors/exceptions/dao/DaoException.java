package com.rockpaperscissors.exceptions.dao;

/**
 * This class is used as a supertype for all of the different errors
 * that might occur in the dao layer.
 */
public abstract class DaoException extends Exception
{
    public DaoException(String message)
    {
        super(message);
    }
}
