package main.web;

import main.entity.Customer;
import main.entity.Appointment;
import main.entity.Room;
import main.exception.EntityNotFoundException;
import main.model.AppointmentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import main.service.CustomerService;
import main.service.AppointmentService;
import main.service.RoomService;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/Appointments")
public class AppointmentController {
    private AppointmentService AppointmentService;

    private CustomerService customerService;

    private RoomService roomService;

    @PostMapping(value = "/add", consumes = "application/json")
    public void addAppointment(@RequestBody AppointmentModel Appointment) {
        try {
            Customer customer = customerService.getById(Appointment.getCustomerId());
            Room room = roomService.getById(Appointment.getRoomId());
            Date date = Date.valueOf(Appointment.getDate());
            AppointmentService.add(new Appointment(date, room, customer));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable("id") long id) {
        try {
            AppointmentService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void changeConfirmationStatus(@PathVariable("id") long id) {
        try {
            Appointment Appointment = AppointmentService.getById(id);
            boolean currentStatus = Appointment.isConfirmed();
            Appointment.setConfirmed(!currentStatus);
            AppointmentService.add(Appointment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomerAppointments(@PathVariable("id") long id) {
        try {
            AppointmentService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<Appointment>> getCustomerAppointments(@PathVariable("id") long id) {
        List<Appointment> Appointments = AppointmentService.getAllByCustomer(id);
        return new ResponseEntity<>(Appointments, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAppointments() {
        List<Appointment> Appointments = AppointmentService.getAll();
        return new ResponseEntity<>(Appointments, HttpStatus.OK);
    }

    @Autowired
    public void setAppointmentService(AppointmentService AppointmentService) {
        this.AppointmentService = AppointmentService;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
}
