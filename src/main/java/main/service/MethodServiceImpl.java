package main.service;

import main.entity.Method;
import main.entity.RoomMethod;
import main.exception.EntityNotFoundException;
import main.repository.MethodRepository;
import main.repository.RoomMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MethodServiceImpl implements MethodService {
    @Autowired
    private MethodRepository MethodRepository;

    @Autowired
    private RoomMethodRepository roomMethodRepository;

    @Override
    public boolean add(Method Method) {
        Optional<Method> MethodFromDB = MethodRepository.findById(Method.getId());
        if (MethodFromDB.isPresent()) {
            return false;
        }
        MethodRepository.save(Method);
        return true;
    }

    @Override
    public void delete(long id) {
        Optional<Method> Method = MethodRepository.findById(id);
        if (!Method.isPresent()) {
            throw new EntityNotFoundException("Method not found");
        }

        ((List<RoomMethod>) roomMethodRepository.findAll())
          .stream()
          .filter(roomMethod -> roomMethod.getMethod().getId().compareTo(id) == 0)
          .forEach(roomMethod -> roomMethodRepository.delete(roomMethod));

        MethodRepository.delete(Method.get());
    }

    @Override
    public Method getById(long id) {
        Optional<Method> Method = MethodRepository.findById(id);
        if (!Method.isPresent()) {
            throw new EntityNotFoundException("Method not found");
        }
        return Method.get();
    }

    @Override
    public boolean checkById(long id) {
        return MethodRepository.findById(id).isPresent();
    }

    @Override
    public List<Method> getAll() {
        return (List<Method>) MethodRepository.findAll();
    }

}
