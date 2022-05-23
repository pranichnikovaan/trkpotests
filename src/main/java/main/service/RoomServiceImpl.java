package main.service;

import main.entity.Appointment;
import main.entity.Room;
import main.entity.RoomMethod;
import main.exception.EntityNotFoundException;
import main.repository.RoomMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import main.repository.AppointmentRepository;
import main.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AppointmentRepository AppointmentRepository;

    @Autowired
    private RoomMethodRepository roomMethodRepository;

    @Override
    public boolean add(Room room) {
        Optional<Room> roomFromDB = roomRepository.findByName(room.getName());
        if (roomFromDB.isPresent()) {
            return false;
        }
        roomRepository.save(room);
        return true;
    }

    @Override
    public void delete(long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (!room.isPresent()) {
            throw new EntityNotFoundException("Room not found");
        }

        ((List<RoomMethod>) roomMethodRepository.findAll())
            .stream()
            .filter(roomMethod -> roomMethod.getRoom().getId().compareTo(id) == 0)
            .forEach(roomMethod ->  roomMethodRepository.delete(roomMethod));

        ((List<Appointment>) AppointmentRepository.findAll())
            .stream()
            .filter(Appointment -> Appointment.getRoom().getId().compareTo(id) == 0)
            .forEach(Appointment ->  AppointmentRepository.delete(Appointment));

        roomRepository.delete(room.get());
    }

    @Override
    public Room getById(long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (!room.isPresent()) {
            throw new EntityNotFoundException("Room not found");
        }
        return room.get();
    }

    @Override
    public List<Room> getAll() {
        return (List<Room>) roomRepository.findAll();
    }

    @Override
    public List<Room> getAvailable(Date date) {
        Set<Long> reserved = getReservedRoomsStream(date)
                .map(Room::getId)
                .collect(Collectors.toSet());

        return getAll()
                .stream()
                .filter(room -> !reserved.contains(room.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getReserved(Date date) {
        return getReservedRoomsStream(date)
                .collect(Collectors.toList());
    }

    private Stream<Room> getReservedRoomsStream(Date date) {
        return ((List<Appointment>) AppointmentRepository.findAll())
                .stream()
                .filter(Appointment -> Appointment.getDate().compareTo(date) == 0)
                .map(Appointment::getRoom);
    }
}
