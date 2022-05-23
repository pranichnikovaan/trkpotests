package main.service;

import main.entity.Method;

import java.util.List;

public interface MethodService {
    boolean add(Method customer);

    void delete(long id);

    Method getById(long id);

    boolean checkById(long id);

    List<Method> getAll();
}
