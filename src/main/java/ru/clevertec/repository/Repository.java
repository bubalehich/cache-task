package ru.clevertec.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<I, T> {
    Optional<T> findById(I id);

    List<T> findAll();

    T save(T product);

    void delete(I id);
}
