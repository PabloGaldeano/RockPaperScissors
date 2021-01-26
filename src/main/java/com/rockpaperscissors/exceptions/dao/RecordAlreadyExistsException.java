package com.rockpaperscissors.exceptions.dao;

public class RecordAlreadyExistsException extends DaoException
{
    public RecordAlreadyExistsException(String message)
    {
        super(message);
    }
}
