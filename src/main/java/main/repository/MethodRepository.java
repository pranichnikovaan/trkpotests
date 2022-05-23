package main.repository;

import main.entity.Method;
import org.springframework.data.repository.CrudRepository;

public interface MethodRepository extends CrudRepository<Method, Long> {
}
