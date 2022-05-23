package main.web;

import main.entity.Method;
import main.entity.Room;
import main.entity.RoomMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.service.RoomMethodService;
import main.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;

    private RoomMethodService roomMethodService;

    @GetMapping("/available/{date}")
    public ResponseEntity<List<Room>> getAvailableRooms(@PathVariable("date") String date) {
        List<Room> rooms = roomService.getAvailable(java.sql.Date.valueOf(date));
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/reserved/{date}")
    public ResponseEntity<List<Room>> getReservedRooms(@PathVariable("date") String date) {
        List<Room> rooms = roomService.getReserved(java.sql.Date.valueOf(date));
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAll();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{id}/Methods")
    public ResponseEntity<List<Method>> getRoomMethods(@PathVariable("id") long id) {
        List<Method> Methods = roomMethodService.getMethodsByRoom(id);
        return new ResponseEntity<>(Methods, HttpStatus.OK);
    }

    @GetMapping("/Methods")
    public ResponseEntity<List<RoomMethod>> getAllRoomsMethods() {
        List<RoomMethod> roomMethods = roomMethodService.getAllRoomsMethods();
        return new ResponseEntity<>(roomMethods, HttpStatus.OK);
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
