package main.service;

import main.entity.Method;
import main.entity.RoomMethod;

import java.util.List;

public interface RoomMethodService {
    void add(RoomMethod roomMethod);

    void delete(long id);

    List<Method> getMethodsByRoom(long id);

    List<RoomMethod> getAllRoomsMethods();
}
