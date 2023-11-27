package ru.clevertec.service;

import java.util.List;
import java.util.UUID;

public interface Service<T> {
    T get(UUID uuid);

    List<T> getAll();

    UUID create(T type);

    void update(UUID uuid, T type);

    void delete(UUID uuid);
}
