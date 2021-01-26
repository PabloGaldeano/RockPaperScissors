package com.rockpaperscissors.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Dao<K,T>
{
    Optional<T> get(K id);

    Collection<T> getAll();

    void save(T t);

    void update(K id, T t);

    void delete(T t);
}
