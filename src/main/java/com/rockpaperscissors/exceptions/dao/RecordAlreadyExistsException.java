package com.rockpaperscissors.exceptions.dao;

/**
 * This exception is used to inform about the error of an attempt to
 * insert a record that already exists.
 */
public class RecordAlreadyExistsException extends DaoException
{
    public RecordAlreadyExistsException(String message)
    {
        super(message);
    }
}
