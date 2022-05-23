package main.web;

import main.entity.Method;
import main.entity.Room;
import main.entity.RoomMethod;
import main.exception.EntityNotFoundException;
import main.model.MethodModel;
import main.model.RoomMethodModel;
import main.model.RoomModel;
import org.hibernate.persister.walking.spi.WalkingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import main.service.MethodService;
import main.service.RoomMethodService;
import main.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {
    private MethodService MethodService;

    private RoomService roomService;

    private RoomMethodService roomMethodService;

    @PostMapping(value = "/Method", consumes = "application/json", produces = "application/json")
    public void addMethod(@RequestBody MethodModel Method) {
        try {
            MethodService.add(new Method(Method.getName(), Method.getDescription()));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Method already exist");
        }
    }

    @PostMapping(value = "/room", consumes = "application/json", produces = "application/json")
    public void addRoom(@RequestBody RoomModel room) {
        try {
            roomService.add(new Room(room.getName(), room.getDescription(), room.getPrice()));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Method already exist");
        }
    }

    @PostMapping(value = "/room_Method", consumes = "application/json", produces = "application/json")
    public void addRoomMethod(@RequestBody RoomMethodModel roomMethod) {
        try {
            Room room = roomService.getById(roomMethod.getRoomId());
            Method Method = MethodService.getById(roomMethod.getMethodId());
            roomMethodService.add(new RoomMethod(room, Method));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Method in room already exist");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/room/{id}")
    public void deleteRoom(@PathVariable("id") long id) {
        try {
            roomService.delete(id);
        } catch (WalkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
    }

    @DeleteMapping("/Method/{id}")
    public void deleteMethod(@PathVariable("id") long id) {
        try {
            MethodService.delete(id);
        } catch (WalkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Method not found");
        }
    }

    @DeleteMapping("/room_Method/{id}")
    public void deleteRoomMethod(@PathVariable("id") long id) {
        try {
            roomMethodService.delete(id);
        } catch (WalkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RoomMethod not found");
        }
    }

    // FIXME /Methods/all
    @GetMapping("/all")
    public ResponseEntity<List<Method>> getAllMethod() {
        List<Method> Method = MethodService.getAll();
        return new ResponseEntity<>(Method, HttpStatus.OK);
    }


    @Autowired
    public void setMethodService(MethodService MethodService) {
        this.MethodService = MethodService;
    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    public void setRoomMethodService(RoomMethodService roomMethodService) {
        this.roomMethodService = roomMethodService;
    }
}
