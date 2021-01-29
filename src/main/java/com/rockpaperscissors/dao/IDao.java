package com.rockpaperscissors.dao;

import com.rockpaperscissors.exceptions.dao.RecordAlreadyExistsException;
import com.rockpaperscissors.exceptions.dao.RecordNotFoundException;

import java.util.Collection;
import java.util.Optional;

/**
 * Since we are operating with some kind of persistence but not using the
 * JPA repositories I will use this interface to implement the basic behaviour
 * of a common DAO just to use it as a Dummy.
 *
 * @param <K> The type of the key
 * @param <T> The type of the entity
 */
public interface IDao<K, T>
{
    Optional<T> get(K id);

    Collection<T> getAll();

    void save(T t) throws RecordAlreadyExistsException;

    void update(K id, T t) throws RecordNotFoundException;

    void delete(T t) throws RecordNotFoundException;
}
