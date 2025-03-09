package org.pancakelab.repository.interfaces;

import org.pancakelab.model.pancakes.Pancake;

import java.util.List;
import java.util.UUID;

public interface PancakeRepository {
    Pancake findById(UUID id);
    List<Pancake> findAll();
    void save(Pancake pancake);
    void delete(UUID id);
}

