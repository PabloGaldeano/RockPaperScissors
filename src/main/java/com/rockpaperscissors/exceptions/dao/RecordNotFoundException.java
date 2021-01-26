package com.rockpaperscissors.exceptions.dao;

public class RecordNotFoundException extends DaoException
{
    public RecordNotFoundException(String message)
    {
        super(message);
    }
}
