package com.rockpaperscissors.exceptions.dao;

/**
 * This exception is used to inform about the error of a requested Dao record that
 * does not exist.
 */
public class RecordNotFoundException extends DaoException
{
    public RecordNotFoundException(String message)
    {
        super(message);
    }
}
