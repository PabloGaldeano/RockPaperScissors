package com.rockpaperscissors.dao;

import com.rockpaperscissors.exceptions.dao.RecordAlreadyExistsException;
import com.rockpaperscissors.exceptions.dao.RecordNotFoundException;

import java.util.Collection;
import java.util.Optional;

public interface Dao<K, T>
{
    Optional<T> get(K id);

    Collection<T> getAll();

    void save(T t) throws RecordAlreadyExistsException;

    void update(K id, T t) throws RecordNotFoundException;

    void delete(T t) throws RecordNotFoundException;
}
