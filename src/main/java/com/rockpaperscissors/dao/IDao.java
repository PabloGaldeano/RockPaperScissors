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
    /**
     * This method is used to retrieve an element from the persistance layer,
     * an {@link Optional} is returned since the requested record might or might  not
     * exist.
     *
     * @param id The id of the record to look for
     * @return An {@link Optional} container with the record in case it exist. Empty container otherwise
     */
    Optional<T> get(K id);

    /**
     * This method is used to get all the elements from the persistence layer
     *
     * @return A collection with all of the elements.
     */
    Collection<T> getAll();

    /**
     * Method used to save a record, in case the record already exist, an exception will be thrown
     *
     * @param entity The entity to insert
     * @throws RecordAlreadyExistsException thrown when the record has been already inserted
     */
    void save(T entity) throws RecordAlreadyExistsException;

    /**
     * Method to update an already existing record.
     *
     * @param id    the ID of the record.
     * @param value An entity containing the new information for the record.
     * @throws RecordNotFoundException thrown when the record to update could not be found in the DB
     */
    void update(K id, T value) throws RecordNotFoundException;

    /**
     * Method to delete a record, an exception will be thrown if the record can not be found.
     *
     * @param id the ID of the record to delete
     * @throws RecordNotFoundException thrown when the selected record could not be found
     */
    void delete(K id) throws RecordNotFoundException;
}
