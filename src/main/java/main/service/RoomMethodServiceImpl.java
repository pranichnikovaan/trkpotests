package main.service;

import main.entity.Method;
import main.entity.RoomMethod;
import main.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import main.repository.RoomMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomMethodServiceImpl implements RoomMethodService {
    @Autowired
    private RoomMethodRepository roomMethodRepository;

    @Override
    public void add(RoomMethod roomMethod) {
        roomMethodRepository.save(roomMethod);
    }

    @Override
    public void delete(long id) {
        Optional<RoomMethod> roomMethod = roomMethodRepository.findById(id);
        if (!roomMethod.isPresent()) {
            throw new EntityNotFoundException("RoomMethod not found");
        }
        roomMethodRepository.delete(roomMethod.get());
    }

    @Override
    public List<Method> getMethodsByRoom(long id) {
        return ((List<RoomMethod>) roomMethodRepository.findAll())
                .stream()
                .filter(roomMethod -> roomMethod.getRoom().getId().compareTo(id)==0)
                .map(RoomMethod::getMethod)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMethod> getAllRoomsMethods() {
        return (List<RoomMethod>) roomMethodRepository.findAll();
    }
}
