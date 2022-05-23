package main.service;

import main.entity.Method;
import main.entity.Room;
import main.entity.RoomMethod;
import main.repository.RoomMethodRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RoomMethodServiceImpl.class})
class RoomMethodServiceImplTest {

    @MockBean
    private RoomMethodRepository roomMethodRepository;

    @Autowired
    private RoomMethodService roomMethodService;

    @Test
    void shouldAddMethodToRoom() {
        RoomMethod roomMethod = new RoomMethod();
        roomMethodService.add(roomMethod);
        verify(roomMethodRepository, times(1)).save(roomMethod);
    }

    @Test
    void shouldDeleteMethodFromRoom() {
        RoomMethod roomMethod = new RoomMethod();
        roomMethod.setId(1L);
        when(roomMethodRepository.findById(roomMethod.getId()))
                .thenReturn(Optional.of(roomMethod));
        roomMethodService.delete(roomMethod.getId());
        verify(roomMethodRepository, times(1)).delete(roomMethod);
    }

    @Test
    void shouldReturnMethodsFromRoom() {
        Room room = new Room();
        room.setId(1L);
        Method expectedMethod = new Method();
        RoomMethod roomMethod = new RoomMethod();
        roomMethod.setRoom(room);
        roomMethod.setMethod(expectedMethod);
        List<RoomMethod> roomMethodList = Collections.singletonList(roomMethod);

        when(roomMethodRepository.findAll()).thenReturn(roomMethodList);

        List<Method> roomMethods = roomMethodService.getMethodsByRoom(1L);
        assertEquals(roomMethods, Collections.singletonList(expectedMethod));
        verify(roomMethodRepository, times(1)).findAll();
    }

}